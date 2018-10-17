package com.shakepoint.web.api.core.fcm;

import com.shakepoint.web.api.core.fcm.msg.FirebaseMessage;
import com.shakepoint.web.api.core.fcm.msg.FirebaseMessageEnvelope;
import com.shakepoint.web.api.core.fcm.msg.FirebaseNotification;
import com.shakepoint.web.api.core.fcm.msg.FirebaseResponse;
import okhttp3.ResponseBody;
import org.apache.log4j.Logger;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
@Startup
public class FirebaseClientService {

    @Inject
    private FirebaseRetrofitConfiguration configuration;

    private final Logger log = Logger.getLogger(getClass());
    private FirebaseClient client;

    @PostConstruct
    public void init() {
        client = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(configuration.getServerUrl())
                .build().create(FirebaseClient.class);
    }

    public void sendPushNotification(String token, String message, String title) {
        if (token == null) {
            throw new IllegalArgumentException("Invalid FCM token, cannot send push notification");
        }
        try{
            Response<FirebaseResponse> responseBody = client.sendPushNotification(configuration.getApiKey(),
                    createFirebaseEnvelope(token, title, message), configuration.getProjectId())
                    .execute();
            if (! responseBody.isSuccessful()) {
                //log raw response
                log.debug(responseBody.raw());
            }
        }catch(IOException ex) {
            log.info("Could not send push notification", ex);
        }
    }

    private FirebaseMessageEnvelope createFirebaseEnvelope(String token, String title, String body) {
        FirebaseMessageEnvelope envelope = new FirebaseMessageEnvelope();
        envelope.setMessage(new FirebaseMessage(token, new FirebaseNotification(title, body)));
        return envelope;
    }
}
