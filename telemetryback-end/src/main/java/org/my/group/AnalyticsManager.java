package org.my.group;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import org.apache.http.entity.StringEntity;

import  org.apache.http.entity.ContentType;

// import org.json.JSONObject;
import org.json.simple.JSONObject;

import org.eclipse.che.api.core.rest.HttpJsonRequestFactory;
// import org.apache.httpcomponents;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.HttpClient;



// import org.webjars.bower.HashMap;

// import org.apache.http.client.HttpPost;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;

// import org.apache.httpcomponents.client.httpclient;
import org.eclipse.che.incubator.workspace.telemetry.base.AbstractAnalyticsManager;
import org.eclipse.che.incubator.workspace.telemetry.base.AnalyticsEvent;
// import org.eclipse.che.incubator.workspace.telemetry.base.AnalyticsEvent.WORKSPACE_STOPPED;
import static org.eclipse.che.incubator.workspace.telemetry.base.AnalyticsEvent.*;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class AnalyticsManager extends AbstractAnalyticsManager {

    private long inactiveTimeLimt = 60000 * 3;

    public AnalyticsManager(String apiEndpoint, String workspaceId, String machineToken,
            HttpJsonRequestFactory requestFactory) {
        super(apiEndpoint, workspaceId, machineToken, requestFactory);
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void destroy() {
        System.out.println("*****Hello");
        System.out.println(workspaceName);
        System.out.println(workspaceStartingUserId);
        System.out.println(getUserId());
        onEvent(WORKSPACE_STOPPED, userId, lastIp, lastUserAgent, lastResolution, commonProperties);
        // TODO Auto-generated method stub
    }

    @Override
    public void onEvent(AnalyticsEvent event, String ownerId, String ip, String userAgent, String resolution,
            Map<String, Object> properties) {
        // TODO Auto-generated method stub
        System.out.println("*************");
        System.out.println(ownerId);
        System.out.println("*************____start");
        System.out.println("*************____33");
        Connection conn = null;


        HashMap<String, Object> eventPayload = new HashMap<String, Object>(properties);
        System.out.println("*************____33");

        eventPayload.put("event", event);
        System.out.println(event);
        System.out.println("*************____44");
        System.out.println(eventPayload);
        System.out.println("*************____44_2");

        System.out.println(properties);
        JSONObject jsonmsg = new JSONObject(properties);
        System.out.println("*************____44_3");

        System.out.println(ip);
        System.out.println(userAgent);
        System.out.println(resolution);

        System.out.println(jsonmsg);

        String message;
        JSONObject json = new JSONObject();
        json.put("name", "student");

        JSONObject item = new JSONObject();
        item.put("information", "test");
        item.put("id", 3);
        item.put("name", "course1");


        message = json.toString();
        System.out.println("*************____44_4");


        System.out.println(message);
        System.out.println(properties.toString());




        

        try{

            conn = DriverManager.getConnection(
                "jdbc:postgresql://13.233.70.170:5432/che_telemetry","telemetry_user","SpringTelemetry123");
            
            Statement stmt = conn.createStatement();
            if (conn != null) {

                String sql = "insert into che_logs(log_type,log_data,user_id) values('"+event+"','"+jsonmsg+"','"+ownerId+"')";
                // sql = "INSERT INTO che_logs VALUES (101, 'Mahnaz', 'Fatma', 25)";
         stmt.executeUpdate(sql);
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
    // if (rs != null) {
    //     try {
    //         rs.close();
    //     } catch (SQLException e) { /* Ignored */}
    // }
    // if (ps != null) {
    //     try {
    //         ps.close();
    //     } catch (SQLException e) { /* Ignored */}
    // }
    if (conn != null) {
        try {
            conn.close();
            System.out.println("*************connection__closed");

        } catch (SQLException e) { /* Ignored */}
    }
}


        
        
        
        HttpClient httpClient = HttpClientBuilder.create().build();
        System.out.println("*************____11");

        // HttpClient httpClient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost("http://teletest.lc.cl-labs.springpeople.com/event");
        System.out.println("*************____22");

    

    System.out.println("-0-0-0-0-0-0-0-0");

    System.out.println(eventPayload);
    JSONObject jobj = new JSONObject(eventPayload);
    System.out.println("*******9999999");

    // System.out.println(new StringEntity(jobj.toString(),ContentType.APPLICATION_JSON));

    // System.out.println(new JsonObject(eventPayload).toString(),ContentType.APPLICATION_JSON);
    

    // StringEntity requestEntity = new StringEntity(new JsonObject(eventPayload).toString(),ContentType.APPLICATION_JSON);
    StringEntity requestEntity = new StringEntity(jobj.toString(),ContentType.APPLICATION_JSON);
    httpPost.setEntity(requestEntity);
    try {
        HttpResponse response = httpClient.execute(httpPost);
    } catch (IOException e) {
        e.printStackTrace();
    }
         
    }

    @Override
    public void increaseDuration(AnalyticsEvent event, Map<String, Object> properties) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onActivity() {
        // TODO Auto-generated method stub
        if (System.currentTimeMillis() - lastEventTime >= inactiveTimeLimt) {
            onEvent(WORKSPACE_INACTIVE, userId, lastIp, lastUserAgent, lastResolution, commonProperties);
        }
    }
}