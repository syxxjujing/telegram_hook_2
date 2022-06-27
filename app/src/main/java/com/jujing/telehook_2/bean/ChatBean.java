package com.jujing.telehook_2.bean;

public class ChatBean {

    public long chatId ;
    public String title;

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ChatBean{" +
                "chatId=" + chatId +
                ", title='" + title + '\'' +
                '}';
    }
}
