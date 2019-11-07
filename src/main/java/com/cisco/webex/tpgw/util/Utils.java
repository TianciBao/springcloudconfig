package com.cisco.webex.tpgw.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.util.LinkedMultiValueMap;

import com.cisco.webex.tpgw.service.entity.ResponseForTpgw;
import com.cisco.webex.tpgw.service.entity.TpgwRequest;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class Utils {

    public static ConcurrentHashMap<String ,List<ResponseForTpgw>> responseForTpgwMap =new ConcurrentHashMap<String ,List<ResponseForTpgw>>();
    public static ConcurrentHashMap<String ,ResponseForTpgw> responseForTpgwMapLast =new ConcurrentHashMap<String ,ResponseForTpgw>();

    public static ConcurrentHashMap<String ,List<String>> resultForSessionOpenResponse =new ConcurrentHashMap<String ,List<String>>();

    //public static ConcurrentHashMap TpgwRequestMap =new ConcurrentHashMap<String ,TpgwRequest>();
    
    //public static LinkedMultiValueMap<String ,TpgwRequest> TpgwRequestMap = new LinkedMultiValueMap<String ,TpgwRequest>();
    
 // 通过CacheBuilder构建一个缓存实例
    public static Cache<String, LinkedMultiValueMap<String ,TpgwRequest>> requestCache = CacheBuilder.newBuilder()
    				.initialCapacity(500)
                    .maximumSize(10000) // 设置缓存的最大容量
                    .expireAfterAccess(2, TimeUnit.DAYS) // 设置缓存在写入一分钟后失效
                    .concurrencyLevel(10) // 设置并发级别为10
                    .recordStats() // 开启缓存统计
                    .build();
    
    public static void addResponseForTpgwMap(String key, ResponseForTpgw responseForTpgwJson) {
    	List<ResponseForTpgw> responseForTpgws = new ArrayList<ResponseForTpgw>();
    	if ((List<ResponseForTpgw>) responseForTpgwMap.get(key) == null) {
    		responseForTpgws.add(responseForTpgwJson);
		} else {
			responseForTpgws = responseForTpgwMap.get(key);
			if (responseForTpgwJson.getParamMap().get("paramIsLatest").equals("true")) {
				responseForTpgws.clear();
			}
			responseForTpgws.add(responseForTpgwJson);
		}
    	
    	responseForTpgwMap.put(key, responseForTpgws);
    	responseForTpgwMapLast.put(key, responseForTpgwJson);
    }
    
    public static ResponseForTpgw getResponseForTpgwMap(String key) {
    	List<ResponseForTpgw> responseForTpgws = responseForTpgwMap.get(key);
    	ResponseForTpgw Value = new ResponseForTpgw();
    	if (responseForTpgws.size()>0) {
    		ResponseForTpgw responseForTpgwJson = responseForTpgws.get(0);
    		Value = responseForTpgwJson;
        	responseForTpgws.remove(0);
		} else {
			Value = responseForTpgwMapLast.get(key);
		}
    	
    	return Value;
    }
    
    public static Object jsonToObj(Object obj,String jsonStr) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();	
	    return obj = mapper.readValue(jsonStr, obj.getClass());
	}

	public static String objToJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);  
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
