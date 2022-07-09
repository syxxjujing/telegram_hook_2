package com.jujing.telehook_2.model.operate;

import android.os.Handler;
import android.os.Looper;

import com.jujing.telehook_2.model.UsersAndChats;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.HookUtil;
import com.jujing.telehook_2.util.LoggerUtil;

import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;
import java.util.Locale;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static com.jujing.telehook_2.HookMain.classLoader;
import static com.jujing.telehook_2.model.UsersAndChats.getAccountInstance;

public class SendForwardAction {


    private static final String TAG = "SendForwardAction";

    public static void sendMessagesList(final long toUid,ArrayList sendMsg ) {
        final Object SendMessagesHelper = getSendMessagesHelper(classLoader);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                XposedHelpers.callMethod(SendMessagesHelper, "sendMessage", sendMsg, toUid, false, false, true, 0);
                LoggerUtil.logI(TAG, "发送收藏  263--->" + toUid+"---->"+sendMsg);
            }
        });

    }


    public static ArrayList queryMessagesByMid( String mid) {
        ArrayList msgs = new ArrayList();
        try {

            //5319114872
            //5319114872
//            int index=1;//第几个的

            int currentId = UsersAndChats.getCurrentUserId(classLoader);


//            final long  toUid=Long.parseLong("939531867");
            Object database = getDatabase(classLoader);

            long dialogId = getClientUserId(classLoader);
            LoggerUtil.logI(TAG, "sendForwardMessages  41---->" + dialogId  + "---->" + mid);

            StringBuilder sbSql = new StringBuilder();
            sbSql.append("SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, ");
            sbSql.append("m.ttl, m.mention, m.imp, m.forwards, m.replies_data FROM messages_v2 as m LEFT ");
            sbSql.append(" JOIN randoms_v2 as r ON r.mid = m.mid AND r.uid = m.uid WHERE m.uid = ");
            sbSql.append(String.valueOf(dialogId));
            sbSql.append(" AND m.mid = " + mid);


            String sql = sbSql.toString();

            Object[] sss = new Object[]{sql, new Object[]{}};

            Object cursor = XposedHelpers.callMethod(database, "queryFinalized", sss);

            if (cursor == null) {
                return msgs;
            }

            ArrayList messageIdsToFix = new ArrayList();

            int minId = Integer.MAX_VALUE;
            int maxId = Integer.MIN_VALUE;


            boolean hasNext = (boolean) XposedHelpers.callMethod(cursor, "next");

            while (hasNext) {

                Object data = XposedHelpers.callMethod(cursor, "byteBufferValue", 1);

                if (data == null) {
                    continue;
                }

                int i = (int) XposedHelpers.callMethod(data, "readInt32", false);

                Class Message = classLoader.loadClass("org.telegram.tgnet.TLRPC$Message");
                Object TLRPCMessage = XposedHelpers.callStaticMethod(Message, "TLdeserialize", data, i, false);

                int send_state = (int) XposedHelpers.callMethod(cursor, "intValue", 2);
                XposedHelpers.setIntField(TLRPCMessage, "send_state", send_state);

                long fullMid = (long) XposedHelpers.callMethod(cursor, "longValue", 3);
                XposedHelpers.setIntField(TLRPCMessage, "id", (int) fullMid);


                if ((fullMid & 0xffffffff00000000L) == 0xffffffff00000000L && send_state > 0) {
                    if (messageIdsToFix == null) {
                        messageIdsToFix = new ArrayList<>();
                    }
                    messageIdsToFix.add(fullMid);
                }


                if (fullMid > 0 && send_state != 0 && send_state != 3) {

                    XposedHelpers.setIntField(TLRPCMessage, "send_state", 0);
                }
                if (dialogId == currentId) {

                    XposedHelpers.setBooleanField(TLRPCMessage, "out", true);
                }


                XposedHelpers.callMethod(TLRPCMessage, "readAttachPath", data, currentId);
                XposedHelpers.callMethod(data, "reuse");

                int unreadFlag = (int) XposedHelpers.callMethod(cursor, "intValue", 0);
                Class MessageObject = classLoader.loadClass("org.telegram.messenger.MessageObject");
                XposedHelpers.callStaticMethod(MessageObject, "setUnreadFlags", TLRPCMessage, unreadFlag);


                if (fullMid > 0) {
                    minId = (int) Math.min(fullMid, minId);
                    maxId = (int) Math.max(fullMid, maxId);
                }

                int date = (int) XposedHelpers.callMethod(cursor, "intValue", 4);
                XposedHelpers.setIntField(TLRPCMessage, "date", date);
                XposedHelpers.setLongField(TLRPCMessage, "dialog_id", dialogId);

                int flags = XposedHelpers.getIntField(TLRPCMessage, "flags");

                int MESSAGE_FLAG_HAS_VIEWS = 0x00000400;

                if ((flags & MESSAGE_FLAG_HAS_VIEWS) != 0) {

                    int views = (int) XposedHelpers.callMethod(cursor, "intValue", 7);
                    int forwards = (int) XposedHelpers.callMethod(cursor, "intValue", 11);
                    XposedHelpers.setIntField(TLRPCMessage, "views", views);
                    XposedHelpers.setIntField(TLRPCMessage, "forwards", forwards);

                }


                Object repliesData = XposedHelpers.callMethod(cursor, "byteBufferValue", 12);


                if (repliesData != null) {
                    int repliesDataV = (int) XposedHelpers.callMethod(repliesData, "readInt32", false);
                    Class MessageReplies = classLoader.loadClass("org.telegram.tgnet.TLRPC$MessageReplies");
                    Object replies = XposedHelpers.callStaticMethod(MessageReplies, "TLdeserialize", repliesDataV, false);


                    if (replies != null) {

                        XposedHelpers.setObjectField(TLRPCMessage, "replies", replies);
                    }

                    XposedHelpers.callMethod(repliesData, "reuse");
                }

                Class DialogObject = classLoader.loadClass("org.telegram.messenger.DialogObject");

                boolean isEncryptedDialog = (boolean) XposedHelpers.callStaticMethod(DialogObject, "isEncryptedDialog", dialogId);
                int ttl = XposedHelpers.getIntField(TLRPCMessage, "ttl");
                if (!isEncryptedDialog && ttl == 0) {
                    //   message.ttl = cursor.intValue(8);
                    int ttl0 = (int) XposedHelpers.callMethod(cursor, "intValue", 8);
                    XposedHelpers.setIntField(TLRPCMessage, "ttl", ttl0);

                }

                int v9 = (int) XposedHelpers.callMethod(cursor, "intValue", 9);

                if (v9 != 0) {

                    XposedHelpers.setBooleanField(TLRPCMessage, "mentioned", true);
                }

                int flags10 = (int) XposedHelpers.callMethod(cursor, "intValue", 10);
                if ((flags10 & 1) != 0) {

                    XposedHelpers.setIntField(TLRPCMessage, "stickerVerified", 0);
                } else if ((flags & 2) != 0) {

                    XposedHelpers.setIntField(TLRPCMessage, "stickerVerified", 2);
                }


                Object reply_to = XposedHelpers.getObjectField(TLRPCMessage, "reply_to");

                if (reply_to != null) {
                    int reply_to_msg_id = XposedHelpers.getIntField(reply_to, "reply_to_msg_id");
                    long reply_to_random_id = XposedHelpers.getLongField(reply_to, "reply_to_random_id");

                    if ((reply_to_msg_id != 0 || reply_to_random_id != 0)) {
                        boolean isNull = (boolean) XposedHelpers.callMethod(cursor, "isNull", 6);
                        if (!isNull) {
                            data = XposedHelpers.callMethod(cursor, "byteBufferValue", 6);
                            if (data != null) {
                                int k = (int) XposedHelpers.callMethod(data, "readInt32", false);

                                Object oo = XposedHelpers.callStaticMethod(Message, "TLdeserialize", data, k, false);
                                XposedHelpers.setObjectField(TLRPCMessage, "replyMessage", oo);
                                Object replyMessageS = XposedHelpers.getObjectField(TLRPCMessage, "replyMessage");
                                XposedHelpers.callMethod(replyMessageS, "readAttachPath", data, currentId);
                                XposedHelpers.callMethod(data, "reuse");
                            }
                        }
                    }
                }


                boolean isNUll5 = (boolean) XposedHelpers.callMethod(cursor, "isNull", 5);

                if (isEncryptedDialog && !isNUll5) {
                    long random_id = (long) XposedHelpers.callMethod(cursor, "longValue", 5);
                    XposedHelpers.setLongField(TLRPCMessage, "random_id", random_id);
                }


                boolean isSecretMedia = (boolean) XposedHelpers.callStaticMethod(MessageObject, "isSecretMedia", TLRPCMessage);

                if (isSecretMedia) {
                    long ddd = (long) XposedHelpers.callStaticMethod(MessageObject, "getDialogId", TLRPCMessage);
                    String sql2 = String.format(Locale.US, "SELECT date FROM enc_tasks_v4 WHERE mid = %d AND uid = %d AND media = 1", fullMid, ddd);

                    Object[] aaaa = new Object[]{sql2, new Object[]{}};
                    Object cursor2 = XposedHelpers.callMethod(database, "queryFinalized", aaaa);
                    boolean ss = (boolean) XposedHelpers.callMethod(cursor2, "next");
                    if (ss) {
                        int zz = (int) XposedHelpers.callMethod(cursor2, "intValue");
                        XposedHelpers.setIntField(TLRPCMessage, "destroyTime", zz);
                    }

                    XposedHelpers.callMethod(cursor2, "dispose");
                }


                Object messageObject = XposedHelpers.newInstance(MessageObject, currentId, TLRPCMessage, false, false);

                msgs.add(messageObject);
                Object messageText = XposedHelpers.getObjectField(msgs.get(0), "messageText");
                LoggerUtil.logI(TAG, "messageText  251---->" + messageText  + "--->" + fullMid);

                hasNext = (boolean) XposedHelpers.callMethod(cursor, "next");
            }

//            final Object SendMessagesHelper = getSendMessagesHelper(classLoader);


            if (msgs.isEmpty()) {
                LoggerUtil.logI(TAG, "没有收藏内容- 260--->"  + mid);
                return msgs;
            }

            LoggerUtil.logI(TAG, "msgs size  264:" + msgs.size());

//            final ArrayList sendMsg = new ArrayList();
//            sendMsg.add(msgs.get(0));
//            HookUtil.printAllField(msgs.get(0));


//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
//                    XposedHelpers.callMethod(SendMessagesHelper, "sendMessage", sendMsg, toUid, false, false, true, 0);
//                    LoggerUtil.logI(TAG, "发送收藏  263--->"+mid);
//                }
//            });

        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  268 ---->" + CrashHandler.getInstance().printCrash(e));
        }
        return msgs;
    }

    public static void sendForwardMessagesByMid(final long toUid, String mid) {
        try {

            //5319114872
            //5319114872
//            int index=1;//第几个的

            int currentId = UsersAndChats.getCurrentUserId(classLoader);


//            final long  toUid=Long.parseLong("939531867");
            Object database = getDatabase(classLoader);

            long dialogId = getClientUserId(classLoader);
            LoggerUtil.logI(TAG, "sendForwardMessages  41---->" + dialogId + "--->" + toUid + "---->" + mid);

            StringBuilder sbSql = new StringBuilder();
            sbSql.append("SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, ");
            sbSql.append("m.ttl, m.mention, m.imp, m.forwards, m.replies_data FROM messages_v2 as m LEFT ");
            sbSql.append(" JOIN randoms_v2 as r ON r.mid = m.mid AND r.uid = m.uid WHERE m.uid = ");
            sbSql.append(String.valueOf(dialogId));
            sbSql.append(" AND m.mid = " + mid);


            String sql = sbSql.toString();

            Object[] sss = new Object[]{sql, new Object[]{}};

            Object cursor = XposedHelpers.callMethod(database, "queryFinalized", sss);

            if (cursor == null) {
                return;
            }

            ArrayList messageIdsToFix = new ArrayList();

            int minId = Integer.MAX_VALUE;
            int maxId = Integer.MIN_VALUE;

            ArrayList msgs = new ArrayList();


            boolean hasNext = (boolean) XposedHelpers.callMethod(cursor, "next");

            while (hasNext) {

                Object data = XposedHelpers.callMethod(cursor, "byteBufferValue", 1);

                if (data == null) {
                    continue;
                }

                int i = (int) XposedHelpers.callMethod(data, "readInt32", false);

                Class Message = classLoader.loadClass("org.telegram.tgnet.TLRPC$Message");
                Object TLRPCMessage = XposedHelpers.callStaticMethod(Message, "TLdeserialize", data, i, false);

                int send_state = (int) XposedHelpers.callMethod(cursor, "intValue", 2);
                XposedHelpers.setIntField(TLRPCMessage, "send_state", send_state);

                long fullMid = (long) XposedHelpers.callMethod(cursor, "longValue", 3);
                XposedHelpers.setIntField(TLRPCMessage, "id", (int) fullMid);


                if ((fullMid & 0xffffffff00000000L) == 0xffffffff00000000L && send_state > 0) {
                    if (messageIdsToFix == null) {
                        messageIdsToFix = new ArrayList<>();
                    }
                    messageIdsToFix.add(fullMid);
                }


                if (fullMid > 0 && send_state != 0 && send_state != 3) {

                    XposedHelpers.setIntField(TLRPCMessage, "send_state", 0);
                }
                if (dialogId == currentId) {

                    XposedHelpers.setBooleanField(TLRPCMessage, "out", true);
                }


                XposedHelpers.callMethod(TLRPCMessage, "readAttachPath", data, currentId);
                XposedHelpers.callMethod(data, "reuse");

                int unreadFlag = (int) XposedHelpers.callMethod(cursor, "intValue", 0);
                Class MessageObject = classLoader.loadClass("org.telegram.messenger.MessageObject");
                XposedHelpers.callStaticMethod(MessageObject, "setUnreadFlags", TLRPCMessage, unreadFlag);


                if (fullMid > 0) {
                    minId = (int) Math.min(fullMid, minId);
                    maxId = (int) Math.max(fullMid, maxId);
                }

                int date = (int) XposedHelpers.callMethod(cursor, "intValue", 4);
                XposedHelpers.setIntField(TLRPCMessage, "date", date);
                XposedHelpers.setLongField(TLRPCMessage, "dialog_id", dialogId);

                int flags = XposedHelpers.getIntField(TLRPCMessage, "flags");

                int MESSAGE_FLAG_HAS_VIEWS = 0x00000400;

                if ((flags & MESSAGE_FLAG_HAS_VIEWS) != 0) {

                    int views = (int) XposedHelpers.callMethod(cursor, "intValue", 7);
                    int forwards = (int) XposedHelpers.callMethod(cursor, "intValue", 11);
                    XposedHelpers.setIntField(TLRPCMessage, "views", views);
                    XposedHelpers.setIntField(TLRPCMessage, "forwards", forwards);

                }


                Object repliesData = XposedHelpers.callMethod(cursor, "byteBufferValue", 12);


                if (repliesData != null) {
                    int repliesDataV = (int) XposedHelpers.callMethod(repliesData, "readInt32", false);
                    Class MessageReplies = classLoader.loadClass("org.telegram.tgnet.TLRPC$MessageReplies");
                    Object replies = XposedHelpers.callStaticMethod(MessageReplies, "TLdeserialize", repliesDataV, false);


                    if (replies != null) {

                        XposedHelpers.setObjectField(TLRPCMessage, "replies", replies);
                    }

                    XposedHelpers.callMethod(repliesData, "reuse");
                }

                Class DialogObject = classLoader.loadClass("org.telegram.messenger.DialogObject");

                boolean isEncryptedDialog = (boolean) XposedHelpers.callStaticMethod(DialogObject, "isEncryptedDialog", dialogId);
                int ttl = XposedHelpers.getIntField(TLRPCMessage, "ttl");
                if (!isEncryptedDialog && ttl == 0) {
                    //   message.ttl = cursor.intValue(8);
                    int ttl0 = (int) XposedHelpers.callMethod(cursor, "intValue", 8);
                    XposedHelpers.setIntField(TLRPCMessage, "ttl", ttl0);

                }

                int v9 = (int) XposedHelpers.callMethod(cursor, "intValue", 9);

                if (v9 != 0) {

                    XposedHelpers.setBooleanField(TLRPCMessage, "mentioned", true);
                }

                int flags10 = (int) XposedHelpers.callMethod(cursor, "intValue", 10);
                if ((flags10 & 1) != 0) {

                    XposedHelpers.setIntField(TLRPCMessage, "stickerVerified", 0);
                } else if ((flags & 2) != 0) {

                    XposedHelpers.setIntField(TLRPCMessage, "stickerVerified", 2);
                }


                Object reply_to = XposedHelpers.getObjectField(TLRPCMessage, "reply_to");

                if (reply_to != null) {
                    int reply_to_msg_id = XposedHelpers.getIntField(reply_to, "reply_to_msg_id");
                    long reply_to_random_id = XposedHelpers.getLongField(reply_to, "reply_to_random_id");

                    if ((reply_to_msg_id != 0 || reply_to_random_id != 0)) {
                        boolean isNull = (boolean) XposedHelpers.callMethod(cursor, "isNull", 6);
                        if (!isNull) {
                            data = XposedHelpers.callMethod(cursor, "byteBufferValue", 6);
                            if (data != null) {
                                int k = (int) XposedHelpers.callMethod(data, "readInt32", false);

                                Object oo = XposedHelpers.callStaticMethod(Message, "TLdeserialize", data, k, false);
                                XposedHelpers.setObjectField(TLRPCMessage, "replyMessage", oo);
                                Object replyMessageS = XposedHelpers.getObjectField(TLRPCMessage, "replyMessage");
                                XposedHelpers.callMethod(replyMessageS, "readAttachPath", data, currentId);
                                XposedHelpers.callMethod(data, "reuse");
                            }
                        }
                    }
                }


                boolean isNUll5 = (boolean) XposedHelpers.callMethod(cursor, "isNull", 5);

                if (isEncryptedDialog && !isNUll5) {
                    long random_id = (long) XposedHelpers.callMethod(cursor, "longValue", 5);
                    XposedHelpers.setLongField(TLRPCMessage, "random_id", random_id);
                }


                boolean isSecretMedia = (boolean) XposedHelpers.callStaticMethod(MessageObject, "isSecretMedia", TLRPCMessage);

                if (isSecretMedia) {
                    long ddd = (long) XposedHelpers.callStaticMethod(MessageObject, "getDialogId", TLRPCMessage);
                    String sql2 = String.format(Locale.US, "SELECT date FROM enc_tasks_v4 WHERE mid = %d AND uid = %d AND media = 1", fullMid, ddd);

                    Object[] aaaa = new Object[]{sql2, new Object[]{}};
                    Object cursor2 = XposedHelpers.callMethod(database, "queryFinalized", aaaa);
                    boolean ss = (boolean) XposedHelpers.callMethod(cursor2, "next");
                    if (ss) {
                        int zz = (int) XposedHelpers.callMethod(cursor2, "intValue");
                        XposedHelpers.setIntField(TLRPCMessage, "destroyTime", zz);
                    }

                    XposedHelpers.callMethod(cursor2, "dispose");
                }


                Object messageObject = XposedHelpers.newInstance(MessageObject, currentId, TLRPCMessage, false, false);

                msgs.add(messageObject);
                Object messageText = XposedHelpers.getObjectField(msgs.get(0), "messageText");
                LoggerUtil.logI(TAG, "messageText  239---->" + messageText + "--->" + toUid + "--->" + fullMid);

                hasNext = (boolean) XposedHelpers.callMethod(cursor, "next");
            }

            final Object SendMessagesHelper = getSendMessagesHelper(classLoader);


            if (msgs.isEmpty()) {
                LoggerUtil.logI(TAG, "没有收藏内容- 248--->" + "--->" + toUid);
                return;
            }

//            LoggerUtil.logI(TAG, "msgs size  252:" + msgs.size());

            final ArrayList sendMsg = new ArrayList();
            sendMsg.add(msgs.get(0));
//            HookUtil.printAllField(msgs.get(0));


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    XposedHelpers.callMethod(SendMessagesHelper, "sendMessage", sendMsg, toUid, false, false, true, 0);
                    LoggerUtil.logI(TAG, "发送收藏  263--->" + mid+"---->"+toUid);
                }
            });

        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  268 ---->" + CrashHandler.getInstance().printCrash(e));
        }
    }


    public static void sendForwardMessages(final long toUid, int index) {
        try {

            //5319114872
            //5319114872
//            int index=1;//第几个的

            int currentId = UsersAndChats.getCurrentUserId(classLoader);


//            final long  toUid=Long.parseLong("939531867");
            Object database = getDatabase(classLoader);

            long dialogId = getClientUserId(classLoader);
            LoggerUtil.logI(TAG, "sendForwardMessages  40---->" + dialogId + "--->" + toUid + "---->" + index);

            StringBuilder sbSql = new StringBuilder();
            sbSql.append("SELECT m.read_state, m.data, m.send_state, m.mid, m.date, r.random_id, m.replydata, m.media, ");
            sbSql.append("m.ttl, m.mention, m.imp, m.forwards, m.replies_data FROM messages_v2 as m LEFT ");
            sbSql.append(" JOIN randoms_v2 as r ON r.mid = m.mid AND r.uid = m.uid WHERE m.uid = ");
            sbSql.append(String.valueOf(dialogId));
//            sbSql.append(" AND (m.mid >= 1 OR m.mid < 0) ORDER BY m.date ASC, m.mid ASC LIMIT ");//DESC
            sbSql.append(" AND (m.mid >= 1 OR m.mid < 0) ORDER BY m.date DESC, m.mid DESC LIMIT ");//DESC
            sbSql.append(String.valueOf(index - 1));
//            sbSql.append(String.valueOf(index ));
            sbSql.append(",");
            sbSql.append(String.valueOf(index));
//            sbSql.append(String.valueOf(index+1));


            String sql = sbSql.toString();

            Object[] sss = new Object[]{sql, new Object[]{}};

            Object cursor = XposedHelpers.callMethod(database, "queryFinalized", sss);

            if (cursor == null) {
                return;
            }

            ArrayList messageIdsToFix = new ArrayList();

            int minId = Integer.MAX_VALUE;
            int maxId = Integer.MIN_VALUE;

            ArrayList msgs = new ArrayList();


            boolean hasNext = (boolean) XposedHelpers.callMethod(cursor, "next");

            while (hasNext) {

                Object data = XposedHelpers.callMethod(cursor, "byteBufferValue", 1);

                if (data == null) {
                    continue;
                }

                int i = (int) XposedHelpers.callMethod(data, "readInt32", false);

                Class Message = classLoader.loadClass("org.telegram.tgnet.TLRPC$Message");
                Object TLRPCMessage = XposedHelpers.callStaticMethod(Message, "TLdeserialize", data, i, false);

                int send_state = (int) XposedHelpers.callMethod(cursor, "intValue", 2);
                XposedHelpers.setIntField(TLRPCMessage, "send_state", send_state);

                long fullMid = (long) XposedHelpers.callMethod(cursor, "longValue", 3);
                XposedHelpers.setIntField(TLRPCMessage, "id", (int) fullMid);
                LoggerUtil.logI(TAG, "fullMid  342---->" + fullMid);


                if ((fullMid & 0xffffffff00000000L) == 0xffffffff00000000L && send_state > 0) {
                    if (messageIdsToFix == null) {
                        messageIdsToFix = new ArrayList<>();
                    }
                    messageIdsToFix.add(fullMid);
                }


                if (fullMid > 0 && send_state != 0 && send_state != 3) {

                    XposedHelpers.setIntField(TLRPCMessage, "send_state", 0);
                }
                if (dialogId == currentId) {

                    XposedHelpers.setBooleanField(TLRPCMessage, "out", true);
                }


                XposedHelpers.callMethod(TLRPCMessage, "readAttachPath", data, currentId);
                XposedHelpers.callMethod(data, "reuse");

                int unreadFlag = (int) XposedHelpers.callMethod(cursor, "intValue", 0);
                Class MessageObject = classLoader.loadClass("org.telegram.messenger.MessageObject");
                XposedHelpers.callStaticMethod(MessageObject, "setUnreadFlags", TLRPCMessage, unreadFlag);


                if (fullMid > 0) {
                    minId = (int) Math.min(fullMid, minId);
                    maxId = (int) Math.max(fullMid, maxId);
                }

                int date = (int) XposedHelpers.callMethod(cursor, "intValue", 4);
                XposedHelpers.setIntField(TLRPCMessage, "date", date);
                XposedHelpers.setLongField(TLRPCMessage, "dialog_id", dialogId);

                int flags = XposedHelpers.getIntField(TLRPCMessage, "flags");

                int MESSAGE_FLAG_HAS_VIEWS = 0x00000400;

                if ((flags & MESSAGE_FLAG_HAS_VIEWS) != 0) {

                    int views = (int) XposedHelpers.callMethod(cursor, "intValue", 7);
                    int forwards = (int) XposedHelpers.callMethod(cursor, "intValue", 11);
                    XposedHelpers.setIntField(TLRPCMessage, "views", views);
                    XposedHelpers.setIntField(TLRPCMessage, "forwards", forwards);

                }


                Object repliesData = XposedHelpers.callMethod(cursor, "byteBufferValue", 12);


                if (repliesData != null) {
                    int repliesDataV = (int) XposedHelpers.callMethod(repliesData, "readInt32", false);
                    Class MessageReplies = classLoader.loadClass("org.telegram.tgnet.TLRPC$MessageReplies");
                    Object replies = XposedHelpers.callStaticMethod(MessageReplies, "TLdeserialize", repliesDataV, false);


                    if (replies != null) {

                        XposedHelpers.setObjectField(TLRPCMessage, "replies", replies);
                    }

                    XposedHelpers.callMethod(repliesData, "reuse");
                }

                Class DialogObject = classLoader.loadClass("org.telegram.messenger.DialogObject");

                boolean isEncryptedDialog = (boolean) XposedHelpers.callStaticMethod(DialogObject, "isEncryptedDialog", dialogId);
                int ttl = XposedHelpers.getIntField(TLRPCMessage, "ttl");
                if (!isEncryptedDialog && ttl == 0) {
                    //   message.ttl = cursor.intValue(8);
                    int ttl0 = (int) XposedHelpers.callMethod(cursor, "intValue", 8);
                    XposedHelpers.setIntField(TLRPCMessage, "ttl", ttl0);

                }

                int v9 = (int) XposedHelpers.callMethod(cursor, "intValue", 9);

                if (v9 != 0) {

                    XposedHelpers.setBooleanField(TLRPCMessage, "mentioned", true);
                }

                int flags10 = (int) XposedHelpers.callMethod(cursor, "intValue", 10);
                if ((flags10 & 1) != 0) {

                    XposedHelpers.setIntField(TLRPCMessage, "stickerVerified", 0);
                } else if ((flags & 2) != 0) {

                    XposedHelpers.setIntField(TLRPCMessage, "stickerVerified", 2);
                }


                Object reply_to = XposedHelpers.getObjectField(TLRPCMessage, "reply_to");

                if (reply_to != null) {
                    int reply_to_msg_id = XposedHelpers.getIntField(reply_to, "reply_to_msg_id");
                    long reply_to_random_id = XposedHelpers.getLongField(reply_to, "reply_to_random_id");

                    if ((reply_to_msg_id != 0 || reply_to_random_id != 0)) {
                        boolean isNull = (boolean) XposedHelpers.callMethod(cursor, "isNull", 6);
                        if (!isNull) {
                            data = XposedHelpers.callMethod(cursor, "byteBufferValue", 6);
                            if (data != null) {
                                int k = (int) XposedHelpers.callMethod(data, "readInt32", false);

                                Object oo = XposedHelpers.callStaticMethod(Message, "TLdeserialize", data, k, false);
                                XposedHelpers.setObjectField(TLRPCMessage, "replyMessage", oo);
                                Object replyMessageS = XposedHelpers.getObjectField(TLRPCMessage, "replyMessage");
                                XposedHelpers.callMethod(replyMessageS, "readAttachPath", data, currentId);
                                XposedHelpers.callMethod(data, "reuse");
                            }
                        }
                    }
                }


                boolean isNUll5 = (boolean) XposedHelpers.callMethod(cursor, "isNull", 5);

                if (isEncryptedDialog && !isNUll5) {
                    long random_id = (long) XposedHelpers.callMethod(cursor, "longValue", 5);
                    XposedHelpers.setLongField(TLRPCMessage, "random_id", random_id);
                }


                boolean isSecretMedia = (boolean) XposedHelpers.callStaticMethod(MessageObject, "isSecretMedia", TLRPCMessage);

                if (isSecretMedia) {
                    long ddd = (long) XposedHelpers.callStaticMethod(MessageObject, "getDialogId", TLRPCMessage);
                    String sql2 = String.format(Locale.US, "SELECT date FROM enc_tasks_v4 WHERE mid = %d AND uid = %d AND media = 1", fullMid, ddd);

                    Object[] aaaa = new Object[]{sql2, new Object[]{}};
                    Object cursor2 = XposedHelpers.callMethod(database, "queryFinalized", aaaa);
                    boolean ss = (boolean) XposedHelpers.callMethod(cursor2, "next");
                    if (ss) {
                        int zz = (int) XposedHelpers.callMethod(cursor2, "intValue");
                        XposedHelpers.setIntField(TLRPCMessage, "destroyTime", zz);
                    }

                    XposedHelpers.callMethod(cursor2, "dispose");
                }


                Object messageObject = XposedHelpers.newInstance(MessageObject, currentId, TLRPCMessage, false, false);

                msgs.add(messageObject);
                Object messageText = XposedHelpers.getObjectField(msgs.get(0), "messageText");
                LoggerUtil.logI(TAG, "messageText  247---->" + messageText + "--->" + toUid + "--->" + fullMid);

                hasNext = (boolean) XposedHelpers.callMethod(cursor, "next");
            }

            final Object SendMessagesHelper = getSendMessagesHelper(classLoader);


            if (msgs.isEmpty()) {
                LoggerUtil.logI(TAG, "没有收藏内容---->" + index + "--->" + toUid);
                return;
            }

            LoggerUtil.logI(TAG, "msgs size:" + msgs.size());

            final ArrayList sendMsg = new ArrayList();
            sendMsg.add(msgs.get(0));
//            HookUtil.printAllField(msgs.get(0));


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    XposedHelpers.callMethod(SendMessagesHelper, "sendMessage", sendMsg, toUid, false, false, true, 0);
                    LoggerUtil.logI(TAG, "发送收藏");
                }
            });

        } catch (Exception e) {
            LoggerUtil.logI(TAG, "eee  283 ---->" + CrashHandler.getInstance().printCrash(e));
        }
    }

    public static Object getDatabase(ClassLoader classLoader) {
        try {

            int cid = UsersAndChats.getCurrentUserId(classLoader);
            Class MessagesStorage = classLoader.loadClass("org.telegram.messenger.MessagesStorage");
            Object MessagesStorageIns = XposedHelpers.callStaticMethod(MessagesStorage, "getInstance", cid);
            Object db = XposedHelpers.callMethod(MessagesStorageIns, "getDatabase");
            return db;

        } catch (Exception e) {
        }
        return null;
    }

    public static long getClientUserId(ClassLoader classLoader) {
        try {
            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");
            Object UserConfigObj = XposedHelpers.callStaticMethod(UserConfig, "getInstance", UsersAndChats.getCurrentUserId(classLoader));
            long id = (long) XposedHelpers.callMethod(UserConfigObj, "getClientUserId");
            return id;
        } catch (Exception e) {
        }
        return 0;
    }

    public static Object getSendMessagesHelper(ClassLoader classLoader) {

        Object AccountInstanceIns = getAccountInstance(classLoader);
        return XposedHelpers.callMethod(AccountInstanceIns, "getSendMessagesHelper");
    }
}
