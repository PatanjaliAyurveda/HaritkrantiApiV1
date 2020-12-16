package com.bharuwa.haritkranti.msg91;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.scheduling.annotation.Async;

import java.net.URLEncoder;

/**
 * @author anuragdhunna
 */
public class Msg91Services {

    // TODO: validate otp
    // TODO: set expiry time for otp


    /**
     * Send Sms
     * @return
     * @throws Exception
     */
    public static String sendMsg(String otp, String phoneNumber) throws Exception {

        String message = "Your OTP is:"+otp;
        String authkey = "279572AIMNJmQZyZc5cf5fd57";
        String url = "https://api.msg91.com/api/sendhttp.php?authkey=" + authkey + "&mobiles="+phoneNumber+"&message="
                + URLEncoder.encode(message, "UTF-8") + "&sender=ANDATA&route=4&country=91";

//        String arl = "https://api.msg91.com/api/sendhttp.php?mobiles=8930797009&authkey=279572AIMNJmQZyZc5cf5fd57&route=4&sender=TESTIN&message=Hello!%20This%20is%20a%20test%20message&country=91";
//        String url = "http://api.msg91.com/api/sendhttp.php?country=91&sender=TESTIN&route=4&mobiles="+phoneNumber+"&authkey="+authkey+"&message=" + URLEncoder.encode(message, "UTF-8");

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpGet getRequest = new HttpGet(url);
            getRequest.setHeader("Accept", "application/json");
            getRequest.setHeader("Content-Type", "application/json");
            HttpResponse httpResponse = httpClient.execute(getRequest);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            System.out.println("======statusCode======="+statusCode);

            return checkMsgResponse(statusCode);
        }catch(Exception e){
            throw e;
        }
    }

    @Async("threadPoolTaskExecutor")
    public static String sendFarmerAppLinkMsg(String message, String phoneNumber) throws Exception {
        String authkey = "279572AIMNJmQZyZc5cf5fd57";
        String url = "https://api.msg91.com/api/sendhttp.php?authkey=" + authkey + "&mobiles="+phoneNumber+"&message="
                + URLEncoder.encode(StringEscapeUtils.unescapeJava(message), "UTF-8") + "&sender=ANDATA&route=4&country=91&unicode=1";

//        String arl = "https://api.msg91.com/api/sendhttp.php?mobiles=8930797009&authkey=279572AIMNJmQZyZc5cf5fd57&route=4&sender=TESTIN&message=Hello!%20This%20is%20a%20test%20message&country=91";
//        String url = "http://api.msg91.com/api/sendhttp.php?country=91&sender=TESTIN&route=4&mobiles="+phoneNumber+"&authkey="+authkey+"&message=" + URLEncoder.encode(message, "UTF-8");

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpGet getRequest = new HttpGet(url);
            getRequest.setHeader("Accept", "application/json");
            getRequest.setHeader("Content-Type", "application/json");
            HttpResponse httpResponse = httpClient.execute(getRequest);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            System.out.println("======statusCode======="+statusCode);

            return checkMsgResponse(statusCode);
        }catch(Exception e){
            throw e;
        }
    }

    /**
     * Check send sms response
     * All response codes are not checked
     * @param code
     * @return
     */
    private static String checkMsgResponse(int code) {

        switch (code) {
            case 200:
                return"success";
            case 101:
                return "Missing mobile no.";
            case 102:
                return "Missing message";
            case 106:
                return "Missing Authentication Key";
            case 202:
                return "Invalid mobile number. You must have entered either less than 10 digits or there is an alphabetic character in the mobile number field in API.";
            case 207:
                return "Invalid authentication key. Crosscheck your authentication key from your account’s API section.";
            case 208:
                return "IP is blacklisted. We are getting SMS submission requests other than your whitelisted IP list.";
            case 301:
                return "Insufficient balance to send SMS";
            case 302:
                return "Expired user account. You need to contact your account manager.";
            case 310:
                return "SMS is too long. System paused this request automatically.";
        }

        // Check response here
        // https://docs.msg91.com/collection/msg91-api-integration/5/send-sms/T26A6X72
        return "Need to check code";
    }
}
