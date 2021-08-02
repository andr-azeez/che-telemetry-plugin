package org.my.group;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import org.json.simple.JSONObject;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
// import org.apache.http.entity.StringEntity;

// import  org.apache.http.entity.ContentType;

// import com.google.common.annotations.VisibleForTesting;
import org.eclipse.che.api.core.rest.HttpJsonRequestFactory;
// import org.apache.http.impl.client.HttpClientBuilder;
// import org.apache.http.client.HttpClient;


import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalNotification;


// import org.apache.http.client.methods.HttpPost;
// import org.apache.http.HttpResponse;

import org.eclipse.che.incubator.workspace.telemetry.base.AbstractAnalyticsManager;
import org.eclipse.che.incubator.workspace.telemetry.base.AnalyticsEvent;
import static org.eclipse.che.incubator.workspace.telemetry.base.AnalyticsEvent.*;
import static org.eclipse.che.multiuser.machine.authentication.shared.Constants.USER_NAME_CLAIM;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.UUID;


import org.eclipse.che.api.core.model.workspace.Workspace;
// import org.eclipse.che.incubator.workspace.telemetry.base.AbstractAnalyticsManager.getCommonProperties;

import org.eclipse.che.api.core.model.workspace.Workspace;
import org.eclipse.che.api.workspace.shared.dto.WorkspaceDto;





public class AnalyticsManager extends AbstractAnalyticsManager {

    private long inactiveTimeLimt = 60000 * 12;
    private long last_save =  System.currentTimeMillis();
    private int current_run = 0;
    private  String  getmachineToken = "";
    private  String  str_stackid = "";
    private  UUID  gen_uuid = UUID.randomUUID();
    // final protected String stackId;
    
      private HttpJsonRequestFactory requestFactory;



    // @VisibleForTesting
    // ScheduledExecutorService networkExecutor = Executors.newSingleThreadScheduledExecutor(
    //         new ThreadFactoryBuilder().setNameFormat("Analytics Network Request Submitter").build());

    // @VisibleForTesting
    // LoadingCache<String, EventDispatcher> dispatchers;

    // @VisibleForTesting
    // HttpUrlConnectionProvider httpUrlConnectionProvider = null;

      
    

    public AnalyticsManager(String apiEndpoint, String workspaceId, String machineToken,
            HttpJsonRequestFactory requestFactory) {

        
        super(apiEndpoint, workspaceId, machineToken, requestFactory);
        // String endpoint = apiEndpoint + "/workspace/" + workspaceId;
        
        // System.out.println(stackName);
            // String endpoint = apiEndpoint + "/workspace/" + workspaceId;

            //  Workspace workspace = getWorkspace(endpoint);

            


        // Workspace workspace = getWorkspace(endpoint);
        // stackId = workspace.getAttributes().get("stackName");
        // System.out.println(stackId);
        // String endpoint = apiEndpoint + "/workspace/" + workspaceId;

        // Workspace workspace = getWorkspace(endpoint);
        // str_stackid = workspace.getAttributes().get("stackName");
        // System.out.println(str_stackid);
        // System.out.println(stackId);
        getmachineToken = machineToken;
        // System.out.println(getUserNameFromMachineToken(machineToken));
        System.out.println("*******^^^^^^^^^^^^^^^^^^^^^^^^getUserNameFromMachineToken");

        // System.out.println(getUserNameFromMachineToken(machineToken));

        

    }


    private Workspace getWorkspace(String endpoint) {
    try {
      return this.requestFactory.fromUrl(endpoint).request().asDto(WorkspaceDto.class);
    } catch (Exception e) {
                e.printStackTrace();
                System.out.println("^^^^^^^^^^^^^^^^ Exception");

            }
            return null;

  }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void destroy() {
        onEvent(WORKSPACE_STOPPED, userId, lastIp, lastUserAgent, lastResolution, commonProperties);
        // TODO Auto-generated method stub
    }

