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
import models.UserCompensationGroup;
import play.*;
import play.libs.Json;
import play.mvc.*;

import util.LowerCaseNaming;
import views.html.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    /**
     * This is an example of calling the CSOD data warehouse and combining the data from two calls.
     * @return The JSON representation of a the aggregate of user compensation for a location
     */
    public static Result data(){
        CsodConfig config = new CsodConfig(); 
        config.setPortal(Play.application().configuration().getString("csod.portal"));
        config.setSessionToken(Play.application().configuration().getString("csod.sessionToken"));
        config.setSessionSecret(Play.application().configuration().getString("csod.sessionSecret"));
        config.setApiSecret(Play.application().configuration().getString("csod.apiSecret"));
        config.setApiToken(Play.application().configuration().getString("csod.apiToken"));



        CsodApi api = new CsodApi(config);



        try {
            String sessionInfo = api.generateSession("dhoffman", "testSession53");

            String compJson = api.getData("CurrentCompensation", "$select=UserID,CurrentCompaRatio,CurrentSalary", true);
            String userJson = api.getData("User", "$select=UserID,UserDivision,UserPosition,UserLocation,UserManagerId", true);

            // code to convert from JSON
            ObjectMapper compMapper = new ObjectMapper();
            ObjectMapper userMapper = new ObjectMapper();
            compMapper.setPropertyNamingStrategy(new LowerCaseNaming());
            userMapper.setPropertyNamingStrategy(new LowerCaseNaming());

            //set up grouping
            HashMap<String, UserCompensationGroup> groupByLocation = new HashMap<String, UserCompensationGroup>(); 
            
            JsonNode node = null;
            try {
                node = compMapper.readTree(compJson);
                node = node.get("value");
                ArrayList<Compensation> comp = compMapper.readValue(node.toString(), new TypeReference<ArrayList<Compensation>>(){});

                node = userMapper.readTree(userJson);
                node = node.get("value");
                ArrayList<User> users = userMapper.readValue(node.toString(), new TypeReference<ArrayList<User>>(){});

                //join and group data
                for(User u : users){
                    for(Compensation c : comp){
                        if(u.getId() == c.getUserID()){
                           
                            UserCompensationGroup group; 
                            if(groupByLocation.containsKey(u.getUserLocation())){
                                group = groupByLocation.get(u.getUserLocation()); 
                            }else{
                                group = new UserCompensationGroup(); 
                                group.setGroupName(u.getUserLocation());
                                groupByLocation.put(u.getUserLocation(), group); 
                            }
                            group.addCompaRatio(c.getCurrentCompaRatio());
                            group.addTotalSalary(c.getCurrentSalary());
                            group.setGroupCount(group.getGroupCount()+1);
                            
                        }
                    }
                }

            } catch (Exception e) {
                return badRequest(e.getMessage());
            }
            return ok((new ObjectMapper()).writeValueAsString(groupByLocation));
        }catch (Exception e){
            //do more thorough error handling here
            return badRequest(e.getMessage());
        }
        
    }
    public static Result employees(){
        CsodConfig config = new CsodConfig();
        config.setPortal(Play.application().configuration().getString("csod.portal"));
        config.setSessionToken(Play.application().configuration().getString("csod.sessionToken"));
        config.setSessionSecret(Play.application().configuration().getString("csod.sessionSecret"));

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
