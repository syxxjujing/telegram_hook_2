package com.jujing.telehook_2;

import android.os.Environment;

public class Global {
    public static final String XPOSED_CODE = "128";//TODO 测试  记得改isDebug！！！
    public static boolean IS_USE = false;//TODO 测试  false 是 nkt  记得改appName   nkt是疯狂
    public static boolean IS_DEBUG = false;//TODO 测试
    public static boolean IS_DEBUG_STNLOGIC = false;//TODO 测试
    public static boolean IS_DEBUG_MESSAGE_TEST = false;//TODO 测试
    public static boolean IS_TRAN_DEBUG = false;//TODO 测试
    public static boolean IS_DEBUG_XIAOMI = false;//TODO 测试
    public static boolean IS_DEBUG_LOGIN = false;//TODO 测试



    public static final String storage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/1tele_2";

    public static final String  ACTION_APP_LOG = "action_app_log_tele";
    public static final String  ACTION_APP_LOG_2 = "action_app_log_tele_2";
    public static final String  ACTION_APP_LOG_3 = "action_app_log_tele_3";
    public static final String  ACTION_APP_LOG_4 = "action_app_log_tele_4";
    public static final String  ACTION_APP_LOG_5 = "action_app_log_tele_5";
    public static final String  ACTION_APP_LOG_6 = "action_app_log_tele_6";
    public static final String  ACTION_APP_LOG_7 = "action_app_log_tele_7";
    public static final String STORAGE_APP_LOG = storage + "/storage_app_log";//
    public static final String STORAGE_APP_LOG_2 = storage + "/storage_app_log_2";//
    public static final String STORAGE_APP_LOG_3 = storage + "/storage_app_log_3";//
    public static final String STORAGE_APP_LOG_4 = storage + "/storage_app_log_4";//
    public static final String STORAGE_APP_LOG_5 = storage + "/storage_app_log_5";//
    public static final String STORAGE_APP_LOG_6 = storage + "/storage_app_log_6";//
    public static final String STORAGE_APP_LOG_7 = storage + "/storage_app_log_7";//
    public static final String STORAGE_KEY= storage + "/key";//
    public static final String BAN_LOGIN = storage + "/ban_login";

    public static final String TOKEN = storage + "/token.json";
    public static final String ACCOUNT = storage + "/account.json";
    public static final String LOGIN_JSON = storage + "/login_json.json";
    public static final String COMMENT_JSON = storage + "/comment_json.json";
    public static final String USER_ID = storage + "/user_id.json";
    public static final String USER_INFO_ID = storage + "/userinfo_id.json";
    public static final String USER_INFO_NICKNAME = storage + "/userinfo_nickname.json";
    public static final String USER_INFO_PHONE = storage + "/user_info_phone.json";
    public static final String LEVEL = storage + "/level.json";
    public static final String USER_INFO = storage + "/userinfo.json";
    public static final String CONTACTS_JSON = storage + "/contactsJson.json";
    public static final String CONTACTS_NUM = storage + "/contacts_num.json";
    public static final String CONTACTS_CHATROOM = storage + "/chatroomJson.json";
    public static final String STORAGE_FILE = storage + "/file2/";//file 改成file1 了
    public static final String STORAGE_SNS_VIDEO = storage + "/sns/video2/";
    public static final String STORAGE_SNS_TRANSFER = storage + "/sns/transfer/";
    public static final String STORAGE_NO_REPLY = storage + "/unreply/";
    public static final String AUTOREPLY_WXIDS = storage + "/AutoReply_wxids";
    public static final String IS_OPEN_RING = storage + "/is_open_ring";
    public static final String IS_OPEN_CHATROOM_VOICE = storage + "/is_open_chatroom_voice";
    public static final String IS_OPEN_CHECK_ZOMBIE = storage + "/is_open_check_zombie";
    public static final String IS_OPEN_AUTO_ACCEPT = storage + "/is_open_auto_accept";
    public static final String IS_OPEN_AUTO_REMARK = storage + "/is_open_auto_remark";

