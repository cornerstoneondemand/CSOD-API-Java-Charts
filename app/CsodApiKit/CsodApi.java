package CsodApiKit;


import com.fasterxml.jackson.databind.deser.Deserializers;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

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
    /*
    * This function puts the date into the proper format to make the CSOD call
    *
    *
    */
    private String getUtcDate(Date date){
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000");
        ft.setTimeZone(TimeZone.getTimeZone("UTC"));
        return ft.format(date); 
    }
    /*
    * Creates valid CSOD headers to make the call
    *  
     */
    private String getSignature(String url, String sessionToken, String sessionSecret, String httpVerb, String utc) throws Exception {
        
        
        String stringToSign = httpVerb + "\n" + "x-csod-date:" + utc +
                "\n" + "x-csod-session-token:" + sessionToken + "\n" + url;


        byte[] secretBytes = sessionSecret.getBytes("UTF-8");
        byte[] signatureInput = stringToSign.getBytes("UTF-8"); 
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.update(secretBytes);
        byte[] sigBytes = digest.digest(signatureInput);
        String signature = Base64.getEncoder().encodeToString(sigBytes);
        return signature; 
    }

    public String getData(String entity, String query, boolean isDW) throws Exception {
        String verb = "GET";
        String entityUrl = (isDW == true)?"/services/dw/" : "/services/data/";
        String utc = getUtcDate(new Date());
        String encodedQuery = query.replace(" ", "%20");
        
        String signature = getSignature(entityUrl ,getConfig().getSessionToken(), getConfig().getSessionSecret(), verb, utc);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet("https://"+config.getPortal()+entityUrl+entity+"?"+encodedQuery);
        get.setHeader("x-cosd-date", utc);
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

    public CsodConfig getConfig() {
        return config;
    }
}
