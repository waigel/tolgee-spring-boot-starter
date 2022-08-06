package com.waigel.tolgee.exceptions

class TolgeeInternalServerException(message: String, statusCode: Int): Exception("INTERNAL_SERVER_ERROR (status=$statusCode)- Request failed: $message")