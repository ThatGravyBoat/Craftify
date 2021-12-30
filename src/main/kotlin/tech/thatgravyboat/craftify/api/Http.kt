package tech.thatgravyboat.craftify.api

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

    fun call(path: Paths, auth: String, body: String? = null, params: Map<String, String>? = null): Response? {
        if (path.requiresBody && body == null) throw IllegalArgumentException("Body is required for $path")
        if (path.requireParams && params == null) throw IllegalArgumentException("Parameters are required for $path")
        return try {
            val formattedParams = params?.map { (key, value) -> "$key=$value" }?.joinToString("&")
            call(path.url + (formattedParams?.let { "?$it" } ?: ""), path.methodType, auth, body)
        } catch (e: Exception) {
            null
        }
    }

    @Throws(IOException::class)
    fun call(url: String, method: MethodType, auth: String? = null, body: String? = null, contentType: String? = null): Response {
        val connection = URL(url).openConnection() as HttpURLConnection
        if (connection is HttpsURLConnection) connection.sslSocketFactory = sslFactory
        val bytes: ByteArray? = body?.toByteArray(StandardCharsets.UTF_8)
        connection.requestMethod = method.name
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
