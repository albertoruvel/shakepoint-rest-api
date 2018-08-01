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
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.MessageDigest;

@Startup
public class CryptoServiceImpl implements CryptoService {

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.crypto.key", type = ApplicationProperty.Types.SYSTEM)
    private String key;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.crypto.algorithm", type = ApplicationProperty.Types.SYSTEM)
    private String algorithm;

    private final Logger log = Logger.getLogger(getClass());

    @Override
    public String encrypt(String value) {
        try{
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            byte[] digest = messageDigest.digest();
            String hex = DatatypeConverter.printHexBinary(digest);
            return hex;
        }catch(Exception ex){
            log.error("Could not encrypt string value", ex);
            return null;
        }
    }
}
