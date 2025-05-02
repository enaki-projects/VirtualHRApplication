package org.enaki.license.jwt_provider.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtProperties {
    @Value("\${secretKey}")
    lateinit var secretKey: String

    //validity in milliseconds
    @Value("\${validityInMs}")
    lateinit var validityInMs: String // 1h
}
