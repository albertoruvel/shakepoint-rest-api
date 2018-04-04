package com.shakepoint.web.api.core.shop;

import com.github.roar109.syring.annotation.ApplicationProperty;

import javax.inject.Inject;

public class RetrofitConfiguration {

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.admin.banorte.url", type = ApplicationProperty.Types.SYSTEM)
    private String serverUrl;

    public String getServerUrl() {
        return serverUrl;
    }
}
