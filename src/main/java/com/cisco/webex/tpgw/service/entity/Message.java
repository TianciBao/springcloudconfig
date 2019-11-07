package com.cisco.webex.tpgw.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**  
*  
* @author Tianci  
* @date 2019-08-20 16:15:27
*/

@JsonIgnoreProperties(ignoreUnknown=true)
public class Message {
	private String version;
	private String type;
	private String trackingId;
	private Data data = new Data();
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTrackingId() {
		return trackingId;
	}
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	public class Data {
		private Integer siteId;
		private String conferenceId;
		private String sessionType;
		private String sessionId;
		private String type;
		private String result;
		private String reason;
		private String data;
		
		public Integer getSiteId() {
			return siteId;
		}
		public void setSiteId(Integer siteId) {
			this.siteId = siteId;
		}
		public String getConferenceId() {
			return conferenceId;
		}
		public void setConferenceId(String conferenceId) {
			this.conferenceId = conferenceId;
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
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		public String getData() {
			return data;
		}
		public void setData(String data) {
			this.data = data;
		}
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}

	}
}
