package com.jujing.telehook_2.util;

import android.util.Log;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * @author kenny
 * @date 2017/11/7.
 */
public class HookUtil {

    public static final String TAG = "XHook_HookUtil";

    public static void frames() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] st = Thread.currentThread().getStackTrace();

        for (int i = 0; i < st.length; i++) {
            sb.append(st[i].toString()).append("\n");
            st[i].isNativeMethod();
        }

//        for (i in st.indices) {
//            sb.append(st[i].toString()).append("\n")
//            st[i].isNativeMethod
//        }

        LoggerUtil.logAll(TAG,"390==================================");
        LoggerUtil.logAll(TAG,sb.toString());
        LoggerUtil.logAll(TAG,"392==================================");
    }

    public static Object getDeclaredField(Object person, String key) throws Exception {
        Field field = person.getClass().getSuperclass().getDeclaredField(key);
        field.setAccessible(true);

        return field.get(person);
    }

    /**
     * hook类的所有构造函数
     *
     * @param clazz            类class
     * @param loadPackageParam
     */
    public static void hookAllConstructors(final String clazz, XC_LoadPackage.LoadPackageParam loadPackageParam) {
//        LogUtil.show(TAG, "hookAllConstructors hook clazz:" + clazz);
        Class voice = XposedHelpers.findClass(clazz, loadPackageParam.classLoader);

        Method[] methods = voice.getDeclaredMethods();

        for (final Method m : methods) {
            if (!Modifier.isAbstract(m.getModifiers())) {
                XposedBridge.hookMethod(m, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        StringBuilder sb = new StringBuilder();
                        if (param.args != null) {
                            for (Object o : param.args) {
                                if (o instanceof String) {
                                    sb.append(o.toString()).append(",");
                                } else {
                                    sb.append(o).append(",");
                                }
                            }
                        }
                        String invoker = getInvoker(m);
                        Log.d(TAG, "hookAllConstructors beforeHookedMethod clazz:" + clazz + ",method:" + m.getName() + ",params:" + sb.toString() + "," + invoker);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.d(TAG, "hookAllConstructors afterHookedMethod clazz:" + clazz + ",method:" + m.getName() + ",result:" + param.getResult());
                    }
                });
            }
        }
    }

    /**
     * hook类的所有方法
     *
     * @param clazz            类class
     * @param
     */
