package com.jk.rcp.main.data.model.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventPost {

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("env")
    @Expose
    private String env;
    @SerializedName("event")
    @Expose
    private Event event;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "EventPost{" +
                "state='" + state + '\'' +
                ", env='" + env + '\'' +
                ", event=" + event +
                ", msg='" + msg + '\'' +
                '}';
    }
}