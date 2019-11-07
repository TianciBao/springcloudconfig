package com.cisco.webex.tpgw.controller;

import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockJ2EEController {
    private Logger logger = Logger.getLogger(MockJ2EEController.class);

    private static Cache<String, JSONArray> cbPmrNotifyRequest = CacheBuilder.newBuilder().maximumSize(10000)
            .expireAfterWrite(300, TimeUnit.SECONDS).build(new CacheLoader<String, JSONArray>() {

                @Override
                public JSONArray load(String Key) throws Exception {
                    // TODO Auto-generated method stub
                    return null;
                }

            });

    /**
     * /mc3000/pmrnotify.do?siteurl=cet78&appName=APP_CB2&appToken=SDJBUAAAAAEtwCq/gTcLWRLZwDEu/v1vYeD55iga2kTRBrQDBlQXXwAAAWttOdig&xml=<PMRNotify><MessageType>attendeewaiting</MessageType><MeetingNum>1284835166</MeetingNum><AttendeeList><Attendee>ZVRlc3RfVXNlci0x</Attendee></AttendeeList><Total>1</Total></PMRNotify>
     */
    @PostMapping(value = "/pmrnotify/{meetingkey}")
    public @ResponseBody String handleRequestBearerToken(@PathVariable("meetingkey") String meetingkey,HttpServletRequest request,HttpServletResponse resp) {
        Enumeration keys = request.getParameterNames();
        JSONObject requestParameters = new JSONObject();
        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            String value = request.getParameter(key);
            requestParameters.put(key, value);
        }

        JSONArray existRequest = cbPmrNotifyRequest.getIfPresent(meetingkey);
        if(existRequest == null) {
            existRequest = new JSONArray();
            cbPmrNotifyRequest.put(meetingkey, existRequest);
        }
        existRequest.put(requestParameters);

        logger.info("Receive request from CB, meetingkey:" + meetingkey + ",parameters:" + requestParameters);
        return "OKOKOK";
    }

    @GetMapping(value = "/pmrnotifyta")
    public String getCbPmrNotifyByMeetingKey(@RequestParam("meetingKey") String meetingKey) {
        JSONArray res = cbPmrNotifyRequest.getIfPresent(meetingKey);
        if(res == null)
            res = new JSONArray();
        
        logger.info("Find data from cache, meetingkey:" + meetingKey + ",parameters:" + res);
        return res.toString();
    }
}