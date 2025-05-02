package org.enaki.license.jwt_provider

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class JwtProviderApplication

fun main(args: Array<String>) {
    runApplication<JwtProviderApplication>(*args)
}
