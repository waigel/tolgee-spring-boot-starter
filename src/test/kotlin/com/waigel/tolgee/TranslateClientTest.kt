package com.waigel.tolgee

import com.waigel.tolgee.configuration.TolgeeAutoConfiguration
import com.waigel.tolgee.configuration.TolgeeConfiguration
import com.waigel.tolgee.configuration.TolgeeSyncConfiguration
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.ReflectionTestUtils
import kotlin.test.assertEquals

@SpringBootTest(classes = [TolgeeAutoConfiguration::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TranslateClientTest(
) {
    private lateinit var translateClient: TranslateClient;

    @Mock
    private lateinit var tolgeeClient: TolgeeClient;

    @BeforeAll
    fun beforeAll() {
        translateClient =
            TranslateClient(TolgeeConfiguration(forceLanguage = "de-DE"), TolgeeSyncConfiguration(), tolgeeClient)
        ReflectionTestUtils.setField(translateClient, "translations", TestHashMap.create());
        ReflectionTestUtils.setField(translateClient, "configuration", TolgeeConfiguration(
            defaultLanguage = "en"
        ));
    }

    @Test
    fun when_t_function_called_but_key_not_exist_return_key() {
        assertEquals(translateClient.t("TEST"), "TEST")
    }

    @Test
    fun when_t_function_called_and_force_language_exist() {
        ReflectionTestUtils.setField(translateClient, "configuration", TolgeeConfiguration(
            forceLanguage = "de-DE"
        ));
        assertEquals(translateClient.t("TEST_LABEL", "en"), "Test Beschriftung")
    }

    @Test
    fun when_t_function_called_without_language_should_use_default_language() {
        assertEquals(translateClient.t("TEST_LABEL"), "Test label")
    }

    @Test
    fun when_t_function_called_with_prev_language_should_use_prev_language() {
        assertEquals(translateClient.t("TEST_LABEL", "de-DE"), "Test Beschriftung")
    }

    @Test
    fun when_t_function_called_translation_should_replace_params() {
        val params: HashMap<String, String> = linkedMapOf()
        params["name"] = "KlexHub";
        params["created"] = "12.09.1900"
        assertEquals(
            translateClient.t("TEST_LABEL_WITH_PARAMS", "en", params),
            "Test label for KlexHub. Created at 12.09.1900"
        )
    }


}