package controllers;

import CsodApiKit.CsodApi;
import CsodApiKit.CsodConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.*;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static Result data(){
        CsodConfig config = new CsodConfig(); 
        config.setPortal("demopm.csod.com");
        config.setSessionToken("1ki43lap4igkv");
        config.setSessionSecret("egNY4u6sJQFYeKB5yMfa4izFGhfbpZKziFoq4kK69pEtVWBicPktJHp7/beji1uLQlWLJm9Rt6TL0PCIurWQUw==");

        CsodApi api = new CsodApi(config); 
        try {
            String result = api.getData("Transcript", "$select=UserID,ObjectID,TranscriptDueDate,TranscriptCompletionDate&$count", true);

            // code to convert to jason
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = null;
            try {
                node = mapper.readTree(result);
            } catch (Exception e) {
                return badRequest(e.getMessage());
            }
            return ok(node);
        }catch (Exception e){
            //do more thorough error handling here
            return badRequest(e.getMessage());
        }
        
    }
    public static Result employees(){
        CsodConfig config = new CsodConfig();
        config.setPortal("demopm.csod.com");
        config.setSessionToken("1ki43lap4igkv");
        config.setSessionSecret("egNY4u6sJQFYeKB5yMfa4izFGhfbpZKziFoq4kK69pEtVWBicPktJHp7/beji1uLQlWLJm9Rt6TL0PCIurWQUw==");

        CsodApi api = new CsodApi(config);
        try {
            String result = api.getData("Employee", "$select=ID, FirstName, LastName &$filter=id gt 12", false);

            // code to convert to jason
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = null;
            try {
                node = mapper.readTree(result);
            } catch (Exception e) {
                return badRequest(e.getMessage());
            }
            return ok(node);
        }catch (Exception e){
            //do more thorough error handling here
            return badRequest(e.getMessage());
        }

    }

}
