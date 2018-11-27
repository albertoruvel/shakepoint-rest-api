package com.shakepoint.web.api.data.fcm;

import java.util.Map;

public class FcmNotification {

    private static final String MESSAGE_TYPE = "fcmMessageType";

    private FirebaseMessage message;

    public FirebaseMessage getMessage() {
        return message;
    }

    public void setMessage(FirebaseMessage message) {
        this.message = message;
    }

    public static FcmNotification createNotification(FcmMessageType type, String message, String title, String token, Map<String, Object> data) {
        FcmNotification notification = new FcmNotification();
        FirebaseMessage firebaseMessage = new FirebaseMessage();
        firebaseMessage.setToken(token);
        firebaseMessage.setData(data);
        //add type to data
        data.put(MESSAGE_TYPE, type.value); 
        FirebaseNotificationData notificationData = new FirebaseNotificationData();
        notificationData.setBody(message);
        notificationData.setTitle(title);
        firebaseMessage.setNotification(notificationData);
        notification.setMessage(firebaseMessage);
        return notification;
    }
}
