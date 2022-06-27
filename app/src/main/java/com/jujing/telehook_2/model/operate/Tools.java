package com.jujing.telehook_2.model.operate;


import de.robv.android.xposed.XposedHelpers;

public class Tools {
    public  static   int getCurrentUserId(ClassLoader classLoader)
    {
        try {
            Class UserConfig=classLoader.loadClass("org.telegram.messenger.UserConfig");
            int currentAccount = XposedHelpers.getStaticIntField(UserConfig,"selectedAccount");
            return  currentAccount;
        }catch (Exception e)
        {
//            com.hook.tools.Tool.printException(e);
        }
        return 0;
    }
    public static Object getFileLoaderInstance(ClassLoader classLoader)
    {
        try {
            int currentAccount= getCurrentUserId(classLoader);
            Class FileLoader= classLoader.loadClass("org.telegram.messenger.FileLoader");
            Object AccountInstanceIns=XposedHelpers.callStaticMethod(FileLoader,"getInstance",currentAccount);

            return AccountInstanceIns;
        }catch (Exception e)
        {
        }
        return null;
    }
    public static Object getConnectionsManager(ClassLoader classLoader)
    {
        try {
            Object accont= getAccountInstance(classLoader);
            Object ConnectionsManager=XposedHelpers.callMethod(accont,"getConnectionsManager");
            return  ConnectionsManager;
        }catch (Exception e)
        {
        }
        return null;
    }
    //getDatabase
    public  static   Object getMessagesStorage(ClassLoader classLoader)
    {
        try {

            int cid=getCurrentUserId(classLoader);
            Class MessagesStorage=classLoader.loadClass("org.telegram.messenger.MessagesStorage");
            Object MessagesStorageIns= XposedHelpers.callStaticMethod(MessagesStorage,"getInstance",cid);
            return MessagesStorageIns;

        }catch (Exception e)
        {
//            com.hook.tools.Tool.printException(e);
        }
        return 0;
    }

    public  static   Object getDatabase(ClassLoader classLoader)
    {
        try {

            int cid=getCurrentUserId(classLoader);
            Class MessagesStorage=classLoader.loadClass("org.telegram.messenger.MessagesStorage");
            Object MessagesStorageIns= XposedHelpers.callStaticMethod(MessagesStorage,"getInstance",cid);
            Object db=XposedHelpers.callMethod(MessagesStorageIns,"getDatabase");
            return db;

        }catch (Exception e)
        {
//            com.hook.tools.Tool.printException(e);
        }
        return null;
    }

    public static Object getAccountInstance(ClassLoader classLoader)
    {
        try {
            int currentAccount= getCurrentUserId(classLoader);

            Class AccountInstance=classLoader.loadClass("org.telegram.messenger.AccountInstance");
            Object AccountInstanceIns=XposedHelpers.callStaticMethod(AccountInstance,"getInstance",currentAccount);

            return AccountInstanceIns;
        }catch (Exception e)
        {
            
        }
        return null;
    }

    public static Object getUser(ClassLoader classLoader,long uid)
    {
        try {
            int currentId=getCurrentUserId(classLoader);
            Class MessagesController= classLoader.loadClass("org.telegram.messenger.MessagesController");
            Object MessagesControllerIns=XposedHelpers.callStaticMethod(MessagesController,"getInstance",currentId);
            Object user=XposedHelpers.callMethod(MessagesControllerIns,"getUser",uid);
            return user;
        }catch (Exception e)
        {

        }

        return null;
    }

    public static Object getUserFull(ClassLoader classLoader,long uid)
    {
        try {


            Object aaa=getAccountInstance(classLoader);
            Object messagesController=XposedHelpers.callMethod(aaa,"getMessagesController");
            Object user=XposedHelpers.callMethod(messagesController,"getUserFull",uid);
            return user;
        }catch (Exception e)
        {

        }

        return null;
    }

    public static Object getInputPeer(ClassLoader classLoader,long uid)
    {
        try {


            Object aaa=getAccountInstance(classLoader);
            Object messagesController=XposedHelpers.callMethod(aaa,"getMessagesController");
            Object user=XposedHelpers.callMethod(messagesController,"getInputPeer",uid);
            return user;
        }catch (Exception e)
        {

        }

        return null;
    }

    public static Object getSendMessagesHelper(ClassLoader classLoader)
    {

        Object AccountInstanceIns=  getAccountInstance(classLoader);
        return XposedHelpers.callMethod(AccountInstanceIns,"getSendMessagesHelper");
    }

    public static long getClientUserId(ClassLoader classLoader)
    {
        try {
            Class UserConfig= classLoader.loadClass("org.telegram.messenger.UserConfig");
            Object UserConfigObj=XposedHelpers.callStaticMethod(UserConfig,"getInstance",getCurrentUserId(classLoader));
            long id=(long) XposedHelpers.callMethod(UserConfigObj,"getClientUserId");
            return id;
        }catch (Exception e)
        {

        }
        return 0;
    }
}
