package com.waigel.tolgee

import com.waigel.tolgee.configuration.TolgeeConfiguration
import com.waigel.tolgee.configuration.TolgeeSyncConfiguration
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import java.util.regex.Matcher
import java.util.regex.Pattern


open class TranslateClient(
    private var configuration: TolgeeConfiguration,
    private val syncConfiguration: TolgeeSyncConfiguration,
    private var tolgeeClient: TolgeeClient
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var translations: HashMap<String, HashMap<String, String>>? = null

    private fun getTranslations(): HashMap<String, HashMap<String, String>> {
        if (translations == null) {
            try {
                logger.debug("[TOLGEE] Fetch translation from remote server (${configuration.apiUrl})")
                translations = tolgeeClient.export()
            } catch (e: Exception) {
                logger.error("[TOLGEE] Failed to fetch translation from remote server. ${e.localizedMessage}")
            }
        }
        return translations ?: return linkedMapOf()
    }

    @Scheduled(fixedDelayString = "\${tolgee.sync.interval:30000}")
    fun doRefresh() {
        if (!syncConfiguration.enabled) return;
        logger.debug("[TOLGEE] Schedule tolgee translation sync.")
        try {
            val newTranslations = tolgeeClient.export()
            if (newTranslations != translations) {
                logger.debug("[TOLGEE] Translations has been updated")
                translations = newTranslations;
            }
        } catch (e: Exception) {
            logger.error("[TOLGEE] Failed to sync translations. Legacy translations are used if available. ${e.localizedMessage}")
        }
    }


    private fun getTranslationsForLanguage(prevLanguage: String?): HashMap<String, String>? {
        val translations = getTranslations()

        // use language that is forced, if not exist return empty HasMap
        if (configuration.forceLanguage != null) {
            return translations[configuration.forceLanguage];
        }
        if (prevLanguage != null) {
            val maybeTranslations = translations[prevLanguage] ?: return translations[configuration.defaultLanguage];
            return maybeTranslations;
        }
        return translations[configuration.defaultLanguage]
    }

    private fun replaceParamsInTextTranslation(translation: String, params: HashMap<String, String>): String {
        var translationReplaceBuilder = translation;
        val allMatches: MutableList<String> = mutableListOf()
        val regex: Matcher = Pattern.compile("\\{.*?}").matcher(translation)
        while (regex.find()) {
            allMatches.add(regex.group())
        }
        for (paramKeyRaw in allMatches) {
            val paramKey = paramKeyRaw.drop(1).dropLast(1);
            translationReplaceBuilder =
                translationReplaceBuilder.replace(paramKeyRaw, params[paramKey] ?: "");
        }
        return translationReplaceBuilder;
    }

    fun t(key: String): String {
        return t(key, null as String?)
    }

    fun t(key: String, prevLanguage: String?): String {
        val trans = getTranslationsForLanguage(prevLanguage)
        if (trans == null) {
            logger.warn(
                "[TOLGEE] No translation found for selected language. " +
                        "(defaultLanguage=${configuration.defaultLanguage}," +
                        " forceLanguage=${configuration.forceLanguage}, " +
                        "prevLanguage=${prevLanguage})"
            )
            return key;
        }
        return trans[key] ?: return key;
    }

    fun t(key: String, prevLanguage: String?, params: HashMap<String, String>?): String {
        val translation = t(key, prevLanguage)
        if (translation !== key && params != null) {
            return replaceParamsInTextTranslation(translation, params)
        }
        return translation
    }

    fun t(key: String, params: HashMap<String, String>?): String {
        return t(key, params)
    }
}