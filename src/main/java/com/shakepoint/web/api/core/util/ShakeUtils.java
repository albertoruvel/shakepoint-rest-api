/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.api.core.util;

import com.shakepoint.web.api.data.dto.request.admin.NewProductRequest;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Alberto Rubalcaba
 */
public class ShakeUtils {

    public static String[] getDateRange(Date from, Date to) {
        //get day difference
        int days = Math.round((to.getTime() - from.getTime()) / 86400000); //convert to days
        final String[] range = new String[days];
        long time = from.getTime();
        String date = "";
        for (int i = 0; i < days; i++) {
            date = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.format(new Date(time));
            //add the date to the range array
            range[i] = date;
            time = time + 86400000;
        }
        return range;
    }

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    public static final SimpleDateFormat SLASHES_SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SecureRandom random = new SecureRandom();

    public static String getNextSessionToken() {
        return new BigInteger(130, random).toString();
    }

    public static String getResetPasswordToken() {
        final String token = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return token;
    }
}
