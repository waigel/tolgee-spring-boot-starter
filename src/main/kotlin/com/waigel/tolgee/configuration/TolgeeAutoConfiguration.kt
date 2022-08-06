package com.waigel.tolgee.configuration

import com.waigel.tolgee.TolgeeClient
import com.waigel.tolgee.TranslateClient
import com.waigel.tolgee.exceptions.TolgeeExceptionHandler
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate
import java.io.Serializable


@Configuration
@EnableConfigurationProperties(*[TolgeeConfiguration::class, TolgeeSyncConfiguration::class])
@EnableScheduling
open class TolgeeAutoConfiguration(
    private var tolgeeConfiguration: TolgeeConfiguration,
    private val tolgeeSyncConfiguration: TolgeeSyncConfiguration
) : Serializable {
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = ["tolgee.enabled"], matchIfMissing = true)
    open fun tolgeeClient(): TolgeeClient {
        val restTemplate = RestTemplate()
        restTemplate.errorHandler = TolgeeExceptionHandler()
        return TolgeeClient(tolgeeConfiguration, restTemplate)
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = ["tolgee.enabled"], matchIfMissing = true)
    open fun translateClient(): TranslateClient {
        return TranslateClient(tolgeeConfiguration, tolgeeSyncConfiguration, tolgeeClient())
    }

}