    @Override
    public void onEvent(AnalyticsEvent event, String ownerId, String ip, String userAgent, String resolution,
            Map<String, Object> properties) {
        // TODO Auto-generated method stub
        // Connection conn = null;
        


        HashMap<String, Object> eventPayload = new HashMap<String, Object>(properties);

        eventPayload.put("event", event);
        JSONObject jsonmsg = new JSONObject(properties);
                

        // System.out.println(username);
        
            

        JwtParser jwtParser = Jwts.parser();
        


        if (System.currentTimeMillis() - last_save >= 2400 && current_run==0) {
            current_run = 1;

            // System.out.println(ip);
            // System.out.println(userAgent);
            // System.out.println(resolution);
            // System.out.println("*******************_________&&&&&&&");

            // System.out.println("event:");
            // System.out.println(event);
            // System.out.println("jsonmsg:");
            // System.out.println(jsonmsg);
            // System.out.println("ownerId:");
            // System.out.println(getUserId());
            // System.out.println(ownerId);






             Connection conn = null;
            PreparedStatement stmt = null;
            try {

                System.out.println("eventeventeventeventevent");
                System.out.println(event);
                System.out.println( getValueOrDefault(ip,"NA"));
                conn = DriverManager.getConnection(
                    "jdbc:mysql://15.207.183.248:3309/che_db","che_user","che_password");
                // stmt = conn.prepareStatement("INSERT INTO person (name, email) values (?, ?)");
                stmt = conn.prepareStatement("insert into che_logs(log_type,log_data,user_id,ip,user_agent,session_id,workspace_id,workspace_name,user_name) values(?,?,?,?,?,?,?,?,?)");
                // stmt = "insert into che_logs(log_type,log_data,user_id,ip,user_agent,session_id,workspace_id,workspace_name,user_name) values(?,?,?,?,?,?,?,?,?)";
                // stmt = "insert into che_logs(log_type,log_data,user_id,ip,user_agent,session_id,workspace_id,workspace_name,user_name) values('"+event+"','"+jsonmsg.toString()+"','"+getUserId()+"','"+ip+"','"+userAgent+"','"+gen_uuid+"','"+workspaceId+"','"+workspaceName+"','"+getUserNameFromMachineTokenInternal(getmachineToken)+"')";

                System.out.println(jsonmsg.toString());
                System.out.println(createdOn);
                System.out.println(stackId);
                stmt.setString(1, event.toString()); //log_type
                stmt.setString(2, jsonmsg.toString()); //log_data
                stmt.setString(3, getUserId()); //user_id
                stmt.setString(4, getValueOrDefault(ip,"NA")); //ip
                stmt.setString(5, getValueOrDefault(userAgent,"NA")); //ip
                stmt.setString(6, gen_uuid.toString()); //session_id
                stmt.setString(7, workspaceId); //workspace_id
                stmt.setString(8, workspaceName); //workspace_name
                stmt.setString(9, getUserNameFromMachineTokenInternal(getmachineToken)); //user_name
                stmt.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("^^^^^^^^^^^^^^^^ Exception");
                System.out.println(e);

            }
            finally {
                try {
                    if (stmt != null) { stmt.close(); }
                }
                catch (Exception e) {
                    // log this error
                }
                try {
                    if (conn != null) { conn.close(); }
                }
                catch (Exception e) {
                    // log this error
                }
            }










            // ######################


        //     try{

        //         DriverManager.getDrivers();

        //         // conn = DriverManager.getConnection(
        //         //     "jdbc:postgresql://13.233.70.170:5432/che_telemetry","telemetry_user","SpringTelemetry123");
                
        //         System.out.println("");
        //         System.out.println("");
        //         System.out.println("");
        //         System.out.println(workspaceName);
        //         System.out.println(ownerId);
        //         System.out.println(workspaceId);
        //         System.out.println(getUserNameFromMachineTokenInternal(getmachineToken));

        //         System.out.println("");
        //         System.out.println("");
        //         conn = DriverManager.getConnection(
        //             "jdbc:mysql://15.207.183.248:3309/che_db","che_user","che_password");
                
        //         Statement stmt = conn.createStatement();
        //         if (conn != null) {
        //             System.out.println("^^^^^^^^^^^^^^^^ Connected to the database!");


        //             String sql = "insert into che_logs(log_type,log_data,user_id,ip,user_agent,session_id,workspace_id,workspace_name,user_name) values('"+event+"','"+jsonmsg.toString()+"','"+getUserId()+"','"+ip+"','"+userAgent+"','"+gen_uuid+"','"+workspaceId+"','"+workspaceName+"','"+getUserNameFromMachineTokenInternal(getmachineToken)+"')";
        //             // sql = "INSERT INTO che_logs VALUES (101, 'Mahnaz', 'Fatma', 25)";
        //             stmt.executeUpdate(sql);
        //             current_run = 0;
        //         } else {
        //             System.out.println("^^^^^^^^^^^^^^^^ Failed to make connection!");
        //         }

        //     } catch (SQLException e) {
        //         System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        //         System.out.println("^^^^^^^^^^^^^^^^ SQLException");

        //     } catch (Exception e) {
        //         e.printStackTrace();
        //         System.out.println("^^^^^^^^^^^^^^^^ Exception");

        //     }
        //     finally {
        //         current_run = 0;
        //     // if (rs != null) {
        //     //     try {
        //     //         rs.close();
        //     //     } catch (SQLException e) { /* Ignored */}
        //     // }
        //     // if (ps != null) {
        //     //     try {
        //     //         ps.close();
        //     //     } catch (SQLException e) { /* Ignored */}
        //     // }
        //     if (conn != null) {
        //         try {
        //             conn.close();
        //             System.out.println("*************connection__closed");

        //         } catch (SQLException e) { /* Ignored */}
        //     }
        // }

    }

    
    
    // ################ Post events to URL --start ######################


        
    // HttpClient httpClient = HttpClientBuilder.create().build();
    // System.out.println("*************____11");

    // // HttpClient httpClient = HttpClients.createDefault();
    // HttpPost httpPost = new HttpPost("http://teletest.lc.cl-labs.springpeople.com/event");


    // System.out.println(eventPayload);
    // JSONObject jobj = new JSONObject(eventPayload);

    // // #StringEntity requestEntity = new StringEntity(new JsonObject(eventPayload).toString(),ContentType.APPLICATION_JSON);
    // StringEntity requestEntity = new StringEntity(jobj.toString(),ContentType.APPLICATION_JSON);
    // httpPost.setEntity(requestEntity);
    // try {
    //     HttpResponse response = httpClient.execute(httpPost);
    // } catch (IOException e) {
    //     e.printStackTrace();
    // }



    // ################ Post events to URL --end ######################


    last_save = System.currentTimeMillis();
    current_run = 0;

    

         
    }
    

    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    @Override
    public void increaseDuration(AnalyticsEvent event, Map<String, Object> properties) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onActivity() {
        // TODO Auto-generated method stub
        // System.out.println("**** >>>>>>>>>>>>>>> onActivity - START");
        // System.out.println(workspaceName);
        // System.out.println(workspaceStartingUserId);
        // System.out.println(getUserId());
        // System.out.println(System.currentTimeMillis());
        // System.out.println(lastEventTime);
        // System.out.println(inactiveTimeLimt);
        // System.out.println("**** >>>>>>>>>>>>>>> onActivity - END");

        if (System.currentTimeMillis() - lastEventTime >= inactiveTimeLimt) {
            onEvent(WORKSPACE_INACTIVE, userId, lastIp, lastUserAgent, lastResolution, commonProperties);
        }
    }



    private String getUserNameFromMachineTokenInternal(final String machineToken) {
    String username = null;
    if (machineToken != null && !machineToken.isEmpty()) {
      try {
        JwtParser jwtParser = Jwts.parser();
        String[] splitted = machineToken.split("\\.");
        if (splitted.length != 3) {
            System.out.println("Cannot retrieve user name from the machine token: invalid token");
        } else {
          Object userNameClaim = jwtParser.parseClaimsJwt(splitted[0] + "." + splitted[1] + ".").getBody()
              .get(USER_NAME_CLAIM);
          if (userNameClaim == null) {
            System.out.println("Cannot retrieve user Id ");

          } else {
            username = userNameClaim.toString();
          }
        }
      } catch (Exception e) {
            System.out.println("Cannot retrieve user Id from the machine token");

      }
    }
    return username;
  }
}