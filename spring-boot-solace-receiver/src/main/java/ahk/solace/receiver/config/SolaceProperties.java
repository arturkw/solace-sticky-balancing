package ahk.solace.receiver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolaceProperties {

    @Value("${solace.jms.host}")
    private String host;
    @Value("${solace.jms.msgVpn}")
    private String msgVpn;
    @Value("${solace.jms.clientUsername}")
    private String clientUserName;
    @Value("${solace.jms.clientPassword}")
    private String clientPassword;

    public String getHost() {
        return host;
    }

    public String getMsgVpn() {
        return msgVpn;
    }

    public String getClientUserName() {
        return clientUserName;
    }

    public String getClientPassword() {
        return clientPassword;
    }
}
