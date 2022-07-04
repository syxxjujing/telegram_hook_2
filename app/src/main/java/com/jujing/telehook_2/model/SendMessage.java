package com.jujing.telehook_2.model;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.hook.HookActivity;
import com.jujing.telehook_2.model.operate.Tools;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.FileUtils;
import com.jujing.telehook_2.util.LogTool;
import com.jujing.telehook_2.util.LoggerUtil;

import org.telegram.tgnet.TLRPC;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static com.jujing.telehook_2.HookMain.classLoader;
import static com.jujing.telehook_2.model.operate.Tools.getAccountInstance;

public class SendMessage {

    private static final String TAG = "SendMessage";

    public static void sendMessage0(int account, final String text, final long user) {

        try {


            Class MessageObject = classLoader.loadClass("org.telegram.messenger.MessageObject");
            Class WebPage = classLoader.loadClass("org.telegram.tgnet.TLRPC$WebPage");
            Class ReplyMarkup = classLoader.loadClass("org.telegram.tgnet.TLRPC$ReplyMarkup");
            Class SendMessagesHelper = classLoader.loadClass("org.telegram.messenger.SendMessagesHelper");
            Class SendAnimationData = classLoader.loadClass("org.telegram.messenger.MessageObject$SendAnimationData");
            final Method mSendMessage = SendMessagesHelper.getDeclaredMethod("sendMessage", String.class, long.class, MessageObject, MessageObject,
                    WebPage, boolean.class, ArrayList.class, ReplyMarkup, HashMap.class, boolean.class, int.class, SendAnimationData);

            final Object SendMessagesHelperIns = XposedHelpers.callStaticMethod(SendMessagesHelper, "getInstance", account);
            //必须再说主线程执行
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    try {
                        mSendMessage.invoke(SendMessagesHelperIns, text, user, null,
                                null, null, true, null,
                                null, null, true, 0, null);
                        LoggerUtil.logI(TAG, "mSendMessage  57====");
                    } catch (Exception e) {
                        LoggerUtil.logI(TAG, "e  60====" + e);
                    }


                }
            });
            LoggerUtil.logI(TAG, "mSendMessage  67====");
        } catch (Exception e) {
        }

    }

    public static void sendMessage(final String text, final long user) {

        try {

            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");

            int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");

            Class MessageObject = classLoader.loadClass("org.telegram.messenger.MessageObject");
            Class WebPage = classLoader.loadClass("org.telegram.tgnet.TLRPC$WebPage");
            Class ReplyMarkup = classLoader.loadClass("org.telegram.tgnet.TLRPC$ReplyMarkup");
            Class SendMessagesHelper = classLoader.loadClass("org.telegram.messenger.SendMessagesHelper");
            Class SendAnimationData = classLoader.loadClass("org.telegram.messenger.MessageObject$SendAnimationData");
            final Method mSendMessage = SendMessagesHelper.getDeclaredMethod("sendMessage", String.class, long.class, MessageObject, MessageObject,
                    WebPage, boolean.class, ArrayList.class, ReplyMarkup, HashMap.class, boolean.class, int.class, SendAnimationData);

            final Object SendMessagesHelperIns = XposedHelpers.callStaticMethod(SendMessagesHelper, "getInstance", currentAccount);
            //必须再说主线程执行
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mSendMessage.invoke(SendMessagesHelperIns, text, user, null,
                                null, null, true, null,
                                null, null, true, 0, null);

                    } catch (Exception e) {
//                        Tool.printException(e);
                    }


                }
            });
        } catch (Exception e) {
//            Tool.printException(e);
        }

    }
    public  static void markDialogAsRead(long from_id){
//        TLRPC.Dialog dialog = getMessagesController().dialogs_dict.get(939531867);
//        getMessagesController().markDialogAsRead(939531867, dialog.top_message, dialog.top_message, dialog.last_message_date, false, 0, 0, true, 0);

        Object aaa=getAccountInstance(classLoader);
        Object messagesController=XposedHelpers.callMethod(aaa,"getMessagesController");
        Object dialogs_dict = XposedHelpers.getObjectField(messagesController, "dialogs_dict");
        Object dialog = XposedHelpers.callMethod(dialogs_dict, "get", from_id);
        XposedHelpers.callMethod(messagesController,"markDialogAsRead",from_id
                ,XposedHelpers.getIntField(dialog,"top_message"),XposedHelpers.getIntField(dialog,"top_message")
                ,XposedHelpers.getIntField(dialog,"last_message_date"),false,0,0,true,0);


    }
    /**
     * @param isChat 如果是群就传true
     */
    public static void sendText(final boolean isChat, final long id, final String content) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //以下是发送文字的
                Class<?> aClass = XposedHelpers.findClass("org.telegram.messenger.SendMessagesHelper", classLoader);
                Object getInstance = XposedHelpers.callStaticMethod(aClass, "getInstance", UsersAndChats.getCurrentUserId(classLoader));
                if (isChat) {
                    XposedHelpers.callMethod(getInstance, "sendMessage", content, id * -1, null, null, true, null, null, null);
                } else {
//                    XposedHelpers.callMethod(getInstance, "sendMessage", content, id, null, null, true, null, null, null);
                    XposedHelpers.callMethod(getInstance, "sendMessage", content, id, null, null, null, true, null, null, null, true, 0, null);
                }
            }
        };
        Class<?> aClass = XposedHelpers.findClass("org.telegram.messenger.AndroidUtilities", classLoader);
        XposedHelpers.callStaticMethod(aClass, "runOnUIThread", runnable);


    }

    /**
     * @param isChat 如果是群就传true
     * @param path   为本地地址
     */
    public static void sendImage(final boolean isChat, final long id, final String path) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //以下是发送图片的
                Class<?> aClass = XposedHelpers.findClass("org.telegram.messenger.SendMessagesHelper", classLoader);
                Object getInstance = XposedHelpers.callStaticMethod(aClass, "getInstance", UsersAndChats.getCurrentUserId(classLoader));
                Object generatePhotoSizes = XposedHelpers.callMethod(getInstance, "generatePhotoSizes", path, null);
