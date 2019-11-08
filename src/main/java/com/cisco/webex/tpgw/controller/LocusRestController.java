package com.cisco.webex.tpgw.controller;

import static com.cisco.webex.tpgw.util.ControllerMappingConst.API_GET_PICTURE_SIMILARITY;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_API_VI_ACTION_EXPECTED_All_RESPONE_QUERY;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_API_VI_ACTION_EXPECTED_RESPONE_QUERY;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_API_VI_EXPECTED_All_RESPONE_QUERY;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_API_VI_EXPECTED_RESPONE;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_API_VI_EXPECTED_RESPONE_REMOVE;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_API_VI_JOIN_REQUEST;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_API_VI_KEEPLIVE_REQUEST;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_API_VI_LEAVE_REQUEST;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_API_VI_MEETING_CONTROLS_REQUEST;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_API_VI_PARTICIPANT_CONTROLS_REQUEST;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_API_VI_UPDATE_REQUEST;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_Avatar;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_Delta_EVENT_Sync;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_End_Meeting;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_FULL_DTO;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_HEALTH_CHECK;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_ID;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_Media_Share_Floor;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_Query_Meeting_Status;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_VIDEO_CALLBACK;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_WebexUnsupportedFeature;
import static com.cisco.webex.tpgw.util.ControllerMappingConst.Locus_Webex_Host_Joined;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cisco.webex.tpgw.service.LocusService;
import com.cisco.webex.tpgw.service.entity.ResponseForTpgw;
import com.cisco.webex.tpgw.service.entity.TpgwRequest;
import com.cisco.webex.tpgw.util.Utils;

@RestController
public class LocusRestController {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired  
    private HttpServletRequest request;  
	
	@Autowired
	private LocusService locusService;
	
	@GetMapping(value = Locus_HEALTH_CHECK)
	public ResponseEntity GetHealthCheck() {
		return new ResponseEntity("SUCCESS", HttpStatus.valueOf(200));
	}
	
    @PostMapping(value = Locus_API_VI_EXPECTED_RESPONE)
    public ResponseEntity addOneMockRecord(@RequestHeader(value="action") String action, @RequestHeader(value="locusid") String locusid, @RequestBody(required=false) ResponseForTpgw responseForTpgw) {
        logger.info("Set_Expected_ResponseForTpgw, Action:" + action + ", locusid:" + locusid + ", responseForTpgw:" + Utils.objToJson(responseForTpgw));
        Utils.addResponseForTpgwMap(locusid + "+" + action, responseForTpgw);
        return new ResponseEntity("SUCCESS", HttpStatus.valueOf(200));

    }
    
    @GetMapping(value = Locus_FULL_DTO)
    public ResponseEntity ResponseGetLocusFullDto4Action(@PathVariable("locusid")String locusid) {
    	String action = "GETFULLLOCUS";
        logger.info("Tpgw/CB request action: " + action + ", locusid: " + locusid + ", put to map");
        Map<String, String> paramMap = new HashMap<>();
    	
        locusService.saveTpgwRequest(locusid, action, request, "", paramMap);
        return locusService.getResponseEntityForTpgw(locusid + "+" + action);
    }
    
    @GetMapping(value = Locus_Query_Meeting_Status)
    public ResponseEntity ResponseQueryMeetingStatus4Action(@PathVariable("meetingkey") String meetingkey, @PathVariable("sitename") String sitename) {
    	String action = "Query_Meeting_Status";
        logger.info("Tpgw/CB request action: " + action + ", locusid: " + meetingkey + ", sitename: " + sitename + ", put to map");
        Map<String, String> paramMap = new HashMap<>();
    	
        locusService.saveTpgwRequest(meetingkey, action, request, "", paramMap);
        return locusService.getResponseEntityForTpgw(meetingkey + "+" + action);
    }
    
    @RequestMapping(value = Locus_API_VI_MEETING_CONTROLS_REQUEST, method= {RequestMethod.PATCH,RequestMethod.PUT})
    public ResponseEntity ResponseRecord4Mute(@PathVariable("locusid")String locusid, @RequestBody String locusRes) {
    	String action = "MEETING_CONTROLS";
        logger.info("Tpgw/CB request action: " + action + ", locusid: " + locusid + ", put to map");
        Map<String, String> paramMap = new HashMap<>();
    	
        locusService.saveTpgwRequest(locusid, action, request, locusRes, paramMap);
    	
        return locusService.getResponseEntityForTpgw(locusid + "+" + action);
    }
  
