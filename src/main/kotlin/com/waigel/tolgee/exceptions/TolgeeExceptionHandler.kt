package com.waigel.tolgee.exceptions

import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler
import java.io.IOException
import java.util.Scanner

class TolgeeExceptionHandler : DefaultResponseErrorHandler() {
    @Throws(IOException::class)
    override fun handleError(response: ClientHttpResponse) {
        val content = Scanner(response.body).useDelimiter("\\A").next();
        if (response.statusCode.value() == 403) {
            throw TolgeeRequestForbiddenException()
        }
        if (response.statusCode.is4xxClientError){
            throw TolgeeBadRequestException(content, response.statusCode.value())
        }
        if (response.statusCode.is5xxServerError){
            throw TolgeeInternalServerException(content, response.statusCode.value())
        }
    }
}