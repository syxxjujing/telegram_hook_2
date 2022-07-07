package com.jujing.telehook_2.bean;

public class SayHiMessageBean {
    public int date;
    public long mid;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    @Override
    public String toString() {
        return "SayHiMessageBean{" +
                "date=" + date +
                ", mid=" + mid +
                '}';
    }
}