//                sss  54--->org.telegram.tgnet.TLRPC$TL_photo@f102266,null,746055308,null,null,null,null,{groupId=0, originalPath=/storage/emulated/0/11Siri/bgBottomBitmap.png30971_1561946476000, final=1},0,null,
                HashMap<String, String> params = new HashMap<>();
                params.put("groupId", "0");
                params.put("originalPath", path);
                params.put("final", "1");
                if (isChat) {
                    XposedHelpers.callMethod(getInstance, "sendMessage", generatePhotoSizes, null, id * -1, null, null, null, null, params, 0, null);
                } else {

//                    public void sendMessage(TLRPC.TL_photo photo, String path, long peer,
// MessageObject replyToMsg, MessageObject replyToTopMsg, String caption,
// ArrayList<TLRPC.MessageEntity> entities, TLRPC.ReplyMarkup replyMarkup,
// HashMap<String, String> params, boolean notify, int scheduleDate, int ttl, Object parentObject) {


//                    XposedHelpers.callMethod(getInstance, "sendMessage", generatePhotoSizes, null, id, null, null, null, null, params, 0, null);
                    XposedHelpers.callMethod(getInstance, "sendMessage", generatePhotoSizes, path, id,
                            null, null, "", null, null, params, true, 0, 0, null);
                    //TODO  这个字符串参数 是要加的文字
                }
            }
        };
        Class<?> aClass = XposedHelpers.findClass("org.telegram.messenger.AndroidUtilities", classLoader);
        XposedHelpers.callStaticMethod(aClass, "runOnUIThread", runnable);
    }

    /**
     * 发视频
     */
    public static void sendVideo(final boolean isChat, final long id, final String path) {

        try {
            LoggerUtil.logI(TAG, "sendVideo  197 ---->" + id + "---->" + path+"---->"+new File(path).exists());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    //以下是发送图片的
                    Class<?> aClass = XposedHelpers.findClass("org.telegram.messenger.SendMessagesHelper", classLoader);
                    Object getInstance = XposedHelpers.callStaticMethod(aClass, "getInstance", UsersAndChats.getCurrentUserId(classLoader));

                    Class<?> TLRPC$TL_document = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_document", classLoader);
                    Object tl_document = XposedHelpers.newInstance(TLRPC$TL_document);
                    XposedHelpers.setLongField(tl_document, "id", 0);
                    XposedHelpers.setIntField(tl_document, "date", 0);

                    String screenshotPath = path.replace(".mp4", "") + ".jpg";
                    boolean exists = new File(screenshotPath).exists();
                    LoggerUtil.logI(TAG, "screenshotPath 185--->" + screenshotPath + "----->" + exists);
                    if (!exists) {
                        Bitmap videoThumb = FileUtils.getVideoThumb(path);
                        boolean b = FileUtils.bitmap2File(videoThumb, screenshotPath);
                        LoggerUtil.logI(TAG, "bbbb 187--->" + b);
                    }

                    Object TL_photo = XposedHelpers.callMethod(getInstance, "generatePhotoSizes", screenshotPath, null);
                    List sizes = (List) XposedHelpers.getObjectField(TL_photo, "sizes");
                    LoggerUtil.logI(TAG, "sizes  166--->" + sizes);

//                    Class<?> TLRPC$TL_photoSizeEmpty = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_photoSizeEmpty", classLoader);
//                    Object photoSizeEmpty = XposedHelpers.newInstance(TLRPC$TL_photoSizeEmpty);
                    XposedHelpers.setObjectField(tl_document, "thumbs", sizes);

                    XposedHelpers.setObjectField(tl_document, "mime_type", "video/mp4");
                    List list = new ArrayList();
                    Class<?> TLRPC$TL_documentAttributeAudio = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_documentAttributeVideo", classLoader);
                    Object documentAttributeAudio = XposedHelpers.newInstance(TLRPC$TL_documentAttributeAudio);
                    XposedHelpers.setBooleanField(documentAttributeAudio, "voice", false);
                    int ringDuring = 0;
                    try {
                        ringDuring = Integer.parseInt(FileUtils.getRingDuring(path)) / 1000;
                    } catch (Exception e) {
                    }
                    LoggerUtil.logI(TAG, "ringDuring  192 ---->" + ringDuring);

                    XposedHelpers.setIntField(documentAttributeAudio, "duration", ringDuring);
//                    XposedHelpers.setIntField(documentAttributeAudio,"w", 720);
//                    XposedHelpers.setIntField(documentAttributeAudio,"h", 1280);
                    list.add(documentAttributeAudio);


                    XposedHelpers.setObjectField(tl_document, "attributes", list);
//                    XposedHelpers.setIntField(tl_document,"size", (int) new File(path).length());
                    HashMap<String, String> params = new HashMap<>();

//                    params.put("ve","-1_-1_-1_90_1280_720_900000_640_360_24_/storage/emulated/0/360/1.mp4");
//                    params.put("groupId","0");
                    params.put("originalPath", screenshotPath);
//                    params.put("final", "1");
//                    (TLRPC.TL_document document, VideoEditedInfo videoEditedInfo, String path, long peer, MessageObject reply_to_msg, String caption, ArrayList<TLRPC.MessageEntity> entities, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> params, int ttl, Object parentObject)
                    if (isChat) {
                        XposedHelpers.callMethod(getInstance, "sendMessage", tl_document, null, path, id * -1, null, null, null, null, params, 0, null);
                    } else {
//                        XposedHelpers.callMethod(getInstance, "sendMessage", tl_document, null, path, id, null, null, null, null, params, 0, null);

                        XposedHelpers.callMethod(getInstance, "sendMessage", tl_document, null, path
                                , id, null, null, "", null, null, params, false, 0, 0, null, null);
                    }

                    LoggerUtil.logI(TAG, "sendVideo  end 263 ---->" + id + "---->" + path+"---->"+new File(path).exists());

                }
            };
            Class<?> aClass = XposedHelpers.findClass("org.telegram.messenger.AndroidUtilities", classLoader);
            XposedHelpers.callStaticMethod(aClass, "runOnUIThread", runnable);
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee 83---->" + CrashHandler.getInstance().printCrash(e));
        }
    }

    //mime_type 音频格式 "audio/"+格式 比如MP3是audio/mp3
    public static void sendVoice0(long toUid, String voicePath) {
        try {
            // Thread.sleep(5000);
            LoggerUtil.logI(TAG, "sendVoice  262 ---->" + toUid + "---->" + voicePath);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {


                        File fvoice = new File(voicePath);

                        Class TL_document = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_document");
                        Object audioToSend = XposedHelpers.newInstance(TL_document);


                        Class SharedConfig = classLoader.loadClass("org.telegram.messenger.SharedConfig");
                        int localId = (int) XposedHelpers.callStaticMethod(SharedConfig, "getLastLocalId");


                        MediaPlayer mediaPlayer = MediaPlayer.create(HookActivity.baseActivity, Uri.fromFile(fvoice));
                        ;
                        int date = (int) System.currentTimeMillis() / 1000;
                        XposedHelpers.setIntField(audioToSend, "date", date);
                        XposedHelpers.setIntField(audioToSend, "size", (int) fvoice.length());

//                        XposedHelpers.setObjectField(audioToSend, "mime_type", "audio/mp3");
                        XposedHelpers.setObjectField(audioToSend, "mime_type", "audio/ogg");
                        long user_id = Tools.getClientUserId(classLoader);
                        XposedHelpers.setLongField(audioToSend, "user_id", user_id);
                        XposedHelpers.setIntField(audioToSend, "dc_id", Integer.MIN_VALUE);
                        XposedHelpers.setIntField(audioToSend, "id", localId);
                        XposedHelpers.setObjectField(audioToSend, "file_reference", new byte[0]);


                        Class TL_documentAttributeAudio = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_documentAttributeAudio");
                        Object attributeAudio = XposedHelpers.newInstance(TL_documentAttributeAudio);

                        XposedHelpers.setBooleanField(attributeAudio, "voice", true);
                        XposedHelpers.setIntField(attributeAudio, "duration", mediaPlayer.getDuration() / 1000);

                        mediaPlayer.release();
                        List attributes = (List) XposedHelpers.getObjectField(audioToSend, "attributes");
                        attributes.add(attributeAudio);


                        Object messagesHelper = Tools.getSendMessagesHelper(classLoader);

                        Class SendMessagesHelper = classLoader.loadClass("org.telegram.messenger.SendMessagesHelper");
                        Class VideoEditedInfo = classLoader.loadClass("org.telegram.messenger.VideoEditedInfo");
                        Class MessageObject = classLoader.loadClass("org.telegram.messenger.MessageObject");
                        Class SendAnimationData = classLoader.loadClass("org.telegram.messenger.MessageObject$SendAnimationData");
                        Class ReplyMarkup = classLoader.loadClass("org.telegram.tgnet.TLRPC$ReplyMarkup");

                        Method msendMessage = SendMessagesHelper.getDeclaredMethod("sendMessage", TL_document,
                                VideoEditedInfo,
                                String.class, long.class, MessageObject,
                                MessageObject, String.class, ArrayList.class,
                                ReplyMarkup, HashMap.class, boolean.class, int.class, int.class, Object.class, SendAnimationData);
                        msendMessage.setAccessible(true);
                        msendMessage.invoke(messagesHelper, audioToSend, null,
                                voicePath, toUid, null, null, null, null,
                                null, null, true,
                                0, 0, null, null);

                    } catch (Exception e) {

                        LoggerUtil.logI(TAG, "eee 327---->" + CrashHandler.getInstance().printCrash(e));
                    }
                }
            });

        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee 334---->" + CrashHandler.getInstance().printCrash(e));
        }

    }

    /**
     * 发语音
     */
    public static void sendVoice(final long id, final String path) {

        try {
            LoggerUtil.logI(TAG, "sendVoice  99 ---->" + id + "---->" + path+"---->"+new File(path).exists());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    //以下是发送图片的
                    Class<?> aClass = XposedHelpers.findClass("org.telegram.messenger.SendMessagesHelper", classLoader);
                    Object getInstance = XposedHelpers.callStaticMethod(aClass, "getInstance", UsersAndChats.getCurrentUserId(classLoader));

                    Class<?> TLRPC$TL_document = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_document", classLoader);
                    Object tl_document = XposedHelpers.newInstance(TLRPC$TL_document);
                    XposedHelpers.setLongField(tl_document, "id", 0);

                    Class<?> ConnectionsManager = XposedHelpers.findClass("org.telegram.tgnet.ConnectionsManager", classLoader);
                    Object connectionsManager = XposedHelpers.callStaticMethod(ConnectionsManager, "getInstance", UsersAndChats.getCurrentUserId(classLoader));
                    int getCurrentTime = (int) XposedHelpers.callMethod(connectionsManager, "getCurrentTime");
                    LoggerUtil.logI(TAG, "getCurrentTime  106 ---->" + getCurrentTime);
                    XposedHelpers.setIntField(tl_document, "date", getCurrentTime);

//                    Class<?> TLRPC$TL_photoSizeEmpty = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_photoSizeEmpty", classLoader);
//                    Object photoSizeEmpty = XposedHelpers.newInstance(TLRPC$TL_photoSizeEmpty);
//                    XposedHelpers.setObjectField(tl_document,"thumb",photoSizeEmpty);

                    List list = new ArrayList();
                    Class<?> TLRPC$TL_documentAttributeAudio = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_documentAttributeAudio", classLoader);
                    Object documentAttributeAudio = XposedHelpers.newInstance(TLRPC$TL_documentAttributeAudio);
                    XposedHelpers.setBooleanField(documentAttributeAudio, "voice", true);

                    long audioFileVoiceTime = FileUtils.getAudioFileVoiceTime(path);
                    LoggerUtil.logI(TAG, "audioFileVoiceTime  277 ---->" + audioFileVoiceTime);
                    int ringDuring = 0;
                    try {
                        ringDuring = (int) (audioFileVoiceTime / 1000);
                    } catch (Exception e) {
                    }
                    LoggerUtil.logI(TAG, "ringDuring  128 ---->" + ringDuring);

                    XposedHelpers.setIntField(documentAttributeAudio, "duration", ringDuring);

//                    byte[] bytes1 = FileUtils.File2byte(path);
                    byte[] bytes = path.getBytes();
//                    LoggerUtil.logI(TAG,"bytes1  298 ---->"+bytes1.length);
                    XposedHelpers.setObjectField(documentAttributeAudio, "waveform", bytes);
                    list.add(documentAttributeAudio);
                    XposedHelpers.setObjectField(tl_document, "attributes", list);
                    XposedHelpers.setObjectField(tl_document, "mime_type", "audio/ogg");
//                    XposedHelpers.setIntField(tl_document,"size", (int) new File(path).length());


//                    sendMessage(null, caption, null, null, videoEditedInfo, null
//                            , document, null, null, null, peer, path, replyToMsg
//                            , replyToTopMsg, null, true, null, entities, replyMarkup
//                            , params, notify, scheduleDate, ttl, parentObject, sendAnimationData);

                    HashMap<String, String> params = new HashMap<>();
//                    params.put("groupId", "0");
//                    params.put("originalPath", path);
//                    params.put("final", "1");
                    XposedHelpers.callMethod(getInstance, "sendMessage", tl_document, null, path
                            , id, null, null, "", null, null, params, false, 0, 0, null, null);

                    LoggerUtil.logI(TAG, "sendVoice  end 417 ---->" + id + "---->" + path+"---->"+new File(path).exists());
                }
            };
            LoggerUtil.logI(TAG, "HookActivity.baseActivity  101---->" + HookActivity.baseActivity + "------");
            HookActivity.baseActivity.runOnUiThread(runnable);
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee 83---->" + CrashHandler.getInstance().printCrash(e));
        }
    }

