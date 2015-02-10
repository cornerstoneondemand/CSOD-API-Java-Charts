package CsodApiKit;

/**
 * This class is for configuring a connection to the CSOD OData APIs
 */
public class CsodConfig {
    private String sessionToken; 
    private String sessionSecret; 
    private String portal;

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionSecret() {
        return sessionSecret;
    }

    public void setSessionSecret(String sessionSecret) {
        this.sessionSecret = sessionSecret;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }
}
