package com.jujing.telehook_2.model.operate;

import static com.jujing.telehook_2.HookMain.classLoader;

import android.os.SystemClock;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.R;
import com.jujing.telehook_2.model.UsersAndChats;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.ExecutorUtil;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;

public class GroupAddMemberAction {

    private static final String TAG = "GroupAddMemberAction";
    public static void handle(){

        ExecutorUtil.doExecute(new Runnable() {
            @Override
            public void run() {
                try {

                    LoggerUtil.sendLog5("开始拉人进群");
                    long chatId = Long.parseLong(WriteFileUtil.read(Global.GROUP_ADD_MEMBER_ID));
                    LoggerUtil.logI(TAG, "chatId  24========== "+chatId);
                    int num = Integer.parseInt(WriteFileUtil.read(Global.GROUP_ADD_MEMBER_NUM));
                    LoggerUtil.logI(TAG, "num  26========== "+num);
                    if (num==-1){
                        LoggerUtil.sendLog5("准备拉通讯录全部人进群,群id："+chatId);
                        List<Long> userIdList = getUserIdList();
                        for (int i = 0; i < userIdList.size(); i++) {
                            long id = userIdList.get(i);
                            LoggerUtil.logI(TAG, "id  32========== "+id+"--->"+id);
                            JoinToGroupAction.addUserToChat(chatId,id);
                            LoggerUtil.sendLog5("已拉第："+(i+1)+"人，id:"+id);
//                            SystemClock.sleep(1000);
                        }
                    }else {
                        LoggerUtil.sendLog5("准备拉通讯录里"+num+"个人进群,群id："+chatId);
                        List<Long> userIdList = getUserIdList();
                        for (int i = 0; i <userIdList.size(); i++) {
                            long id = userIdList.get(i);
                            LoggerUtil.logI(TAG, "id  32========== "+id+"--->"+id);
                            if (i+1==num){
                                break;
                            }
                            JoinToGroupAction.addUserToChat(chatId,id);
                            LoggerUtil.sendLog5("已拉第："+(i+1)+"人，id:"+id);
                        }

                    }
                } catch (Exception e) {
                    LoggerUtil.logI(TAG,"eee 24---->"+CrashHandler.getInstance().printCrash(e));
                    LoggerUtil.sendLog5("出错："+e);
                }
            }
        });

    }

    public static List<Long>  getUserIdList() {
        List<Long> userIdList  = new ArrayList<>();
        try {

            LoggerUtil.logI(TAG, "handle  19========== ");
            Class UserConfig = classLoader.loadClass("org.telegram.messenger.UserConfig");
            int currentAccount = XposedHelpers.getStaticIntField(UserConfig, "selectedAccount");


            Class ContactsController = classLoader.loadClass("org.telegram.messenger.ContactsController");

            Object ContactsControllerIns = XposedHelpers.callStaticMethod(ContactsController, "getInstance", currentAccount);
//            HashMap contactsByPhone = (HashMap) XposedHelpers.getObjectField(ContactsControllerIns, "contactsByPhone");
            List contactsByPhone = (List) XposedHelpers.getObjectField(ContactsControllerIns, "contacts");
//            List contactsByPhone = (List) XposedHelpers.getObjectField(ContactsControllerIns, "phoneBookContacts");

            LoggerUtil.logI(TAG, "联系人总共  89:" + contactsByPhone.size() + "人");
            Object currentUser = UsersAndChats.getCurrentUser();
            long myId = XposedHelpers.getLongField(currentUser, "id");
            for (int j = 0; j < contactsByPhone.size(); j++) {
                long user_id = XposedHelpers.getLongField(contactsByPhone.get(j), "user_id");
                if (myId==user_id){
                    continue;
                }
                userIdList.add(user_id);

                Object user = UsersAndChats.getUser(user_id);
                Object username = XposedHelpers.getObjectField(user, "username");
                Object phone = XposedHelpers.getObjectField(user, "phone");
                Object first_name = XposedHelpers.getObjectField(user, "first_name");
                LoggerUtil.logI(TAG, "user_id:" + user_id + "---->" + username + "---->" + phone + "---->" + first_name + "---->" + j);

//                JoinToGroupAction.addUserToChat(647320018,user_id);
            }


        } catch (Exception e) {
            LoggerUtil.logI(TAG, "e   97------->" + CrashHandler.getInstance().printCrash(e));

        }
        return userIdList;

    }
}
