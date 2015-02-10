package controllers;

import CsodApiKit.CsodApi;
import CsodApiKit.CsodConfig;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Compensation;
import models.User;
import models.UserCompensation;
import play.*;
import play.libs.Json;
import play.mvc.*;

import util.LowerCaseNaming;
import views.html.*;

import java.io.StringReader;
import java.util.ArrayList;

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
            String compJson = api.getData("CurrentCompensation", "$select=UserID,CurrentCompaRatio,CurrentSalary", true);
            String userJson = api.getData("User", "$select=UserID,UserDivision,UserPosition,UserLocation,UserManagerId", true);

            // code to convert to jason
            ObjectMapper compMapper = new ObjectMapper();
            ObjectMapper userMapper = new ObjectMapper();
            compMapper.setPropertyNamingStrategy(new LowerCaseNaming());
            userMapper.setPropertyNamingStrategy(new LowerCaseNaming());
            //Compensation comp = mapper.readValue(compJson, Compensation.class);

            ArrayList<UserCompensation> userComp = new ArrayList<UserCompensation>();
            JsonNode node = null;
            try {
                node = compMapper.readTree(compJson);
                node = node.get("value");
                ArrayList<Compensation> comp = compMapper.readValue(node.toString(), new TypeReference<ArrayList<Compensation>>(){});

                node = userMapper.readTree(userJson);
                node = node.get("value");
                ArrayList<User> users = userMapper.readValue(node.toString(), new TypeReference<ArrayList<User>>(){});

                for(User u : users){
                    for(Compensation c : comp){
                        if(u.getId() == c.getUserID()){
                            userComp.add(new UserCompensation(u,c));
                        }
                    }
                }

            } catch (Exception e) {
                return badRequest(e.getMessage());
            }
            return ok((new ObjectMapper()).writeValueAsString(userComp));
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
