package com.jujing.telehook_2.util;

/**
 * Created by Administrator on 2018/11/10.
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorUtil {
    /*
     *线程执行器
     */
    private static final ExecutorService execute = Executors.newCachedThreadPool();
//    private static final Thread thread = new Thread();
//    public static ExecutorService execute = Executors.newFixedThreadPool(3);

    public static void doExecute(Runnable paramRunnable)
    {
        execute.execute(paramRunnable);
//        new Thread(paramRunnable).start();
    }
}
