package com.shakepoint.web.api.core.fcm.msg;

public final class FirebaseMessage {
    private String token;
    private FirebaseNotification notification;

    public FirebaseMessage(String token, FirebaseNotification notification) {
        this.token = token;
        this.notification = notification;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public FirebaseNotification getNotification() {
        return notification;
    }

    public void setNotification(FirebaseNotification notification) {
        this.notification = notification;
    }
}