    @RequestMapping(value = Locus_API_VI_PARTICIPANT_CONTROLS_REQUEST, method= {RequestMethod.PATCH,RequestMethod.PUT})
    public ResponseEntity ResponseRecord4AssignHost(@PathVariable("locusid")String locusid,@PathVariable("participantId")String participantId, @RequestBody String locusRes) {
    	String action = "PARTICIPANT_CONTROLS";
    	Map<String, String> paramMap = new HashMap<>();
    	
    	paramMap.put("participantId", participantId);
        logger.info("Tpgw/CB request action: " + action + ", locusid: " + locusid + ", participantId:" + participantId + ",body:" + locusRes + ", put to map");
        locusService.saveTpgwRequest(locusid, action, request, locusRes, paramMap);
        return locusService.getResponseEntityForTpgw(locusid + "+" + action);
    }

    @PutMapping(value = Locus_API_VI_LEAVE_REQUEST)
    public ResponseEntity ResponseRecord4LEAVE(@PathVariable("locusid")String locusid, @PathVariable("participantId")String participantId, @RequestBody String locusRes) {
        String action = "LEAVE";
        logger.info("Tpgw/CB request action: " + action + " , locusid: " + locusid + " , put to map");
        Map<String, String> paramMap = new HashMap<>();
    	
    	paramMap.put("participantId", participantId);

    	logger.info("Tpgw/CB request action: " + action + ", locusid: " + locusid + ", participantId:" + participantId + ", put to map");
        locusService.saveTpgwRequest(locusid, action, request, locusRes, paramMap);
        return locusService.getResponseEntityForTpgw(locusid + "+" + action);
    }
    
    @PutMapping(value = Locus_Media_Share_Floor)
    public ResponseEntity Locus_Media_Share_Floor(@PathVariable("locusid")String locusid, @PathVariable("mediaShare_id")String mediaShare_id, @RequestBody String locusRes) {
        String action = "Media_Share_Floor";
        logger.info("Tpgw/CB request action: " + action + " , locusid: " + locusid + " , put to map");
        Map<String, String> paramMap = new HashMap<>();
    	
    
    	logger.info("Tpgw/CB request action: " + action + ", locusid: " + locusid + ", put to map");
        locusService.saveTpgwRequest(locusid, action, request, locusRes, paramMap);
        return locusService.getResponseEntityForTpgw(locusid + "+" + action);
    }

    @PostMapping(value = Locus_API_VI_JOIN_REQUEST)
    public ResponseEntity ResponseRecord4Join(@PathVariable("locusid")String locusid, @RequestBody String locusRes ) {
        String action = "JOIN";
        logger.info("Tpgw/CB request action: " + action + ", locusid: " + locusid + ", put to map");
        Map<String, String> paramMap = new HashMap<>();
    	
        locusService.saveTpgwRequest(locusid, action, request, locusRes, paramMap);

        return locusService.getResponseEntityForTpgw(locusid + "+" + action);
    }
    
    @PostMapping(value = Locus_End_Meeting)
    public ResponseEntity Locus_End_Meeting(@PathVariable("locusid")String locusid, @RequestBody(required=false) String locusRes ) {
        String action = "END";
        logger.info("Tpgw/CB request action: " + action + ", locusid: " + locusid + ", put to map");
        Map<String, String> paramMap = new HashMap<>();
    	
        locusService.saveTpgwRequest(locusid, action, request, locusRes, paramMap);

        return locusService.getResponseEntityForTpgw(locusid + "+" + action);
    }
    
    @PostMapping(value = Locus_Webex_Host_Joined)
    public ResponseEntity Locus_Webex_Host_Joined(@PathVariable("locusid")String locusid, @RequestBody String locusRes ) {
        String action = "WEBEX_HOST_JOINED";
        logger.info("Tpgw/CB request action: " + action + ", locusid: " + locusid + ", put to map");
        Map<String, String> paramMap = new HashMap<>();
    	
        locusService.saveTpgwRequest(locusid, action, request, locusRes, paramMap);

        return locusService.getResponseEntityForTpgw(locusid + "+" + action);
    }
    
    @PostMapping(value = Locus_WebexUnsupportedFeature)
    public ResponseEntity Locus_WebexUnsupportedFeature(@PathVariable("locusid")String locusid, @RequestBody String locusRes ) {
        String action = "WebexUnsupportedFeature";
        logger.info("Tpgw/CB request action: " + action + ", locusid: " + locusid + ", put to map");
        Map<String, String> paramMap = new HashMap<>();
    	
        locusService.saveTpgwRequest(locusid, action, request, locusRes, paramMap);

        return locusService.getResponseEntityForTpgw(locusid + "+" + action);
    }
    
