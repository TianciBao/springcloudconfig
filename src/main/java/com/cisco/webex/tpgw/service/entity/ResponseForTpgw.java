package com.cisco.webex.tpgw.service.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.Header;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


public class ResponseForTpgw {
	
	private Map<String, String> paramMap = new HashMap<String, String>();
	private String code;
	private Set<Header> headers;
	private String body;
	
	public Set<Header> getHeaders() {
		return headers;
	}
	public void setHeaders(Set<Header> headers) {
		this.headers = headers;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Map<String, String> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public MultiValueMap<String, String>  generaterMapResponseHeaders() {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    	for (Header header : this.getHeaders()) {
    		headers.add(header.getName(), header.getValue());
		}
    	return headers;
	}
	
}
