package tech.thatgravyboat.craftify.services

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

object Http {

    private val sslFactory: SSLSocketFactory = SSLContext.getInstance("SSL").apply {
        init(
            null,
            arrayOf(
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {}
                    override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {}
                    override fun getAcceptedIssuers() = null
                }
            ),
            SecureRandom()
        )
    }.socketFactory

    fun post(url: String, auth: String? = null, body: String? = null, contentType: String? = null): Response? {
        return try {
            call(url, auth, body, contentType)
        } catch (e: Exception) {
            null
        }
    }

    @Throws(IOException::class)
    fun call(url: String, auth: String? = null, body: String? = null, contentType: String? = null): Response {
        val connection = URL(url).openConnection() as HttpURLConnection
        if (connection is HttpsURLConnection) connection.sslSocketFactory = sslFactory
        val bytes: ByteArray? = body?.toByteArray(StandardCharsets.UTF_8)
        connection.requestMethod = "POST"
        connection.useCaches = true
        connection.addRequestProperty("User-Agent", "Mozilla/4.76 (Craftify)")
        contentType?.let { connection.addRequestProperty("Content-Type", it) }
        auth?.let { connection.addRequestProperty("Authorization", "Bearer $it") }
        bytes?.apply { connection.addRequestProperty("Content-Length", size.toString()) }
        connection.readTimeout = 15000
        connection.connectTimeout = 15000
        connection.doOutput = true
        bytes?.let {
            connection.doInput = true
            connection.outputStream.write(it)
        }
        val code = connection.responseCode
        return if (code / 100 != 2) Response(responseCode = code) else Response(connection.inputStream, code)
    }
}

data class Response(val inputStream: InputStream? = null, val responseCode: Int)
