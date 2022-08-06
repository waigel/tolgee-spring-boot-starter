package com.waigel.tolgee.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "tolgee.sync")
class TolgeeSyncConfiguration(
    var enabled: Boolean = false,
)