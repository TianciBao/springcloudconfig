package com.cisco.webex.tpgw.util;

public class ControllerMappingConst {
   
    public static final String Locus_API_VI_EXPECTED_RESPONE="/locus/api/v1/loci/expected_resource";
   // public static  final String Locus_API_VI_ASSIGN_HOSTS_REQUEST ="/locus/api/v1/loci/{confid}/participant/{id}/controls";
    public static final String Locus_API_VI_PARTICIPANT_CONTROLS_REQUEST ="/locus/api/v1/loci/{locusid}/participant/{participantId}/controls";
    
    public static final String Locus_API_VI_MEETING_CONTROLS_REQUEST ="/locus/api/v1/loci/{locusid}/controls";
    // leave  https://locus-intb.ciscospark.com/locus/api/v1/loci/6223c653-837f-3e12-85b6-37a5d3ed9f66/participant/7e74cd2b-114a-3e60-b753-cc84d23a0fc6/leave
    public static final String Locus_API_VI_LEAVE_REQUEST ="/locus/api/v1/loci/{locusid}/participant/{participantId}/leave";

    // join  https://locus-intb.ciscospark.com/locus/api/v1/loci/6223c653-837f-3e12-85b6-37a5d3ed9f66/participant
    public static final String Locus_API_VI_JOIN_REQUEST ="/locus/api/v1/loci/{locusid}/participant";
    
    // update  https://locus-intb.ciscospark.com/locus/api/v1/loci/6223c653-837f-3e12-85b6-37a5d3ed9f66/participant
    public static final String Locus_API_VI_UPDATE_REQUEST ="/locus/api/v1/loci/{locusid}/participant/{participantId}";

    //https://locus-intb.ciscospark.com/locus/api/v1/loci/6223c653-837f-3e12-85b6-37a5d3ed9f66/keepAlive
    public static final String Locus_API_VI_KEEPLIVE_REQUEST ="/locus/api/v1/loci/{locusid}/keepAlive";

    public static final String Locus_API_VI_ACTION_EXPECTED_RESPONE_QUERY="/locus/api/v1/loci/expected_resource/query/{locusid}/{action}";
    
    public static final String Locus_API_VI_ACTION_EXPECTED_All_RESPONE_QUERY="/locus/api/v1/loci/expected_resource/query/all/{locusid}/{action}";
    
    public static final String Locus_API_VI_EXPECTED_All_RESPONE_QUERY="/locus/api/v1/loci/expected_resource/queryall/{locusid}";
    
    public static final String Locus_API_VI_EXPECTED_RESPONE_REMOVE="/locus/api/v1/loci/expected_resource/remove/{locusid}/{action}";
    
    public static final String API_GET_PICTURE_SIMILARITY = "/picture/similarity/{pictureId}";
    
    public static final String Locus_HEALTH_CHECK = "/locus/health/check";
    
    public static final String Locus_ID = "/locus/api/v1/loci/lookupLocusUrl/{meetingkey}@{sitename}";
    
    public static final String Locus_FULL_DTO = "/locus/api/v1/loci/{locusid}";
    
    public static final String Locus_VIDEO_CALLBACK = "/locus/api/v1/loci/{locusid}/participant/{participantId}/videoCallback";
    
    public static final String Locus_Delta_EVENT_Sync = "/locus/api/v1/loci/{locusid}/sync";
    
    public static final String Locus_Avatar = "/locus/api/v1/loci/{locusid}/avatar";
    
    public static final String Locus_End_Meeting = "/locus/api/v1/loci/{locusid}/end";
    
    public static final String Locus_Webex_Host_Joined = "/locus/api/v1/loci/{locusid}/webExHostJoined";
    
    public static final String Locus_WebexUnsupportedFeature = "/locus/api/v1/loci/{locusid}/webExUnsupportedFeature";
    
    public static final String Locus_Media_Share_Floor = "/locus/api/v1/loci/{locusid}/mediashares/{mediaShare_id}";
    
    public static final String Locus_Query_Meeting_Status = "/locus/api/v1/loci/{meetingkey}@{sitename}/fullState";
 
}
