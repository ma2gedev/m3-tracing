package com.m3.tracing.http

/**
 * Represents HTTP request.
 * Implementation depends on framework (Servlet, Play Framework, ...).
 *
 * This interface is intended NOT to consume stream of request body to prevent breaking application.
 * Thus this interface does NOT provide any information depends on request body (includes request "parameter").
 */
interface HttpRequestInfo {
    /**
     * Get metadata (e.g. Remote IP address) of this request.
     * @return If given key is not supported or no value supplied, return null.
     */
    fun <T> tryGetMetadata(key: HttpRequestMetadataKey<T>): T?

    val url: String?; get() = this.tryGetMetadata(HttpRequestMetadataKey.Url)

    /**
     * Get value of header.
     * @return If given header is not available, return null.
     */
    fun tryGetHeader(name: String): String?
}

/**
 * Represents type (key) of metadata.
 * Any subclass must be singleton so that implementation can rely on instance reference equality.
 */
abstract class HttpRequestMetadataKey<T> {

    /** URL of the request WITHOUT query parameters and credentials. */
    object Url: HttpRequestMetadataKey<String>()

    /** Hostname of the request destination */
    object Host: HttpRequestMetadataKey<String>()

    /** HTTP method name */
    object Method: HttpRequestMetadataKey<String>()

    /** Path of the request */
    object Path: HttpRequestMetadataKey<String>()

    /** Length of the body */
    object ContentLength: HttpRequestMetadataKey<Long>()

    /** Remote IP address of the request. Should consider X-Forwarded-For also. */
    object RemoteAddr: HttpRequestMetadataKey<String>()
}
