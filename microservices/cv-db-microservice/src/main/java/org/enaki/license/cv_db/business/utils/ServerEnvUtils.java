package org.enaki.license.cv_db.business.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Scope("singleton")
public class ServerEnvUtils {
    @Autowired
    Environment environment;

    private String port;
    private String hostname;

    /**
     * Get port.
     *
     * @return
     */
    public String getPort() {
        if (port == null) port = environment.getProperty("local.server.port");
        return port;
    }

    /**
     * Get port, as Integer.
     *
     * @return
     */
    public Integer getPortAsInt() {
        return Integer.valueOf(getPort());
    }

    /**
     * Get hostname.
     *
     * @return
     */
    public String getHostname() throws UnknownHostException {
        hostname = environment.getProperty("cv_db_microservice");
        if (hostname == null) hostname = InetAddress.getLocalHost().getHostAddress();
        return hostname;
    }

    public String getServerUrlPrefix() throws UnknownHostException {
        if (environment.getProperty("cv_db_microservice") == null)
            return "http://" + getHostname() + ":" + getPort();
        return getHostname();
    }
}
