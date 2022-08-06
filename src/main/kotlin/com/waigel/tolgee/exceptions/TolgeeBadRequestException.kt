package com.waigel.tolgee.exceptions

class TolgeeBadRequestException (message: String, statusCode: Int): Exception("BAD_REQUEST (status=$statusCode)- Request failed:  $message")