    @PostMapping(value = Locus_VIDEO_CALLBACK)
    public ResponseEntity ResponseRecord4VideoCallback(@PathVariable("locusid")String locusid, @PathVariable("participantId")String participantId, @RequestBody String locusRes ) {
        String action = "VIDEOCALLBACK";
        Map<String, String> paramMap = new HashMap<>();
    	
    	paramMap.put("participantId", participantId);
        logger.info("Tpgw/CB request action: " + action + " , locusid: " + locusid  + " , participantId: " + participantId + " , put to map");
        
        locusService.saveTpgwRequest(locusid, action, request, locusRes, paramMap);
        return locusService.getResponseEntityForTpgw(locusid + "+" + action);
    }

    
    @PutMapping(value = Locus_API_VI_UPDATE_REQUEST)
    public ResponseEntity ResponseRecord4Update(@PathVariable("locusid")String locusid,@PathVariable("participantId")String participantId, @RequestBody String locusRes) {
        String action = "UPDATE";
        Map<String, String> paramMap = new HashMap<>();
    	
    	paramMap.put("participantId", participantId);
        logger.info("Tpgw/CB request action: " + action + " , locusid: " + locusid  + " , participantId: " + participantId + " , put to map");
        logger.info("update: " + locusRes);

        locusService.saveTpgwRequest(locusid, action, request, locusRes, paramMap);
        return locusService.getResponseEntityForTpgw(locusid + "+" + action);
    }

    @PutMapping(value = Locus_API_VI_KEEPLIVE_REQUEST)
    public ResponseEntity ResponseRecord4KeepLive(@PathVariable("locusid")String locusid, @RequestBody String locusRes) {
    	String agent = request.getHeader("User-Agent");
        String action = "KEEPALIVE";
        logger.info("Tpgw/CB request action: " + action + " , locusid: " + locusid + " ,User-Agent: "+ agent +" put to map");
        Map<String, String> paramMap = new HashMap<>();
    	

        locusService.saveTpgwRequest(locusid, action, request, locusRes, paramMap);
        return locusService.getResponseEntityForTpgw(locusid + "+" + action);
    }
    
    @GetMapping(value = Locus_API_VI_ACTION_EXPECTED_RESPONE_QUERY)
    public ResponseEntity queryDTORequestRecordByAction(@PathVariable("locusid") String locusid, @PathVariable("action") String action) {
    	List<TpgwRequest> tpgwRequestList = new ArrayList<TpgwRequest>();
    	LinkedMultiValueMap<String ,TpgwRequest> map = Utils.requestCache.getIfPresent(locusid);
    	if (map == null) {
    		return new ResponseEntity(" not find TpgwRequest Record", HttpStatus.valueOf(404));
		}
    	Set<String> keySet = map.keySet();
    	for (String key : keySet) {
			if (key.equals(action)) {
				tpgwRequestList = map.get(action);
			}
		}
    	
    	if (tpgwRequestList != null && tpgwRequestList.size()>0) {
    		TpgwRequest tpgwRequest = tpgwRequestList.get(tpgwRequestList.size()-1);
    		logger.info("queryDTORequestRecord action: " + action + " , locusid: " + locusid + " , body:" + tpgwRequest);
            return new ResponseEntity(tpgwRequest, HttpStatus.valueOf(200));
		} else {
			return new ResponseEntity(" not find TpgwRequest Record", HttpStatus.valueOf(404));
		}
    }
    
    @GetMapping(value = Locus_API_VI_ACTION_EXPECTED_All_RESPONE_QUERY)
    public ResponseEntity queryAllDTORequestRecordByAction(@PathVariable("locusid") String locusid, @PathVariable("action") String action) {
    	List<TpgwRequest> tpgwRequestList = new ArrayList<TpgwRequest>();
    	LinkedMultiValueMap<String ,TpgwRequest> map = Utils.requestCache.getIfPresent(locusid);
    	if (map == null) {
    		return new ResponseEntity(" not find TpgwRequest Record", HttpStatus.valueOf(404));
		}
    	Set<String> keySet = map.keySet();
    	for (String key : keySet) {
			if (key.equals(action)) {
				tpgwRequestList = map.get(action);
			}
		}
        
        if (null != tpgwRequestList) {
        	logger.info("query all DTORequestRecord action: " + action + " , locusid: " + locusid + " , body:" + tpgwRequestList);
            return new ResponseEntity(tpgwRequestList, HttpStatus.valueOf(200));
        } else {
            return new ResponseEntity(" not find TpgwRequest Record", HttpStatus.valueOf(404));
        }
    }
    
    @GetMapping(value = Locus_API_VI_EXPECTED_All_RESPONE_QUERY)
    public ResponseEntity queryAllDTORequestRecord(@PathVariable("locusid") String locusid) {
    	List<TpgwRequest> tpgwRequestList = new ArrayList<>();
    	LinkedMultiValueMap<String ,TpgwRequest> map = Utils.requestCache.getIfPresent(locusid);
    	if (map == null) {
    		return new ResponseEntity(" not find TpgwRequest Record", HttpStatus.valueOf(404));
		}
    	Set<String> keySet = map.keySet();
    	for (String key : keySet) {
			tpgwRequestList.addAll(map.get(key));
		}
        if (null != tpgwRequestList && !tpgwRequestList.isEmpty()) {
        	logger.info("query all DTORequestRecord, locusid: " + locusid + " , body:" + tpgwRequestList);
            return new ResponseEntity(tpgwRequestList, HttpStatus.valueOf(200));
        } else {
            return new ResponseEntity("queryall locusid: " + locusid + " not find TpgwRequest Record", HttpStatus.valueOf(404));
        }
    }