//    public void sendMessage(TLRPC.TL_document document, VideoEditedInfo videoEditedInfo, String path, long peer, MessageObject replyToMsg, MessageObject replyToTopMsg, String caption, ArrayList<TLRPC.MessageEntity> entities, TLRPC.ReplyMarkup replyMarkup, HashMap<String, String> params, boolean notify, int scheduleDate, int ttl, Object parentObject, MessageObject.SendAnimationData sendAnimationData) {
//        sendMessage(null, caption, null, null, videoEditedInfo, null, document, null, null, null, peer, path, replyToMsg, replyToTopMsg, null, true, null, entities, replyMarkup, params, notify, scheduleDate, ttl, parentObject, sendAnimationData);
//    }


    public static void updateStatus() {
        Class<?> TLRPC$TL_account_updateStatus = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_account_updateStatus", classLoader);
        Object req = XposedHelpers.newInstance(TLRPC$TL_account_updateStatus);
        XposedHelpers.setBooleanField(req, "offline", true);

        Class<?> $$Lambda$MessagesController$ueghtSvHFDbkkKRlzH3zhB7vPCY = XposedHelpers.findClass("org.telegram.messenger.-$$Lambda$MessagesController$ueghtSvHFDbkkKRlzH3zhB7vPCY", HookMain.classLoader);
        Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", HookMain.classLoader);
        Object getInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", 0);
        Object o = XposedHelpers.newInstance($$Lambda$MessagesController$ueghtSvHFDbkkKRlzH3zhB7vPCY, getInstance);
        Class<?> ConnectionsManager = XposedHelpers.findClass("org.telegram.tgnet.ConnectionsManager", HookMain.classLoader);
        Object getInstance2 = XposedHelpers.callStaticMethod(ConnectionsManager, "getInstance", 0);
        XposedHelpers.callMethod(getInstance2, "sendRequest", req, o);
    }