    public static final String REGISTRATION_JID = storage + "/registration_jid.json";
    public static final String PUSH_NAME = storage + "/push_name.json";
    public static final String STORAGE_XPOSED_CODE = storage + "/xposed_code.json";
    public static final String RECEIVE_BROADCAST_TIME = storage + "/receive_broadcast_time";

    public static final String IS_OPEN_CHECK_REPLY = storage + "/is_open_check_reply";
    public static final String IS_OPEN_CHECK_COUNTRY = storage + "/is_open_check_country";
    public static final String IS_OPEN_CHECK_TIME = storage + "/is_open_check_time";
    public static final String IS_OPEN_CHECK_REPLY_COUNTRY = storage + "/is_open_check_reply_country";
    public static final String IS_OPEN_CHECK_EMOJI = storage + "/is_open_check_emoji";
    public static final String IS_OPEN_REPEAT_CONTACT = storage + "/is_open_repeat_contact";
    public static final String IS_OPEN_TIME_ZONE = storage + "/is_open_time_zone";
    public static final String IS_OPEN_ARCHIVED = storage + "/is_open_archived";
    public static final String IS_OPEN_LOCK = storage + "/is_open_lock";
    public static final String ARCHIVED_NUM = storage + "/archived_num";
    public static final String IS_PLAY_VOICE = storage + "/is_play_voice";
    public static final String EMOJI_DELAY = storage + "/emoji_delay";
    public static final String IS_OPEN_CHECK_NO_REPLY = storage + "/is_open_check_no_reply";
    public static final String CHECK_NO_REPLY = storage + "/check_no_reply";
    public static final String COUNTRY_OR_TIME_INTERVAL = storage + "/country_or_time_interval";
    public static final String TRANSFER_TIME = storage + "/transfer_time";
    public static final String TIME_ZONE_CN = storage + "/TIME_ZONE_CN";
    public static final String TIME_ZONE_EN = storage + "/TIME_ZONE_EN";
    public static final String EVER_CONTACTS = storage + "/ever_contacts_2";
    public static final String ADDED_CONTACTS = storage + "/added_contacts_2";
    public static final String ACTIVE_ALL_OR_ARCHIVE = storage + "/active_all_or_archive";
    public static final String ACTIVE_TIME = storage + "/active_time";
    public static final String ACTIVE_INTERVAL = storage + "/active_interval";

    public static final String WHATSAPP_KEY_WORD_LIST_TIME_JSON = storage + "/whatsapp_key_word_list_time_json";
    public static final String WHATSAPP_KEY_WORD_LIST_COUNTRY_JSON = storage + "/whatsapp_key_word_list_country_json";
    public static final String WHATSAPP_KEY_WORD_LIST_LOCK_JSON = storage + "/whatsapp_key_word_list_lock_json";
    public static final String TRAN_LANG = storage + "/tran_lang";

    public static final String ACTIVE_FIRST_RESP_MSG = storage + "/active_first_resp_msg";


    public static final String IS_OPEN_PLATFORM = storage + "/is_open_platform";
    public static final String IS_OPEN_PLATFORM_REPLY = storage + "/is_open_platform_reply";
    public static final String IS_OPEN_PLATFORM_BAN = storage + "/is_open_platform_ban";
    public static final String IS_OPEN_PLATFORM_BAN_LOCAL = storage + "/is_open_platform_ban_local";
    public static final String PLATFORM_TIME = storage + "/platform_time";
    public static final String PLATFORM_TIME_JUDGE = storage + "/platform_time_judge";
    public static final String PLATFORM_CONTENT = storage + "/platform_content_position";
    public static final String SEARCHED_FRIEND_PHONE = storage + "/searched_friend_phone";
    public static final String SEARCHED_FRIEND_NUMBER = storage + "/searched_friend_number";

    public static final String UPLOADED_MESSAGES = storage + "/uploaded_messages_";


    public static final String ADD_FRIENDS_CONTENT = storage + "/add_friends_content";
    public static final String ADD_FRIENDS_DELAY = storage + "/add_friends_delay";
    public static final String ADDED_FRIEND_USERNAME = storage + "/added_friend_username";

