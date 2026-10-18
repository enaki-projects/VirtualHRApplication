package org.enaki.license.jwt_provider.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.enaki.license.jwt_provider.rest_consuming.models.AuthUserModelForJwt
import org.enaki.license.jwt_provider.utils.ServerEnvUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import java.nio.charset.StandardCharsets
import java.util.*
import jakarta.annotation.PostConstruct
import javax.crypto.SecretKey


@Component
class JwtTokenProvider {
    @Autowired
    lateinit var serverEnvUtils: ServerEnvUtils
    @Autowired
    var jwtProperties: JwtProperties? = null
    final var secretKey: SecretKey? = null
        private set
    final var secretKeyString: String? = null
        private set

    @PostConstruct
    protected fun init() {
        secretKeyString = jwtProperties!!.secretKey

        if (secretKeyString!!.startsWith("dev-only-change-me")) {
            log.warn("JWT_SECRET is not set - using the insecure dev-only fallback key. " +
                    "Set the JWT_SECRET env var (>= 32 bytes) before deploying.")
        }

        // The secret is used as raw UTF-8 bytes (NOT base64-decoded). jjwt 0.12 requires the HMAC
        // key material to be at least 256 bits (32 bytes) for HS256, which hmacShaKeyFor enforces.
        val bytes = secretKeyString!!.toByteArray(StandardCharsets.UTF_8)
        secretKey = Keys.hmacShaKeyFor(bytes)
    }

    fun decodeAndParseJwt(jwt: String?): DecodedJwt {
        val base64DecodeJwt = decodeWithBase64(jwt)
        return parseJwt(base64DecodeJwt)
    }

    @Suppress("UNCHECKED_CAST")
    fun parseJwt(base64DecodeJwt: String?): DecodedJwt {
        val claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(base64DecodeJwt)
                .payload
        val decodedJwt = DecodedJwt()
        decodedJwt.jti = claims.id
        decodedJwt.expiration = claims.expiration
        decodedJwt.roles = claims["roles"] as List<String>?
        decodedJwt.uid = claims.subject
        return decodedJwt
    }

    fun createToken(authUserModelForJwt: AuthUserModelForJwt): String {
        val claims = mutableMapOf<String, Any?>()
        claims["jti"] = authUserModelForJwt.email
        claims["roles"] = authUserModelForJwt.roles
        claims["type"] = "access"
        val now = Date()
        val validity = Date(now.time + jwtProperties!!.validityInMs.toLong())
        return Jwts.builder()
                .header().type("JWT").and()
                .claims(claims)
                .issuer(serverEnvUtils.serverUrlPrefix)
                .subject(authUserModelForJwt.uid.toString())
                .expiration(validity)
                .issuedAt(now)
                .notBefore(now)
                .signWith(secretKey)
                .compact()
    }

    fun encodeWithBase64(jwt: String): String {
        return Base64.getEncoder().encodeToString(jwt.toByteArray())
    }

    fun decodeWithBase64(jwt: String?): String {
        Assert.notNull(jwt, "The jwt must not be null")
        Assert.hasLength(jwt, "The jwt must not be empty")
        return String(Base64.getDecoder().decode(jwt), StandardCharsets.UTF_8)
    }

    companion object {
        private val log = LoggerFactory.getLogger(JwtTokenProvider::class.java)
    }
}
