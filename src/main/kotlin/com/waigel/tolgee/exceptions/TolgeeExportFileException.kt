package com.waigel.tolgee.exceptions

class TolgeeExportFileException(error: String): Exception("EXPORT - Failed to unzip/read the exported files. Error: $error")