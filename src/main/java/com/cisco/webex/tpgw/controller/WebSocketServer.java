package com.cisco.webex.tpgw.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.cisco.webex.tpgw.service.entity.Message;
import com.cisco.webex.tpgw.util.Utils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;

@ServerEndpoint("/wbxsealion/websocket")
@Component
@Slf4j
public class WebSocketServer {
	
    public static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    public static Map<WebSocketServer, List<String>> webSocketMessagesMap = new HashMap<WebSocketServer, List<String>>();

    private Session session;
    private String sessionId = "";
    private String sid = "";
    private String sessionType = "";
    
    public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSessionType() {
		return sessionType;
	}

	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@OnOpen
    public void onOpen(Session session) {
        this.session = session;
        this.sessionId = session.getId();
        webSocketSet.add(this);
        webSocketMessagesMap.put(this, new ArrayList<String>());
        log.info("websocket connect success, sid = " + this.getSid() + ", sessionId:" + this.getSessionId());
    }

    
    @OnClose
    public void onClose() {
    	log.info("webSocketSet remove sid:" + this.getSid() + ", sessionId:" + this.getSessionId());
        webSocketSet.remove(this);  
    }

    /**
     * call back after receive client message
     *
     * @param message 
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException */
    @OnMessage
    public void onMessage(String message, Session session) throws JsonParseException, JsonMappingException, IOException {
        log.info("Receive message from "+session.getId() + " :" + message);
        for (WebSocketServer item : webSocketSet) {
        	if (item.getSessionId().equals(session.getId())) {
        		addMessageForWebSocket(item, message);
        		responseMessage(session, message);
			}
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Occur Error, session:" + session);
        error.printStackTrace();
    }
    /**
     * service send message
     */
    public void sendMessage(String message, boolean isInitiative){
    	if (isInitiative) {
    		log.info("MockSocket initiatively send message,  sid:" + sid + ", sessionId:" + getSessionId() + ", sessionType" + sessionType + ", message:" + message);
		} else {
    		log.info("MockSocket send message, sid:" + sid + ", sessionId:" + getSessionId() + ", sessionType" + sessionType + ", message:" + message);
		}
        try {
			this.session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			log.error("MockSocket send message fail, sid:" + sid + ", sessionId:" + getSessionId() + ", sessionType" + sessionType);;
			e.printStackTrace();
		}
        
    }
	
	public void responseMessage(Session session, String messageStr) throws JsonParseException, JsonMappingException, IOException {

		Message message = (Message) Utils.jsonToObj(new Message(), messageStr);
		Message returnMessage = new Message();
		if (message == null) {
			log.error("message is null");
			return;
		}
		
		returnMessage.setVersion(message.getVersion());
		returnMessage.setType(message.getType());
		returnMessage.setTrackingId(message.getTrackingId());
		returnMessage.getData().setSessionId(message.getData().getSessionId());
		
		if (message.getType().equals("SESSION") && message.getData().getType().equals("OPEN_REQUEST")) {
			//Open WSS Session Request
			this.sid = message.getData().getSessionId();
			this.setSessionType(message.getData().getSessionType());
			returnMessage.getData().setType("OPEN_RESPONSE");
	    	List<String> resultList = Utils.resultForSessionOpenResponse.get(sid + "+" + this.getSessionType());
	    	if (null == resultList || resultList.isEmpty()) {
	    		returnMessage.getData().setResult("OK");
			}else {
				returnMessage.getData().setResult(resultList.get(0));
				resultList.remove(0);
				Utils.resultForSessionOpenResponse.put(sid + "+" + this.getSessionType(), resultList);
			}
	    	log.info("rececive OPEN_REQUEST: sid = " + this.sid + ", sessionId:" + getSessionId() + ", sessionType:" + sessionType);
		} else if (message.getType().equals("SESSION") && message.getData().getType().equals("CLOSE")) {
			//Close WSS Session
			log.warn("rececive CLOSE: sid = " + this.sid + ", sessionId:" + getSessionId());
			session.close();
			this.onClose();
		} else if (message.getType().equals("SUBSCRIBE")) {
			//CB Subscribe Data
	    	log.info("rececive SUBSCRIBE: sid = " + this.sid + ", sessionId:" + getSessionId() + ", sessionType:" + sessionType);
			returnMessage.setType("NOTIFY");
			returnMessage.getData().setData("default");
			try {
				Thread.sleep(5*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return;
		}
		
		sendInfo(Utils.objToJson(returnMessage), this.getSid(), this.getSessionType(), false);
		
		
	}
	
    public static void sendInfo(String message, String sid, String sessiontype, boolean isInitiative){
    	
        for (WebSocketServer item : WebSocketServer.webSocketSet) {
            if(sid==null) {
                item.sendMessage(message, isInitiative);
            }else if(item.getSid().equals(sid) && item.getSessionType().equals(sessiontype)){
                item.sendMessage(message, isInitiative);
            }
        }
    }
    
    public void addMessageForWebSocket(WebSocketServer socketServer, String message) {
    	List<String> messages = webSocketMessagesMap.get(socketServer);
    	messages.add(message);
    	webSocketMessagesMap.put(socketServer, messages);
    	
	}
    
  

}