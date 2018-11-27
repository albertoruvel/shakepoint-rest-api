package com.shakepoint.web.api.data.fcm;

import java.util.Map;

public class FirebaseMessage {

    private String token;
    private FirebaseNotificationData notification;
    private Map<String, Object> data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public FirebaseNotificationData getNotification() {
        return notification;
    }

    public void setNotification(FirebaseNotificationData notification) {
        this.notification = notification;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
