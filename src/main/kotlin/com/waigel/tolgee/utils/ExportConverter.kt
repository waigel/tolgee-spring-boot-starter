package com.waigel.tolgee.utils

import com.google.gson.Gson
import com.waigel.tolgee.exceptions.TolgeeExportFileException
import java.io.File
import java.io.InputStream
import java.util.Scanner
import java.util.zip.ZipException
import java.util.zip.ZipFile

class ExportConverter {

    private var gson: Gson? = null

    private fun getGson(): Gson {
        if (gson == null) {
            gson = Gson();
        }
        return gson as Gson
    }

    @Throws(TolgeeExportFileException::class)
    fun unzip(file: File): HashMap<String, HashMap<String, String>> {
        val translations: HashMap<String, HashMap<String, String>> = linkedMapOf();
        try {
            val zipFile = ZipFile(file)
            for (entry in zipFile.entries().toList()) {
                val entryStream: InputStream = zipFile.getInputStream(entry)
                val entryContent = Scanner(entryStream).useDelimiter("//A").next()

                val keyTranslationMap: HashMap<String, String> =
                    getGson().fromJson(entryContent, HashMap<String, String>().javaClass)
                translations[entry.name.removeSuffix(".json")] = keyTranslationMap;
            }
        } catch (e: ZipException) {
            throw TolgeeExportFileException(e.localizedMessage)
        }
        return translations;
    }
}