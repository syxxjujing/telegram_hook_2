package com.jujing.telehook_2.bean;

public class LocalReplyBean {

    String content = "";
    String time = "";

    public LocalReplyBean(String content, String time) {
        this.content = content;
        this.time = time;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public String toString() {
        return "LocalReplyBean{" +
                "content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
