package com.shakepoint.web.api.core.shop;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PayWorksClient {

    @FormUrlEncoded
    @POST("payw2")
    public Call<ResponseBody> authorizePayment(
            @Field(value = "MODE") String mode,
            @Field(value = "AMOUNT") double amount,
            @Field(value = "CMD_TRANS") String commandTransaction,
            @Field(value = "USER") String user,
            @Field(value = "MERCHANT_ID") String merchantId,
            @Field(value = "PASSWORD") String password,
            @Field(value = "CARD_NUMBER") String cardNumber,
            @Field(value = "CARD_EXP") String cardExpiration,
            @Field(value = "SECURITY_CODE") String cvv,
            @Field(value = "ENTRY_MODE") String entryMode,
            @Field(value = "RESPONSE_LANGUAGE") String responseLanguage,
            @Field(value = "TERMINAL_ID") String terminalId);
}