//    public static void hookMethods(final String clazz) {
////        LogUtil.show(TAG, "hookMethods hook clazz:" + clazz);
//        Class voice = XposedHelpers.findClass(clazz, HookMain.classLoader);
//
//        Method[] methods = voice.getDeclaredMethods();
//
//        for (final Method m : methods) {
//            if (!Modifier.isAbstract(m.getModifiers())) {
//                XposedBridge.hookMethod(m, new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        super.beforeHookedMethod(param);
//                        StringBuilder sb = new StringBuilder();
//                        if (param.args != null) {
//                            for (Object o : param.args) {
//                                if (o instanceof String) {
//                                    sb.append(o.toString()).append(",");
//                                } else {
//                                    sb.append(o).append(",");
//                                }
//                            }
//                        }
//                        String invoker = getInvoker(m);
//                        Log.d(TAG, "hookMethods beforeHookedMethod hook clazz:" + clazz + ",method:" + m.getName() + ",params:" + sb.toString() + "," + invoker);
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        super.afterHookedMethod(param);
//                        Log.d(TAG, "hookMethods afterHookedMethod hook clazz:" + clazz + ",method:" + m.getName() + ",result:" + param.getResult());
//                    }
//                });
//            }
//        }
//    }

    /**
     * 获取当前线程堆栈信息
     *
     * @return
     */
    public static String getTraces() {
        Thread t = Thread.currentThread();
        StringBuilder result = new StringBuilder("traces:\n");
        StackTraceElement[] traces = t.getStackTrace();
        if (traces != null && traces.length > 0) {
            for (int i = 0; i < traces.length; i++) {
                StackTraceElement e = traces[i];
                result.append("[class:").append(e.getClassName()).append(",method:").append(e.getMethodName()).append(",line:").append(e.getLineNumber()).append("]\n");
            }
        }
        return result.toString();
    }

    /**
     * Hook微信 Log
     *
     * @param loadPackageParam
     */
    public static void hookLog(ClassLoader loadPackageParam) {
//        LogUtil.show(TAG, "----------Hook WeChat logs----------");

        try {
            Class clazz = findClass("com.tencent.mm.sdk.platformtools.x", loadPackageParam);
            Method[] methods = clazz.getDeclaredMethods();
            for (final Method m : methods) {
                if (!Modifier.isAbstract(m.getModifiers())) {
                    XposedBridge.hookMethod(m, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);

                            if (param.args != null) {
                                StringBuilder sb = new StringBuilder();
                                if (param.args.length == 2) {
                                    sb.append(param.args[0]).append("-->").append(param.args[1]);
                                } else if (param.args.length == 3) {
                                    try {
                                        sb.append(param.args[0]).append("-->").append(String.format(param.args[1].toString().replaceAll("%@","%%@"), (Object[]) param.args[2]));
                                    }catch (Exception e){
//                                        LogUtil.show(TAG,"hookLog Exception:" + e.getMessage());
                                    }
                                }

//                                LogUtil.show("MicroMsg", sb.toString());
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
//            LogUtil.show(TAG, "hookLog Exception:" + e.getMessage());
        }
    }

    /**
     * 该方法的调用路径
     *
     * @param m
     * @return
     */
    public static String getInvoker(Method m) {
        if (m == null) {
            return "";
        }
        String clazz = m.getDeclaringClass().getName();
        String name = m.getName();
        Thread t = Thread.currentThread();
        StringBuilder result = new StringBuilder("invoker:");
        StackTraceElement[] traces = t.getStackTrace();
        if (traces != null && traces.length > 0) {
            for (int i = 0; i < traces.length; i++) {
                StackTraceElement e = traces[i];
                if (clazz.equals(e.getClassName()) && name.equals(e.getMethodName())) {
                    if (i < traces.length - 1) {
                        e = traces[i + 1];
                    }
                    result.append("[class:").append(e.getClassName()).append(",method:").append(e.getMethodName()).append(",line:").append(e.getLineNumber()).append("]\n");

                }
            }
        }
        return result.toString();
    }

    /**
     * 打印Hook的方法参数
     *
     * @param param
     * @return
     */
    public static String printParams(XC_MethodHook.MethodHookParam param) {
        StringBuilder sb = new StringBuilder();
        if (param.args != null) {
            for (Object o : param.args) {
                if (o instanceof String) {
                    sb.append(o.toString()).append(",");
                } else {
                    sb.append(o).append(",");
                }
            }
        }

        return sb.toString();
    }

    /**
     * 打印该对象的所有属性
     *
     * @param object
     */
    public static void printAllField(Object object) {
        try {
            Class cls = object.getClass();
//            Class cls = object.getClass().getSuperclass();
//            Class cls = object.getClass().getSuperclass().getSuperclass(); //这个是为了测试需要 改成父类了
            LoggerUtil.logI(TAG,"cls  237---->"+cls.getName());

            Field[] fields = cls.getDeclaredFields();  //这个也是测试需要
            if (fields != null) {
                for (Field d : fields) {
                    d.setAccessible(true);
                    LoggerUtil.logI(TAG,d.getName() + ":" + d.get(object));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void printAllFieldForSuperclass(Object object) {
        try {
//            Class cls = object.getClass();
            Class cls = object.getClass().getSuperclass();
//            Class cls = object.getClass().getSuperclass().getSuperclass(); //这个是为了测试需要 改成父类了
            LoggerUtil.logAll(TAG,"cls  275---->"+cls.getName());

            Field[] fields = cls.getDeclaredFields();  //这个也是测试需要
            if (fields != null) {
                for (Field d : fields) {
                    d.setAccessible(true);
                    LoggerUtil.logAll(TAG, d.getName() + ":" + d.get(object));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象的某个属性
     *
     * @param object
     * @param fieldName
     * @return
     */
    public static Object getObjectField(Object object, String fieldName) {
        Object result = null;
        try {
            Field field = XposedHelpers.findField(object.getClass(), fieldName);
            field.setAccessible(true);
            result = field.get(object);

            LogTool.e(fieldName + ":" + result);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return result;
    }
}
