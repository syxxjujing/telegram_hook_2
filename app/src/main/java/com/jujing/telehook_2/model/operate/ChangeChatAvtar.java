package com.jujing.telehook_2.model.operate;

import android.content.Context;

import com.google.gson.Gson;
import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.model.XCMethodHookBase;
import com.jujing.telehook_2.util.LoggerUtil;


import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class ChangeChatAvtar extends XCMethodHookBase implements InvocationHandler {

    private static final String TAG = "ChangeChatAvtar";
    long chatId;

    Object rawDelegate;

    public ChangeChatAvtar(Context context, long chatId, Object rawDelegate) {

        super(context);
        this.chatId = chatId;
        this.rawDelegate = rawDelegate;
    }

    public static void newReq(Context context, String path, long chatId) {
        try {
            ClassLoader classLoader = HookMain.classLoader;
            Class RequestDelegate = classLoader.loadClass("org.telegram.tgnet.RequestDelegate");
            Class FileLoaderDelegate = classLoader.loadClass("org.telegram.messenger.FileLoader$FileLoaderDelegate");

            Object fileLoaderInstance = Tools.getFileLoaderInstance(classLoader);
            Object rawDelegate = XposedHelpers.getObjectField(fileLoaderInstance, "delegate");
            Object callback = Proxy.newProxyInstance(classLoader, new Class[]{FileLoaderDelegate, RequestDelegate},
                    new ChangeChatAvtar(context, chatId, rawDelegate));


            XposedHelpers.callMethod(fileLoaderInstance, "setDelegate", callback);
            XposedHelpers.callMethod(fileLoaderInstance, "uploadFile", path, false, true, 16777216);

            LoggerUtil.sendLog4("开始上传头像:"  + " id:" + chatId);

        } catch (Exception e) {
        }
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        String mName = method.getName();
        //log(this,"invoke mName:"+mName);
        switch (mName) {
            case "fileUploadProgressChanged":
                fileUploadProgressChanged(objects[0], (String) objects[1], (long) objects[2], (long) objects[3], (boolean) objects[4]);
                break;
            case "fileDidUploaded":
                fileDidUploaded(o, (String) objects[0], objects[1], objects[2],
                        (byte[]) objects[3], (byte[]) objects[4], (long) objects[5]);
                break;
            case "fileDidFailedUpload":
                fileDidFailedUpload((String) objects[0], (boolean) objects[1]);
                break;
            case "fileDidLoaded":
                fileDidLoaded((String) objects[0], (File) objects[1], (int) objects[2]);
                break;
            case "fileDidFailedLoad":
                fileDidFailedLoad((String) objects[0], (int) objects[1]);
                break;
            case "fileLoadProgressChanged":
                fileLoadProgressChanged(objects[0], (String) objects[1], (long) objects[2], (long) objects[3]);
                break;

            case "run":
                run(objects[0], objects[1]);


        }
        return null;
    }

    void fileUploadProgressChanged(Object FileUploadOperation, String location, long uploadedSize,
                                   long totalSize, boolean isEncrypted) {


        log(this, "fileUploadProgressChanged uploadedSize:" + uploadedSize + " totalSize:" + totalSize);
    }

    void fileDidUploaded(Object proxy, String location, Object inputFile, Object InputEncryptedFile,
                         byte[] key, byte[] iv, long totalFileSize) {

        try {
            log(this, "fileDidUploaded location:" + location);
            log(this, "fileDidUploaded inputFile:" + new Gson().toJson(inputFile));

            Class editChatPhoto = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_messages_editChatPhoto");
            Class inputChatUploadedPhoto = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_inputChatUploadedPhoto");
            Object inputChatUploadedPhotoObj = XposedHelpers.newInstance(inputChatUploadedPhoto);
            XposedHelpers.setObjectField(inputChatUploadedPhotoObj, "file", inputFile);
            int flags = XposedHelpers.getIntField(inputChatUploadedPhotoObj, "flags");
            XposedHelpers.setIntField(inputChatUploadedPhotoObj, "flags", flags |= 1);
            Object editChatPhotoObj = XposedHelpers.newInstance(editChatPhoto);

            XposedHelpers.setLongField(editChatPhotoObj, "chat_id", chatId);
            XposedHelpers.setObjectField(editChatPhotoObj, "photo", inputChatUploadedPhotoObj);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Thread.sleep(5000);
                        Object mConnectionsManager = Tools.getConnectionsManager(classLoader);
                        XposedHelpers.callMethod(mConnectionsManager, "sendRequest", editChatPhotoObj, proxy, 64);

                        Object fileLoaderInstance = Tools.getFileLoaderInstance(classLoader);
                        XposedHelpers.callMethod(fileLoaderInstance, "setDelegate", rawDelegate);

                         LoggerUtil.logI(TAG,"修改头像。。。");

                        LoggerUtil.sendLog4("修改头像成功:"  + " id:" + chatId);

                    } catch (Exception e) {
                    }
                }
            }).start();


        } catch (Exception e) {
        }


    }

    void fileDidFailedUpload(String location, boolean isEncrypted) {
        log(this, "fileDidFailedUpload");
    }

    void fileDidLoaded(String location, File finalFile, int type) {
        log(this, "fileDidLoaded");
    }

    void fileDidFailedLoad(String location, int state) {
        log(this,
                "fileDidFailedLoad");
    }

    void fileLoadProgressChanged(Object operation, String location, long uploadedSize, long totalSize) {
        log(this, "fileLoadProgressChanged");
    }

    void run(Object response, Object error) {

        if (response == null) {
            // Tools.showMsg(context,"修改群头像失败");
            return;
        }

        if (!response.getClass().getName().equals("org.telegram.tgnet.TLRPC$TL_boolTrue")) {
            // Tools.showMsg(context,"修改群头像失败");
            return;
        }

//        Tool.showMsg(context,"修改群头像成功");
        return;
    }
}
