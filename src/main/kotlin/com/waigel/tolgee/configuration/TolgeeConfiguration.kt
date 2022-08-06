package com.waigel.tolgee.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "tolgee")
class TolgeeConfiguration(

    var apiKey: String = "",
    var apiUrl: String = "https://app.tolgee.io/v2",
    var forceLanguage: String? = null,
    var defaultLanguage: String = "en",
    )