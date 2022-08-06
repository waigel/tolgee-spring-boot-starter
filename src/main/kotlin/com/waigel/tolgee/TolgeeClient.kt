package com.waigel.tolgee


import com.waigel.tolgee.configuration.TolgeeConfiguration
import com.waigel.tolgee.exceptions.TolgeeExceptionHandler
import com.waigel.tolgee.exceptions.TolgeeRequestForbiddenException
import com.waigel.tolgee.models.ExportParams
import com.waigel.tolgee.utils.ExportConverter
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.client.ClientHttpRequest
import org.springframework.http.client.ClientHttpResponse
import org.springframework.util.StreamUtils
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.io.File
import java.io.FileOutputStream


open class TolgeeClient(
    private var configuration: TolgeeConfiguration,
    private var restTemplate: RestTemplate

) {

    @Throws(TolgeeRequestForbiddenException::class)
    fun export(exportParams: ExportParams?): HashMap<String, HashMap<String, String>> {
        val file: File = restTemplate.execute(
            createUriBuilder().path("/projects/export")
                .toUriString(), HttpMethod.GET,
            { clientHttpRequest: ClientHttpRequest ->
                clientHttpRequest.headers.add("X-API-Key", configuration.apiKey)
            },
            { clientHttpResponse: ClientHttpResponse ->
                val ret: File = File.createTempFile("tolgee_export_download", ".zip")
                StreamUtils.copy(clientHttpResponse.body, FileOutputStream(ret))
                ret
            }, exportParams
        ) as File

        return ExportConverter().unzip(file);
    }

    @Throws(TolgeeRequestForbiddenException::class)
    fun export(): HashMap<String, HashMap<String, String>> {
        return this.export(null)
    }


    private fun createUriBuilder(): UriComponentsBuilder {
        return UriComponentsBuilder.fromUriString(configuration.apiUrl)
    }

}