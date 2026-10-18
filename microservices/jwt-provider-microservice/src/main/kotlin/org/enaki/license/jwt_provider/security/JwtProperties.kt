package org.enaki.license.jwt_provider.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtProperties {
    // Signing secret. Sourced from the JWT_SECRET env var. The dev fallback is obviously fake and
    // JwtTokenProvider logs a warning when it is in use. Must be >= 32 bytes (256 bits) for HS256.
    @Value("\${JWT_SECRET:dev-only-change-me-dev-only-change-me-0123456789}")
    lateinit var secretKey: String

    // Token validity in milliseconds.
    @Value("\${JWT_VALIDITY_MS:\${validityInMs:36000000}}")
    lateinit var validityInMs: String
}
