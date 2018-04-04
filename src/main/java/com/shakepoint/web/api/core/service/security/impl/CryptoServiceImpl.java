package com.shakepoint.web.api.core.service.security.impl;

import com.github.roar109.syring.annotation.ApplicationProperty;
import com.shakepoint.web.api.core.service.security.CryptoService;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.security.Key;

@Startup
public class CryptoServiceImpl implements CryptoService {

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.crypto.key", type = ApplicationProperty.Types.SYSTEM)
    private String key;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.crypto.algorithm", type = ApplicationProperty.Types.SYSTEM)
    private String algorithm;

    private Cipher cipher;
    private SecretKeySpec secretKeySpec;
    private final Logger log = Logger.getLogger(getClass());

    @PostConstruct
    public void init(){
        try{
            secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
            cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        }catch(Exception ex){
            log.error("Could not initialize CryptoService", ex);
        }
    }

    @Override
    public String encrypt(String value) {
        try{
            return new String(cipher.doFinal(value.getBytes()), "UTF-8");
        }catch(Exception ex){
            log.error("Could not encrypt string value", ex);
            return null;
        }
    }
}
