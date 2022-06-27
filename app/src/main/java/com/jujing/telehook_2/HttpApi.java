package com.jujing.telehook_2;

public class HttpApi {

    //    public static final String IP = "47.74.252.70";
    public static final String IP = "ec2-54-169-240-122.ap-southeast-1.compute.amazonaws.com";
    public static final String PORT = "8887";
    public static final String BASE = "http://" + IP + ":" + PORT;
    public static final String LOGIN = BASE + "/Login";
    public static final String ReplyMsg = BASE + "/ReplyMsg";
    public static final String UploadMsg = BASE + "/UploadMsg";
    public static final String getUploadToken = BASE + "/getUploadToken";
    public static final String AddMater = BASE + "/AddMater";
    public static final String AppUploadRepeatUser = BASE + "/AppUploadRepeatUser";
    public static final String Translate ="https://ts.crazy-customer.com/api/Translate/Translate";
    public static final String Detect ="https://ts.crazy-customer.com/api/Translate/Detect";
    public static final String AppKeepLive = BASE + "/AppKeepLive";
    public static final String CheckUserWeiXStatus = BASE + "/CheckUserWeiXStatus";
    public static final String GetContractPw = BASE + "/GetContractPw";//获取 -- 通讯录密码
    public static final String ReplyWeiXMsg = BASE + "/ReplyWeiXMsg";
    public static final String GetPlatformUInfos = BASE + "/GetPlatformUInfos";

    public static final String SOCKET = "ws://wxautoreply.muops.com/ws";
    //    public static final String SetReplyTime = "http://wxautoreply.muops.com/api/Set/SetReplyTime";
    public static final String SetReplyTime = "https://autoreply.crazy-customer.com/api/Set/SetReplyTime";
    public static final String FileUpload = "http://s3-2.crazy-customer.com/api/File/FileUpload";

    //    public static final String PLATFORM = "http://123qpapi.cndmks.com/API/Report/GetRegisterAccountsInfomation";
    public static final String GetGamePlatformUserInfos = BASE + "/GetGamePlatformUserInfos";
//    public static final String PLATFORM = "http://test-123qp-api.qaz411.com:9001/API/Report/GetRegisterAccountsInfomation";

    public static final String AddData = "https://wxreport.crazy-customer.com/api/Report/AddData";



    public static final String BAN_LOGIN = "http://ec2-54-169-240-122.ap-southeast-1.compute.amazonaws.com:8887/Login";


}
