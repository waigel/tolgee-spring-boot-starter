package com.waigel.tolgee.annotations


@Target(*[AnnotationTarget.TYPE])
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class EnableTolgee(
    val timeout: Int = 0,
    val version: String = "",
    val weight: Int = 0,
    val numberOfRetries: Int = 0,
    val circuitBreakerErrors: Int = 0
)