package com.jujing.telehook_2.model;



import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public  class XCMethodHookBase extends XC_MethodHook {

    public Context context;
    public ClassLoader classLoader;
    public XCMethodHookBase(Context context)
    {
        this.context=context;
        this.classLoader=context.getClassLoader();
    }



    public void  log(Object thiz,String msg)
    {
        XposedBridge.log(

                thiz.getClass().getSimpleName()+" "+msg);
    }
}
