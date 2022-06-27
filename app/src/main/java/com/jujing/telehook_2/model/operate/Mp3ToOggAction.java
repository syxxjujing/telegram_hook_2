package com.jujing.telehook_2.model.operate;


public class Mp3ToOggAction {

    private static final String TAG = "Mp3ToOggAction";

//    public static String mp3ToOgg(String path) {
////        String path = "/sdcard/1xreply/2.mp3";
//        File srcFile = new File(path);
//        String fileName = srcFile.getName();
//        fileName = fileName.split(".mp3")[0];
//        fileName = fileName + ".ogg";
//        File dstFile = new File(srcFile.getParent(), fileName);
//
//        if (dstFile.exists()) {
//            dstFile.delete();
//        }
//
//        String[] commands = {"-i", srcFile.getPath(), "-ar", "16000", dstFile.getPath()};
//
//        int rc = FFmpeg.execute(commands);
//        LoggerUtil.logI(TAG, "rc  100 ---->" + rc+"---->"+dstFile.getPath());
////                isDealing = false;
//        if (rc == FFmpeg.RETURN_CODE_SUCCESS) {
////                    update(dstFile.getPath());
//        } else {
////                    update(null);
//        }
//        return dstFile.getPath();
//    }
}