//    public static void readHistory(int id){//注意:如果是群的话  传id*-1
//        Class<?> TLRPC$TL_messages_readHistory = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_messages_readHistory", classLoader);
//        Object req = XposedHelpers.newInstance(TLRPC$TL_messages_readHistory);
//
//        Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", HookMain.classLoader);
//        Object getInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", 0);
//        Object getInputPeer = XposedHelpers.callMethod(getInstance, "getInputPeer", id);
//        XposedHelpers.setObjectField(req,"peer",getInputPeer);
//        XposedHelpers.setIntField(req,"max_id",96);
//
//        Class<?> $$Lambda$MessagesController$f6Fg5cePsPVR4IQG1UUiIG6Ywco = XposedHelpers.findClass("org.telegram.messenger.-$$Lambda$MessagesController$f6Fg5cePsPVR4IQG1UUiIG6Ywco", HookMain.classLoader);
//        Object o = XposedHelpers.newInstance($$Lambda$MessagesController$f6Fg5cePsPVR4IQG1UUiIG6Ywco,getInstance);
//
//        Class<?> ConnectionsManager = XposedHelpers.findClass("org.telegram.tgnet.ConnectionsManager", HookMain.classLoader);
//        Object getInstance2 = XposedHelpers.callStaticMethod(ConnectionsManager, "getInstance", 0);
//        XposedHelpers.callMethod(getInstance2, "sendRequest", req, o);
//    }


    public static void seachUsers(final String txt) {
        try {

            // txt="@Kenn_Wu";
            Class TL_contacts_search = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_contacts_search");
//            org.telegram.tgnet.TLRPC$TL_users_getFullUser
            Object req = XposedHelpers.newInstance(TL_contacts_search);
            XposedHelpers.setObjectField(req, "q", txt);
            XposedHelpers.setIntField(req, "limit", 5);

            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");
            final int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");

            Class ConnectionsManager = classLoader.loadClass("org.telegram.tgnet.ConnectionsManager");
            Object ConnectionsManagerIns = XposedHelpers.callStaticMethod(ConnectionsManager, "getInstance", currentAccount);

            Class RequestDelegate = classLoader.loadClass("org.telegram.tgnet.RequestDelegate");
            Object cb = Proxy.newProxyInstance(classLoader, new Class[]{RequestDelegate}, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                    String mName = method.getName();
                    if (mName.equals("run")) {

                        run(objects[0], objects[1]);
                    }
                    return null;
                }

                void run(Object response, Object error) {
                    if (error != null) {
                        String text = (String) XposedHelpers.getObjectField(error, "text");
                        int code = XposedHelpers.getIntField(error, "code");
//                        Tool.showMsg(context, "搜索出错:" + text + ":" + code);
                        LoggerUtil.logI(TAG, "搜索出错:" + text + ":" + code);
                        return;
                    }
                    if (response == null) {
                        LoggerUtil.logI(TAG, "搜索出错:response==null");
                        return;
                    }

                    ArrayList users = (ArrayList) XposedHelpers.getObjectField(response, "users");

                    if (users == null || users.isEmpty()) {
                        LoggerUtil.logI(TAG, "搜索出错:users==null||users.isEmpty");
                        return;
                    }

                    for (int a = 0; a < users.size(); a++) {
                        Object user = users.get(a);


                        final long id = XposedHelpers.getLongField(user, "id");
                        LoggerUtil.logI(TAG, "user id:" + id);
                        getFullUser(user, id);

                        String t = txt.replaceFirst("@", "");


                        String username = (String) XposedHelpers.getObjectField(user, "username");
                        LoggerUtil.logI(TAG, "user username:" + username);

                        if (t.equals(username)) {


//                            Tool.showMsg(context, "搜索到用户" + username + " id:" + id + " 发消息给他");
                            LoggerUtil.logI(TAG, "搜索到用户" + username + " id:" + id + " 发消息给他");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    SystemClock.sleep(3000);
                                    sendMessage0(currentAccount, "12346", id);
                                }
                            }).start();

                        }

                    }

                }
            });
            XposedHelpers.callMethod(ConnectionsManagerIns, "sendRequest", req, cb);

        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  312---->" + CrashHandler.getInstance().printCrash(e));
        }
    }

    public static void getFullUser(Object user, final long id) {
        Class<?> MessagesController = XposedHelpers.findClass("org.telegram.messenger.MessagesController", HookMain.classLoader);
        Object getInstance = XposedHelpers.callStaticMethod(MessagesController, "getInstance", UsersAndChats.getCurrentUserId(classLoader));

        Object getInputUser = XposedHelpers.callMethod(getInstance, "getInputUser", id);

        Class<?> TLRPC$TL_users_getFullUser = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_users_getFullUser", classLoader);
        Object req = XposedHelpers.newInstance(TLRPC$TL_users_getFullUser);
        XposedHelpers.setObjectField(req, "id", getInputUser);
        LoggerUtil.logI(TAG, "req  375---->" + req);
        Class<?> MessagesController$$ExternalSyntheticLambda317 = XposedHelpers.findClass("org.telegram.messenger.MessagesController$$ExternalSyntheticLambda317", HookMain.classLoader);

        Object o = XposedHelpers.newInstance(MessagesController$$ExternalSyntheticLambda317, getInstance, user, 2);
        Class<?> ConnectionsManager = XposedHelpers.findClass("org.telegram.tgnet.ConnectionsManager", HookMain.classLoader);
        Object getInstance2 = XposedHelpers.callStaticMethod(ConnectionsManager, "getInstance", 0);
        XposedHelpers.callMethod(getInstance2, "sendRequest", req, o);
        LoggerUtil.logI(TAG, "req  382---->" + req);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(5000);
                LoggerUtil.logI(TAG, "sendMessage  384---->" + id);
                sendMessage("12346", id);
                LoggerUtil.logI(TAG, "sendMessage  386---->" + id);
            }
        }).start();
    }

    public static void sendGif(long toUid, String path) {
        try {
            LoggerUtil.logI(TAG, "sendGif  484:" + path);
            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");
            int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");
            Class AccountInstance = classLoader.loadClass("org.telegram.messenger.AccountInstance");
            Object AccountInstanceIns = XposedHelpers.callStaticMethod(AccountInstance, "getInstance", currentAccount);
//            String path="/sdcard/Pictures/test.gif";

            if (!new File(path).exists()) {
                XposedBridge.log("文件不存在");
                return;
            }
//            long toUid=1801589119;
            Class SendingMediaInfo = classLoader.loadClass("org.telegram.messenger.SendMessagesHelper$SendingMediaInfo");
            Object SendingMediaInfoObj = XposedHelpers.newInstance(SendingMediaInfo);
            XposedHelpers.setObjectField(SendingMediaInfoObj, "path", path);
            XposedHelpers.setIntField(SendingMediaInfoObj, "ttl", 0);
            XposedHelpers.setBooleanField(SendingMediaInfoObj, "isVideo", false);
            XposedHelpers.setBooleanField(SendingMediaInfoObj, "canDeleteAfter", true);
            XposedHelpers.setBooleanField(SendingMediaInfoObj, "forceImage", false);

            ArrayList medias = new ArrayList();
            medias.add(SendingMediaInfoObj);
            Class SendMessagesHelper = classLoader.loadClass("org.telegram.messenger.SendMessagesHelper");
            XposedHelpers.callStaticMethod(SendMessagesHelper, "prepareSendingMedia",
                    AccountInstanceIns, medias, toUid, null, null, null, false, true, null, true, 0);

            LoggerUtil.logI(TAG, "sendGif  511:" + path);
        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e  514---->" + CrashHandler.getInstance().printCrash(e));
        }
    }

}
