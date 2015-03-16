package CsodApiKit;

/**
 * Created by dhoffman on 3/16/2015.
 */
public class CsodSession {
    private String sessionSecret;
    private String sessionToken;

    public CsodSession(String sessionToken, String sessionSecret){
        setSessionSecret(sessionSecret);
        setSessionToken(sessionToken);
    }

    public String getSessionSecret() {
        return sessionSecret;
    }

    public void setSessionSecret(String sessionSecret) {
        this.sessionSecret = sessionSecret;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
