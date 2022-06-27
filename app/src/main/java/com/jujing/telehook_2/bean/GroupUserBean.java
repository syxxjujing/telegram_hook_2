package com.jujing.telehook_2.bean;

public class GroupUserBean {
    public String userName;
    public String nickName;
    public String group;
    public String collectTime;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    @Override
    public String toString() {
        return "GroupUserBean{" +
                "userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", group='" + group + '\'' +
                ", collectTime='" + collectTime + '\'' +
                '}';
    }
}
