package com.shakepoint.web.api.core.fcm;

import com.github.roar109.syring.annotation.ApplicationProperty;

import javax.inject.Inject;

public class FirebaseRetrofitConfiguration {

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.fcm.serverUrl", type = ApplicationProperty.Types.SYSTEM)
    private String serverUrl;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.fcm.apiKey", type = ApplicationProperty.Types.SYSTEM)
    private String apiKey;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.fcm.projectId", type = ApplicationProperty.Types.SYSTEM)
    private String projectId;

    public String getServerUrl() {
        return serverUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getProjectId() {
        return projectId;
    }
}
