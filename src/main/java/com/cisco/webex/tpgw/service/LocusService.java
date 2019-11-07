package com.cisco.webex.tpgw.service;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.cisco.webex.tpgw.service.entity.ResponseForTpgw;
import com.cisco.webex.tpgw.service.entity.TpgwRequest;
import com.cisco.webex.tpgw.util.Utils;

/**  
*  
* @author Tianci  
* @date 2018-09-14 17:16:04
*/
@Component
public class LocusService {
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static String Locus_Server_Dns = "newtamocklocus.qa.webex.com";

	public static void setLocusDns(String locusDns) {
		Locus_Server_Dns = locusDns;
	}
	
	public void saveTpgwRequest(String locusid, String action, HttpServletRequest request, String locusRes, Map<String, String> paramMap) {
		
    	MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			String value = request.getHeader(key);
			headers.add(key, value);
		}

		TpgwRequest tpgwrequest = new TpgwRequest();
		paramMap.put("action", action);
		paramMap.put("beUsed", "0");
		paramMap.put("url", request.getRequestURI());
		tpgwrequest.setParamMap(paramMap).setHeaders(headers).setBody(locusRes);
		
		LinkedMultiValueMap<String ,TpgwRequest> map = Utils.requestCache.getIfPresent(locusid);
		if (map == null) {
			map = new LinkedMultiValueMap<String, TpgwRequest>();
		}
		map.add(action, tpgwrequest);
        Utils.requestCache.put(locusid, map);
	}

	public ResponseEntity getResponseEntityForTpgw(String locusidAndAction) {
	    String statusCode = "200";
	    String body = "";
	    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
	
	    if (null != locusidAndAction && Utils.responseForTpgwMap.containsKey(locusidAndAction)) {
	    	ResponseForTpgw responseForTpgw = Utils.getResponseForTpgwMap(locusidAndAction);
	        statusCode = responseForTpgw.getCode();
	        body = responseForTpgw.getBody();
	        headers = responseForTpgw.generaterMapResponseHeaders();
	        logger.info("Tpgw/CB call find with the key " + locusidAndAction + ",locus response code:" + statusCode + ", headers:" + headers.toString() + ", body:" + body);
	        
	        String sleepTime = responseForTpgw.getParamMap().get("paramSleepTime");
	        if (sleepTime != null) {
				try {
					Thread.sleep(1000 * Integer.valueOf(sleepTime));
					logger.info("The key " + locusidAndAction + "; wait:" + sleepTime + "s, before return response");
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        
	        
	    } else {
	    	if (null != locusidAndAction && locusidAndAction.split("\\+")[1].equals("GETLOCUSID")) {
	    		body = "{\"locusUrl\":\"https://" + Locus_Server_Dns + "/locus/api/v1/loci/"+ locusidAndAction.split("\\+")[0] +"\",\"webExMeetingLink\":\"https://go.webex.com/meet/elton\",\"sipMeetingUri\":\"elton@go.webex.com\",\"callInNumbersInfo\":{\"callInTollNumber\":{\"number\":\"+1-415-655-0000\",\"tollfree\":false},\"callInTollFreeNumber\":{\"number\":\"1-877-668-4488\",\"tollfree\":true},\"globalCallInNumberUrl\":\"https://go.webex.com/go/globalcallin.php?serviceType=MC&eventID=171003257&tollFree=1\"},\"meetingNumber\":\"342504561\",\"owner\":\"3269f390-b389-456e-920e-1ecd9b0a2bb0\",\"isPmr\":true,\"localDialInNumber\":{\"number\":\"+1-415-655-0000\",\"tollfree\":false},\"numericCode\":\"342504561\",\"uri\":\"elton@go.webex.com\"}";
	    		logger.info("Tpgw/CB call find with the key " + locusidAndAction + ",locus response code:" + statusCode + ", headers:" + headers.toString() + ", body:" + body);
		        
	    	} else {
				logger.info("Tpgw/CB call not find with the key " + locusidAndAction);
			}
	        
	    }
	    return new ResponseEntity(body, headers, HttpStatus.valueOf(Integer.parseInt(statusCode)));
	}

}
