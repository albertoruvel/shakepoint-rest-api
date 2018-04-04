package com.shakepoint.web.api.core.shop;

import com.github.roar109.syring.annotation.ApplicationProperty;
import com.shakepoint.web.api.data.dto.response.PaymentDetails;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import org.apache.log4j.Logger;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PayWorksClientService {

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.admin.banorte.user", type = ApplicationProperty.Types.SYSTEM)
    private String user;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.admin.banorte.password", type = ApplicationProperty.Types.SYSTEM)
    private String password;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.admin.banorte.merchant", type = ApplicationProperty.Types.SYSTEM)
    private String merchantId;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.admin.banorte.terminal", type = ApplicationProperty.Types.SYSTEM)
    private String terminalId;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.admin.banorte.commandTransaction", type = ApplicationProperty.Types.SYSTEM)
    private String commandTransaction;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.admin.banorte.debugMode", type = ApplicationProperty.Types.SYSTEM)
    private Boolean debug;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.admin.banorte.cnfFile", type = ApplicationProperty.Types.SYSTEM)
    private String environmentConfigurationFile;

    @Inject
    private RetrofitConfiguration configuration;

    private final Logger log = Logger.getLogger(getClass());

    private PayWorksClient client;

    @PostConstruct
    public void init() {
        client = new Retrofit.Builder()
                .baseUrl(configuration.getServerUrl())
                .build().create(PayWorksClient.class);
    }

    public PaymentDetails authorizePayment(String cardNumber, String cardExpDate, String cvv, double amount){
        PaymentDetails details;
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        if (debug){
            log.info("DEBUG purchase have been created");
            details = new PaymentDetails("testAuthCode", dateFormat.format(new Date()), dateFormat.format(new Date()),
                    "123123", "123123123", "A", "Test purchase have been accepted");
            return details;
        }else{
            //get current mode
            final String mode = getCurrentProfileMode();
            try{
                Response<ResponseBody> response = client.authorizePayment(mode, amount, commandTransaction, user, merchantId, password, cardNumber, cardExpDate, cvv, "manual", "ES", terminalId)
                        .execute();
                Headers headers = response.headers();
                if (response.errorBody() != null){
                    log.error(response.errorBody().string());
                    return null;
                }else{
                    details = getDetailsFromResponse(headers);
                    return details;
                }

            }catch(IOException ex){
                log.error("Could not complete payment :(", ex);
                return null;
            }
        }
    }

    private String getCurrentProfileMode() {
        try{
            File file = new File(environmentConfigurationFile);
            if (file.exists()){
                BufferedReader reader = new BufferedReader(new FileReader(file));
                final String mode = reader.readLine();
                reader.close();
                return mode;
            }else {
                log.error("There is no file on directory " + environmentConfigurationFile);
                return null;
            }
        }catch(IOException ex){
            log.error("Could not read file " + environmentConfigurationFile, ex);
            return null;
        }
    }

    public void savePayWorksMode(PayWorksMode mode){
        try{
            File file = new File(environmentConfigurationFile);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(mode.getValue());
            writer.close();
        }catch(IOException ex){
            log.error("Could not write mode", ex);
        }
    }

    private PaymentDetails getDetailsFromResponse(Headers headers) {
        final String authCode = headers.get("CODIGO_AUT");
        final String requestDate = headers.get("FECHA_REQ_CTE");
        final String responseDate = headers.get("FECHA_RSP_CTE");
        final String merchantId = headers.get("ID_AFILIACION");
        final String reference = headers.get("REFERENCIA");
        final String payworksResult = headers.get("RESULTADO_PAYW");
        final String message = headers.get("TEXTO");

        return new PaymentDetails(authCode, requestDate, responseDate, merchantId, reference, payworksResult, message);
    }
}
