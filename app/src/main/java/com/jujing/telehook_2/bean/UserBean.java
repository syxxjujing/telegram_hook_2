package com.jujing.telehook_2.bean;

public class UserBean {

    public String username;
    public String nickname;
    public int userid;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", userid=" + userid +
                '}';
    }
}
