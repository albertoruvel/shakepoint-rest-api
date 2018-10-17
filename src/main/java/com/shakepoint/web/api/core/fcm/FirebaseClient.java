package com.shakepoint.web.api.core.fcm;

import com.shakepoint.web.api.core.fcm.msg.FirebaseMessageEnvelope;
import com.shakepoint.web.api.core.fcm.msg.FirebaseResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FirebaseClient {

    @POST("{projectId}/messages:send")
    public Call<FirebaseResponse> sendPushNotification(@Header("Authorization") String authorizationToken,
                                                       @Body FirebaseMessageEnvelope message, @Path("projectId") String projectId);
}
