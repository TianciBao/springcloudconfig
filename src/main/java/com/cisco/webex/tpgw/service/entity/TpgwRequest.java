package com.cisco.webex.tpgw.service.entity;

import java.util.Date;
import java.util.Map;

import org.springframework.util.MultiValueMap;

public class TpgwRequest {
	
	Date requestTime = new Date();
	Map<String, String> paramMap;
    MultiValueMap<String, String> headers;
    String body;
    
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public MultiValueMap<String, String> getHeaders() {
		return headers;
	}
	public TpgwRequest setHeaders(MultiValueMap<String, String> headers) {
		this.headers = headers;
		return this;
	}
	public String getBody() {
		return body;
	}
	public TpgwRequest setBody(String body) {
		this.body = body;
		return this;
	}
	public Map<String, String> getParamMap() {
		return paramMap;
	}
	public TpgwRequest setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
		return this;
	}
    
}
