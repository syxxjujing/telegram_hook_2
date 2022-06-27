package com.jujing.telehook_2.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonTool
{
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public static <T> T paraseJson(String paramString, Class<T> paramClass)
    {
        return (T)gson.fromJson(paramString, paramClass);
    }

    public static String paraseJson(Object paramObject)
    {
        return gson.toJson(paramObject);
    }

    public static String compileXml(String paramString)
    {
        Matcher localMatcher = Pattern.compile("<!\\[CDATA\\[(.*?)]]>").matcher(paramString);
        if (localMatcher.find()) {
            paramString = localMatcher.group(1);
        }
        return paramString;
    }
}
