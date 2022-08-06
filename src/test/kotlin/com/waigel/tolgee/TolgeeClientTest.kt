package com.waigel.tolgee;

import com.waigel.tolgee.configuration.TolgeeAutoConfiguration
import com.waigel.tolgee.configuration.TolgeeConfiguration
import com.waigel.tolgee.models.ExportParams
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.web.client.RequestCallback
import org.springframework.web.client.ResponseExtractor
import org.springframework.web.client.RestTemplate
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest(classes = [TolgeeAutoConfiguration::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TolgeeClientTest(
) {

    @Mock
    private lateinit var restTemplate: RestTemplate;

    private lateinit var tolgeClient: TolgeeClient;

    @BeforeAll
    fun beforeAll() {
        tolgeClient = TolgeeClient(TolgeeConfiguration(), restTemplate)
    }

    @Test
    fun when_translations_exported_unzip_files_correctly() {

        val pathToTestZipWithTranslations = this::class.java.classLoader.getResource("i18n.zip")?.path
        `when`(
            restTemplate.execute(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod::class.java),
                ArgumentMatchers.any(RequestCallback::class.java),
                ArgumentMatchers.any(ResponseExtractor::class.java),
                ArgumentMatchers.any(ExportParams::class.java)
            )
        )
            .thenReturn(File(pathToTestZipWithTranslations!!))

        val exportParams = ExportParams()
        val result = tolgeClient.export(exportParams)

        assertEquals(result.entries.size, 2);
        assertNotNull(result["de-DE"])
        assertNotNull(result["en"])
        assertNull(result["fr"])

        assertEquals(result, TestHashMap.create())

        verify(restTemplate, times(1)).execute(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.any(HttpMethod::class.java),
            ArgumentMatchers.any(RequestCallback::class.java),
            ArgumentMatchers.any(ResponseExtractor::class.java),
            ArgumentMatchers.any(ExportParams::class.java)
        );

    }


}