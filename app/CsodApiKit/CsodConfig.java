package CsodApiKit;

/**
 * This class is for configuring a connection to the CSOD OData APIs
 */
public class CsodConfig {

    private String apiToken;
    private String apiSecret;
    private String portal;



    private CsodSession session;

    public CsodSession getSession() {
        return session;
    }

    public void setSession(CsodSession session) {
        this.session = session;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }
}
