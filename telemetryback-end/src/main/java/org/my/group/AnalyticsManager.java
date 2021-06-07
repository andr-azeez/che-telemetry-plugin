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
        HttpClient httpClient = HttpClientBuilder.create().build();
        // HttpClient httpClient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost("http://teletest.lc.cl-labs.springpeople.com/event");
    HashMap<String, Object> eventPayload = new HashMap<String, Object>(properties);
    eventPayload.put("event", event);
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