package com.cisco.webex.tpgw.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cisco.webex.tpgw.util.Utils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CheckCenterController {
   
    @RequestMapping("/socket/push/{sid}/{sessiontype}/{count}")
    public ResponseEntity pushToWeb(@PathVariable String sid, @PathVariable String sessiontype, @PathVariable Integer count, @RequestBody(required=true) String message) {
        for (int i = 0; i < count; i++) {
        	WebSocketServer.sendInfo(message, sid, sessiontype, true);
		}
        
		return new ResponseEntity("SUCCESS", HttpStatus.valueOf(200));
    }
    
    @RequestMapping("/socket/Session/Response/{sid}/{sessiontype}/{count}")
    public ResponseEntity prepaResponseResult(@PathVariable String sid, @PathVariable String sessiontype, @PathVariable int count, @RequestBody(required=true) String result) {
    	log.info("add response reuslt: sid:" + sid + ", sessiontype:" + sessiontype + ", count:" + count + ", result:" + result );
    	List<String> resultList = Utils.resultForSessionOpenResponse.get(sid + "+" + sessiontype);
    	if (resultList == null) {
    		resultList = new ArrayList<String>();
		}
    	for (int i = 0; i < count; i++) {
    		resultList.add(result);
		}
        Utils.resultForSessionOpenResponse.put(sid + "+" + sessiontype, resultList);
        
		return new ResponseEntity("SUCCESS", HttpStatus.valueOf(200));
    }
    
    @GetMapping("/socket/Session/request/{sid}/{sessiontype}")
    public ResponseEntity getMessageHistory(@PathVariable String sid, @PathVariable String sessiontype) {
    	List<String> messageList = new ArrayList<String>();
    	for (WebSocketServer webSocketServer : WebSocketServer.webSocketMessagesMap.keySet()) {
    		if(webSocketServer.getSid().equals(sid) && webSocketServer.getSessionType().equals(sessiontype)){
    			messageList = WebSocketServer.webSocketMessagesMap.get(webSocketServer);
    			break;
    		}
		}
    	return new ResponseEntity(messageList, HttpStatus.valueOf(200));
    }
}