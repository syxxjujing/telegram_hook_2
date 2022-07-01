package com.jujing.telehook_2.model.operate;

import static com.jujing.telehook_2.HookMain.classLoader;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.jujing.telehook_2.HookMain;
import com.jujing.telehook_2.hook.HookActivity;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.LoggerUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

import de.robv.android.xposed.XposedHelpers;

public class GetNearbyDataAction {


    private static final String TAG = "GetNearbyDataAction";

    public static void getNearByData(){
        try {
            Object accountInstance = Tools.getAccountInstance(HookMain.classLoader);
            Object getLocationController = XposedHelpers.callMethod(accountInstance, "getLocationController");
//            Object location = XposedHelpers.callMethod(getLocationController, "getLastKnownLocation");
//            LoggerUtil.logI(TAG,"location  15---->"+location);
            Class<?> TL_contacts_getLocated = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_contacts_getLocated");
            Object req = XposedHelpers.newInstance(TL_contacts_getLocated);
            Class<?> TL_inputGeoPoint = classLoader.loadClass("org.telegram.tgnet.TLRPC$TL_inputGeoPoint");
            Object tL_inputGeoPoint = XposedHelpers.newInstance(TL_inputGeoPoint);
            XposedHelpers.setObjectField(req,"geo_point",tL_inputGeoPoint);

           LocationManager locationManager = (LocationManager) HookActivity.baseActivity.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LoggerUtil.logI(TAG,"location  40---->"+location);
            if (location==null){
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            LoggerUtil.logI(TAG,"location  44---->"+location);
            double lat = (double) XposedHelpers.callMethod(location, "getLatitude");
            double lon = (double) XposedHelpers.callMethod(location, "getLongitude");
            LoggerUtil.logI(TAG,"lat & lon  29---->"+lat+"--->"+lon);
            Object geo_point = XposedHelpers.getObjectField(req, "geo_point");
            XposedHelpers.setDoubleField(geo_point,"lat",lat);
            XposedHelpers.setDoubleField(geo_point,"_long",lon);

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
                        LoggerUtil.logI(TAG, "getNearByData   61:" + text + ":" + code );
                        return;
                    }
                    if (response == null) {
                        LoggerUtil.logI(TAG, "请求出错:response==null----》" );
                        return;
                    }

                    ArrayList users = (ArrayList) XposedHelpers.getObjectField(response, "users");
                    if (users == null || users.isEmpty()) {
                        LoggerUtil.logI(TAG, "为空:users==null||users.isEmpty---->" );
                    }

                    ArrayList chats = (ArrayList) XposedHelpers.getObjectField(response, "chats");
                    LoggerUtil.logI(TAG,"users & chats 75---->"+users.size()+"---->"+chats.size());



                }
            });
            XposedHelpers.callMethod(ConnectionsManagerIns, "sendRequest", req, cb);


        } catch (Exception e) {
            LoggerUtil.logI(TAG,"eee  25---->"+ CrashHandler.getInstance().printCrash(e));
        }
    }
}
