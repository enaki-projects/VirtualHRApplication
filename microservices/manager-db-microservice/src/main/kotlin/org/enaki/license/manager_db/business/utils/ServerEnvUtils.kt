package org.enaki.license.manager_db.business.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.net.InetAddress
import java.net.UnknownHostException


@Component
class ServerEnvUtils {
    @Autowired
    var environment: Environment? = null

    /**
     * Get port.
     *
     * @return
     */
    var port: String? = null
        get() {
            if (field == null) field = environment!!.getProperty("local.server.port")
            return field
        }

    /**
     * Get hostname.
     *
     * @return
     */
    @get:Throws(UnknownHostException::class)
    var hostname: String? = null
        get() {
            if (field == null) field = InetAddress.getLocalHost().hostAddress
            return field
        }

    /**
     * Get port, as Integer.
     *
     * @return
     */
    val portAsInt: Int
        get() = Integer.valueOf(port)

    @get:Throws(UnknownHostException::class)
    val serverUrlPrefix: String
        get() = "http://$hostname:$port"
}
