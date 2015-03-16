package CsodApiKit;


import com.fasterxml.jackson.databind.deser.Deserializers;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Base64;

/**
 * Created by dhoffman on 2/5/2015.
 */
public class CsodApi {
    
    private CsodConfig config;
    
    public CsodApi(CsodConfig config){
        this.config = config;     
        
    }

    /**
     * This function puts the date into the proper format to make the CSOD call
     * @param date the date to be converted
     * @return the utc date formatted for CSOD
     */
    private String getUtcDate(Date date){
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000");
        ft.setTimeZone(TimeZone.getTimeZone("UTC"));
        return ft.format(date); 
    }

    /**
     * Creates valid CSOD headers to make the call
     *  It uses SHA512 encryption
     * @param secretString The session or api secret
     * @param stringToSign The CSOD formatted string to sign. Could be an API or session call
     * @return The signature for the call.
     * @throws Exception Throws a generic exception if the hash cannot be generated
     */
    private String getSignature(String secretString, String stringToSign) throws Exception {

        byte[] secretBytes = Base64.getDecoder().decode(secretString);  //sessionSecret.getBytes("UTF-8");
        byte[] signatureInput = stringToSign.getBytes("UTF-8");

        SecretKeySpec secretkey = new SecretKeySpec(secretBytes, "HmacSHA512");

        Mac hmac = Mac.getInstance("HmacSHA512");
        hmac.init(secretkey);
        byte[] data = hmac.doFinal(signatureInput);

        String signature = Base64.getEncoder().encodeToString(data);

        return signature; 
    }

    /**
     * Creates valid CSOD headers to make the call
     *  It uses SHA512 encryption
     * @param url The relative entity URL. This does not contain the query string (e.g. /services/data/Employee)
     * @param apiToken The api token
     * @param apiSecret The api secret
     * @param httpVerb The call verb (GET, POST, PUT)
     * @param utc The CSOD formatted utc date @see getUtcDate
     * @return The signature for the call.
     * @throws Exception Throws a generic exception if the hash cannot be generated
     */
    private String getApiSignature(String url, String apiToken, String apiSecret, String httpVerb, String utc) throws Exception {


        String stringToSign = httpVerb + "\n" + "x-csod-date:" + utc +
                "\n" + "x-csod-session-token:" + apiToken + "\n" + url;

        return getSignature(apiSecret, stringToSign);
    }


    /**
     * Creates valid CSOD headers to make the call
     *  It uses SHA512 encryption
     * @param url The relative entity URL. This does not contain the query string (e.g. /services/data/Employee)
     * @param sessionToken The session token
     * @param sessionSecret The session secret
     * @param httpVerb The call verb (GET, POST, PUT)
     * @param utc The CSOD formatted utc date @see getUtcDate
     * @return The signature for the call.
     * @throws Exception Throws a generic exception if the hash cannot be generated
     */
    private String getSessionSignature(String url, String sessionToken, String sessionSecret, String httpVerb, String utc) throws Exception {
        String stringToSign = httpVerb + "\n" + "x-csod-date:" + utc +
                "\n" + "x-csod-session-token:" + sessionToken + "\n" + url;
        return getSignature(sessionSecret, stringToSign);
    }

    /*
    *  This is a simple way to make a get call to the CSOD
    * @param entity The entity in the CSOD api that you are referencing
    * @param query The associated OData query for the entity. It can be blank
    * @param isDW Specifies to call the Data Warehouse if true. If false it calls the transactional database
    * 
    */
    public String getData(String entity, String query, boolean isDW) throws Exception {
        String verb = "GET";
        String entityUrl = (isDW)?"/services/dwdata/"+entity : "/services/data/"+entity;
        String utc = getUtcDate(new Date());
        String encodedQuery = query.replace(" ", "%20");
        
        String signature = getSessionSignature(entityUrl, getConfig().getSessionToken(), getConfig().getSessionSecret(), verb, utc);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet("https://"+config.getPortal()+entityUrl+"?"+encodedQuery);
        get.setHeader("x-csod-date", utc);
        get.setHeader("x-csod-session-token", getConfig().getSessionToken());
        get.setHeader("x-csod-signature", signature);

        CloseableHttpResponse response = client.execute(get);

//        URL url = new URL("https://"+entityUrl+entity+"?"+encodedQuery);
//        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//        con.setRequestMethod(verb);
//        con.setRequestProperty("x-cosd-date", utc);
//        con.setRequestProperty("x-csod-session-token", getConfig().getSessionToken());
//        con.setRequestProperty("x-csod-signature", signature);
        
//        con.connect();
        
//        int status = con.getResponseCode();

        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        switch (response.getStatusLine().getStatusCode()) {

            case 200:
            case 201:
                return sb.toString();
            //anything else is an error so throw an exception
            default:
                throw new Exception(sb.toString());
        }
        //return null;
        
    }

    /**
     *
     * @param userName the username for the user you want to generate the session for
     * @param sessionName the arbitrary name for the session that you are generating
     * @return
     */
    public String generateSession(String userName, String sessionName) throws Exception{
        String url = "https://"+getConfig().getPortal()+"/services/api/sts/"+userName+"/"+sessionName;
        String utc = getUtcDate(new Date());
        String signature = getApiSignature(url, getConfig().getApiToken(), getConfig().getApiSecret(), "GET", utc);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        get.setHeader("x-csod-date", utc);
        get.setHeader("x-csod-session-token", getConfig().getApiToken());
        get.setHeader("x-csod-signature", signature);

        CloseableHttpResponse response = client.execute(get);

        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        switch (response.getStatusLine().getStatusCode()) {

            case 200:
            case 201:
                return sb.toString();
            //anything else is an error so throw an exception
            default:
                throw new Exception(sb.toString());
        }

    }

    public CsodConfig getConfig() {
        return config;
    }
}
