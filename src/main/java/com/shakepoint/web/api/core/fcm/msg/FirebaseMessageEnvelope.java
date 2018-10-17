package com.shakepoint.web.api.core.fcm.msg;

public class FirebaseMessageEnvelope {
    private FirebaseMessage message;

    public FirebaseMessageEnvelope() {
    }

    public FirebaseMessage getMessage() {
        return message;
    }

    public void setMessage(FirebaseMessage message) {
        this.message = message;
    }
}