    public static final String IS_OPEN_VOIP = storage + "/IS_OPEN_VOIP";
    public static final String ALREADY_VIDEO = storage + "/already_video";
    public static final String VOIP_TIME = storage + "/voip_time";


    public static final String STORAGE_LOCAL_REPLY_JSON = storage + "/local_reply_json.json";
    public static final String STORAGE_LOCAL_REPLY_JSON_2 = storage + "/local_reply_json_2.json";
    public static final String INTERVAL_FRIENDS = storage + "/interval_friends";
    public static final String INTERVAL_MESSAGES = storage + "/interval_messages";


    public static final String DAZHAOHU_TYPE = storage + "/dazhaohu_type";
    public static final String SENT_MESSAGES_USER = storage + "/sent_messages_user_2";

    public static final String IS_GROUP_ONLINE = storage + "/is_group_online";
    public static final String IS_GROUP_NICK_CHINESE = storage + "/is_group_nick_chinese";
    public static final String IS_GROUP_ABOUT_CHINESE = storage + "/is_group_about_chinese";
    public static final String IS_GROUP_DAY = storage + "/is_group_day";
    public static final String IS_GROUP_CREATE = storage + "/is_group_create";
    public static final String GROUP_CHAT_ID = storage + "/group_chat_id";
    public static final String GROUP_CHAT_INFO = "/sdcard/Download/群用户信息/";
    public static final String ONLINE_DAY = storage + "/online_day";
    public static final String ONLINE_DAY2 = storage + "/online_day2";

    public static final String GOT_INFO = storage + "/got_info_3";
    public static final String GOT_INFO_INDEX = storage + "/got_info_index_3/";
    public static final String GOT_INFO_INDEX_2 = storage + "/got_info_index_4_jjj/";
    public static final String ADDED_CONTACTS_NUM = storage + "/added_contacts_num";

    public static final String GOT_INFO_USERNAME = storage + "/got_info_username";
    public static final String GOT_USERNAME = storage + "/got_username/";

//    public static final String CHAT_IDS = storage + "/chat_ids";

    public static final String BLACK_PATH = storage + "/black_path";
    public static final String IS_STOP_CAIJI = storage + "/is_stop_caiji";
    public static final String IS_START_CAIJI = storage + "/is_start_caiji";

    public static final String GROUP_JOIN_INTERVAL = storage + "/group_join_interval";

    public static final String GROUP_ADD_INVITE_INTERVAL = storage + "/group_add_invite_interval";
    public static final String GROUP_ADD_INVITE_INTERVAL_2 = storage + "/group_add_invite_interval_2";
    public static final String GROUP_ADD_INVITE_NUM = storage + "/group_add_invite_num";
    public static final String GROUP_ADD_INVITE_NUM_2 = storage + "/group_add_invite_num_2";
    public static final String GROUP_ADD_INVITE_ADMIN_ID = storage + "/group_add_invite_admin_id";

    public static final String GROUP_LIST_JSON = storage + "/group_list_json";


    public static final String ADDED_CONTACT = storage + "/added_contact";


    public static final String GROUP_ADD_INVITE_TITLE = storage + "/group_add_invite_title";

    public static final String GROUP_ADD_INVITE_ID = storage + "/group_add_invite_id";


    public static final String GROUP_ADD_MEMBER_ID = storage + "/group_add_member_id";
    public static final String GROUP_ADD_MEMBER_NUM = storage + "/group_add_member_num";

    public static final String SENT_UID = storage + "/sent_uid/";
    public static final String MESSAGE_SUCCESS_NUM = storage + "message_success_num";
    public static final String MESSAGE_READ_NUM = storage + "message_read_num";


    public static final String COUNTRY_JUDGE = storage + "/country_judge/";
    public static final String LANG_JUDGE = storage + "/lang_judge/";

    public static final String IS_ONLY_UNREAD = storage + "/is_only_unread";
    public static final String SAY_HI_ROUND_INTERVAL = storage + "/say_hi_round_interval";









}