    @GetMapping(value = Locus_API_VI_EXPECTED_RESPONE_REMOVE)
    public ResponseEntity removeDTOrequestRecord(@PathVariable("locusid") String locusid, @PathVariable("action") String action) {
    	LinkedMultiValueMap<String ,TpgwRequest> map = Utils.requestCache.getIfPresent(locusid);
    	Set<String> keySet = map.keySet();
    	for (String key : keySet) {
    		if (key.equals(action)) {
				map.remove(action);
			}
		}
        return new ResponseEntity(" remove locusid: " + locusid + " , action: " + action, HttpStatus.valueOf(200));
    }

    @GetMapping(value = API_GET_PICTURE_SIMILARITY)
    public ResponseEntity getPictureSimilarity(@PathVariable("pictureId") String pictureId) {
    	logger.info("begin get picture similarity...");
        String[] args1=new String[]{"python","C:\\Users\\tiabao\\Documents\\webex-test-tools\\NewLocus\\src\\main\\java\\com\\cisco\\webex\\tpgw\\tool\\histsimilar.py",pictureId};
        String line;
		String body = "";
        try {
        	//调用服务器命令脚本
			Process pr=Runtime.getRuntime().exec(args1);
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			
			while ((line = in.readLine()) != null) {
				logger.info(line);
				body = body + line;
			}
			in.close();
			pr.waitFor();
			logger.info("end get picture similarity...");
			return new ResponseEntity(body, HttpStatus.valueOf(200));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        body = "Fail to get picture similarit,Please contact Tianci to check";
		return new ResponseEntity(body, HttpStatus.valueOf(500));
    }
    
    @GetMapping(value = Locus_ID)
    public ResponseEntity getLocusID(@PathVariable("meetingkey") String meetingkey, @PathVariable("sitename") String sitename) {
    	String action = "GETLOCUSID";
    	logger.info("Get Locus ID， meetingkey= " + meetingkey + ", sitename=" + sitename);
		return locusService.getResponseEntityForTpgw(meetingkey + "+" + action);
    	//return new ResponseEntity(response, HttpStatus.valueOf(200));
    	
    }
    
    @GetMapping(value = Locus_Delta_EVENT_Sync)
    public ResponseEntity getDelta_EVENT_Sync(@PathVariable("locusid") String locusid) {
    	String action = "EVENTSYNC";
        logger.info("Tpgw/CB request action: " + action + " , locusid: " + locusid + " put to map");
        return locusService.getResponseEntityForTpgw(locusid + "+" + action);
    	
    }
    
    @PostMapping(value = Locus_Avatar)
    public ResponseEntity getAvatar(@PathVariable("locusid")String locusid, @RequestBody String locusRes ) {
        String action = "AVATAR";
        logger.info("Tpgw/CB request action: " + action + " , locusid: " + locusid  + " , put to map");
        Map<String, String> paramMap = new HashMap<>();
    	
        locusService.saveTpgwRequest(locusid, action, request, locusRes, paramMap);
        List<UUID> uuids = new ArrayList<UUID>();		
		String jsonObjectString = "{";
        for(int i=0;i<=locusRes.length()-1;i++) {  
            String getstr=locusRes.substring(i,i+1);  
            if(getstr.equals("{")){  
            	int index = locusRes.indexOf("}", i);
        		
        		jsonObjectString+=locusRes.substring(i+8, index)+":{\"80\": {"
					      +"\"url\": \"https://1efa7a94ed216783e352-c62266528714497a17239ececf39e9e2.ssl.cf1.rackcdn.com/V1~e8b08dc85e896901fd4416b7171f3c43~5Hth5YCQRTGx9tYEiDQGPA==~80\","
					      +"\"size\": 80,\"defaultAvatar\": ";
				if(locusRes.substring(i+9, index-1).equals("ffff-ffff-ffff-ffff")){
					jsonObjectString+="true}}},";
				}else{
					jsonObjectString+="false}}},";
				}
				break;
            } 
            
        }
        jsonObjectString = jsonObjectString.substring(0,jsonObjectString.length()-1);
        logger.info("locusid:" + locusid + ", action:" + action + ", responseBody: "+ jsonObjectString);
        return new ResponseEntity(jsonObjectString, HttpStatus.valueOf(200));
    }
    
    public static void main(String[] args) throws IOException {
       Integer xx =  909588537;
       System.out.println(xx.byteValue());
   }

}
