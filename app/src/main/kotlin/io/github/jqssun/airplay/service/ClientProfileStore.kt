package io.github.jqssun.airplay.service

import android.content.Context
import org.json.JSONObject
import java.security.MessageDigest
import java.util.Locale
import java.util.UUID

data class ReceiverCapabilities(
    val width: Int,
    val height: Int,
    val maxFps: Int,
    val hdr: Boolean,
    val hevc: Boolean,
    val lowLatency: Boolean,
    val tvFingerprint: String,
)

data class ClientPlaybackProfile(
    val displayName: String,
    val model: String,
    val width: Int,
    val height: Int,
    val maxFps: Int,
    val hdr: Boolean,
    val hevc: Boolean,
    val lowLatency: Boolean,
    val tvFingerprint: String,
    val lastSeenMs: Long,
    val lastStreamWidth: Int = 0,
    val lastStreamHeight: Int = 0,
    val lastStreamCodec: String = "",
)

data class ClientProfileDecision(
    val cacheKey: String,
    val profile: ClientPlaybackProfile,
    val reused: Boolean,
)

/** Persists only a one-way hash of an AirPlay sender ID, plus its playback profile. */
class ClientProfileStore(context: Context) {
    private val prefs = context.getSharedPreferences("airplay_client_profiles", Context.MODE_PRIVATE)

    @Synchronized
    fun resolve(
        deviceId: String,
        model: String,
        displayName: String,
        capabilities: ReceiverCapabilities,
    ): ClientProfileDecision {
        val identity = stableIdentity(deviceId, model, displayName)
        val key = "client_${sha256(identity)}"
        val old = prefs.getString(key, null)?.let(::decode)
        val now = System.currentTimeMillis()
        val reused = old != null && old.tvFingerprint == capabilities.tvFingerprint
        val profile = if (reused) {
            old!!.copy(
                displayName = displayName.ifBlank { old.displayName },
                model = model.ifBlank { old.model },
                lastSeenMs = now,
            )
        } else {
            ClientPlaybackProfile(
                displayName = displayName.ifBlank { model.ifBlank { "AirPlay device" } },
                model = model,
                width = capabilities.width,
                height = capabilities.height,
                maxFps = capabilities.maxFps,
                hdr = capabilities.hdr,
                hevc = capabilities.hevc,
                lowLatency = capabilities.lowLatency,
                tvFingerprint = capabilities.tvFingerprint,
                lastSeenMs = now,
            )
        }
        prefs.edit().putString(key, encode(profile)).commit()
        return ClientProfileDecision(key, profile, reused)
    }

    @Synchronized
    fun rememberStream(cacheKey: String?, width: Int, height: Int, codec: String? = null) {
        if (cacheKey.isNullOrBlank()) return
        val old = prefs.getString(cacheKey, null)?.let(::decode) ?: return
        val next = old.copy(
            lastStreamWidth = width.takeIf { it > 0 } ?: old.lastStreamWidth,
            lastStreamHeight = height.takeIf { it > 0 } ?: old.lastStreamHeight,
            lastStreamCodec = codec?.takeIf { it.isNotBlank() } ?: old.lastStreamCodec,
        )
        if (next != old) prefs.edit().putString(cacheKey, encode(next)).apply()
    }

    private fun stableIdentity(deviceId: String, model: String, displayName: String): String {
        val id = deviceId.trim()
        if (id.isNotEmpty()) return "airplay-id:${id.uppercase(Locale.ROOT)}"
        val fallback = listOf(model.trim(), displayName.trim()).filter { it.isNotEmpty() }
        return if (fallback.size == 2) "model-name:${fallback.joinToString("|")}" else "uncached:${UUID.randomUUID()}"
    }

    private fun encode(p: ClientPlaybackProfile) = JSONObject().apply {
        put("version", PROFILE_VERSION)
        put("displayName", p.displayName)
        put("model", p.model)
        put("width", p.width)
        put("height", p.height)
        put("maxFps", p.maxFps)
        put("hdr", p.hdr)
        put("hevc", p.hevc)
        put("lowLatency", p.lowLatency)
        put("tvFingerprint", p.tvFingerprint)
        put("lastSeenMs", p.lastSeenMs)
        put("lastStreamWidth", p.lastStreamWidth)
        put("lastStreamHeight", p.lastStreamHeight)
        put("lastStreamCodec", p.lastStreamCodec)
    }.toString()

    private fun decode(raw: String): ClientPlaybackProfile? = runCatching {
        val j = JSONObject(raw)
        if (j.optInt("version") != PROFILE_VERSION) return null
        ClientPlaybackProfile(
            displayName = j.optString("displayName"),
            model = j.optString("model"),
            width = j.optInt("width"),
            height = j.optInt("height"),
            maxFps = j.optInt("maxFps"),
            hdr = j.optBoolean("hdr"),
            hevc = j.optBoolean("hevc"),
            lowLatency = j.optBoolean("lowLatency"),
            tvFingerprint = j.optString("tvFingerprint"),
            lastSeenMs = j.optLong("lastSeenMs"),
            lastStreamWidth = j.optInt("lastStreamWidth"),
            lastStreamHeight = j.optInt("lastStreamHeight"),
            lastStreamCodec = j.optString("lastStreamCodec"),
        )
    }.getOrNull()

    private fun sha256(value: String): String = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray(Charsets.UTF_8))
        .joinToString("") { "%02x".format(it) }

    private companion object {
        const val PROFILE_VERSION = 1
    }
}
