package org.enaki.license.jwt_provider.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.enaki.license.jwt_provider.rest_consuming.models.AuthUserModelForJwt
import org.enaki.license.jwt_provider.utils.ServerEnvUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import java.nio.charset.StandardCharsets
import java.util.*
import javax.annotation.PostConstruct
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

        val bytes = jwtProperties!!.secretKey.toByteArray(StandardCharsets.UTF_8)
        secretKey = Keys.hmacShaKeyFor(bytes)

        //secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString))
        // secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
    }

    fun decodeAndParseJwt(jwt: String?): DecodedJwt {
        val base64DecodeJwt = decodeWithBase64(jwt)
        return parseJwt(base64DecodeJwt)
    }

    fun parseJwt(base64DecodeJwt: String?): DecodedJwt {
        val claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyString)
                .build()
                .parseClaimsJws(base64DecodeJwt)
        val decodedJwt = DecodedJwt()
        decodedJwt.jti = claims.body.id
        decodedJwt.expiration = claims.body.expiration
        decodedJwt.roles = claims.body["roles"] as List<String>?
        decodedJwt.uid = claims.body.subject
        return decodedJwt
    }

    fun createToken(authUserModelForJwt: AuthUserModelForJwt): String {
        val claims = Jwts.claims()
        claims["jti"] = authUserModelForJwt.email
        claims["roles"] = authUserModelForJwt.roles
        claims["type"] = "access"
        val now = Date()
        val validity = Date(now.time + jwtProperties!!.validityInMs.toLong())
        val header = Jwts.header();
        header.type = "JWT";
        return Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .setIssuer(serverEnvUtils.serverUrlPrefix)
                .setSubject(authUserModelForJwt.uid.toString())
                .setExpiration(validity)
                .setIssuedAt(now)
                .setNotBefore(now)
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
}