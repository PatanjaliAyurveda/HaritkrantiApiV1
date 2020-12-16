package com.bharuwa.haritkranti.weather;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Random;
import java.util.Collections;
import java.net.URLEncoder;

/**
 * @author anuragdhunna
 */
public class WeatherYdnJava {

    public static String getWeather(String latitude, String longitude)throws Exception{
        final String appId = "Ql26Fd7a";
        final String consumerKey = "dj0yJmk9UXE1ZTc2Qnh2SEgxJmQ9WVdrOVVXd3lOa1prTjJFbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PTZl";
        final String consumerSecret = "27838a1789f10bb9485f7bbf32b1bbc20e860a84";
        final String url = "https://weather-ydn-yql.media.yahoo.com/forecastrss";

        long timestamp = new Date().getTime() / 1000;
        byte[] nonce = new byte[32];
        Random rand = new Random();
        rand.nextBytes(nonce);
        String oauthNonce = new String(nonce).replaceAll("\\W", "");

        List<String> parameters = new ArrayList<>();
        parameters.add("oauth_consumer_key=" + consumerKey);
        parameters.add("oauth_nonce=" + oauthNonce);
        parameters.add("oauth_signature_method=HMAC-SHA1");
        parameters.add("oauth_timestamp=" + timestamp);
        parameters.add("oauth_version=1.0");
        // Make sure value is encoded
        parameters.add("lat=" + URLEncoder.encode(latitude, "UTF-8"));
        parameters.add("lon=" + URLEncoder.encode(longitude, "UTF-8"));
        parameters.add("format=json");
        Collections.sort(parameters);

        StringBuilder parametersList = new StringBuilder();
        for (int i = 0; i < parameters.size(); i++) {
            parametersList.append((i > 0) ? "&" : "").append(parameters.get(i));
        }

        String signatureString = "GET&" +
                URLEncoder.encode(url, "UTF-8") + "&" +
                URLEncoder.encode(parametersList.toString(), "UTF-8");

        String signature = null;
        try {
            SecretKeySpec signingKey = new SecretKeySpec((consumerSecret + "&").getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHMAC = mac.doFinal(signatureString.getBytes());
            Encoder encoder = Base64.getEncoder();
            signature = encoder.encodeToString(rawHMAC);
        } catch (Exception e) {
            System.err.println("Unable to append signature");
            System.exit(0);
        }


        String authorizationLine = "OAuth " +
                "oauth_consumer_key=\"" + consumerKey + "\", " +
                "oauth_nonce=\"" + oauthNonce + "\", " +
                "oauth_timestamp=\"" + timestamp + "\", " +
                "oauth_signature_method=\"HMAC-SHA1\", " +
                "oauth_signature=\"" + signature + "\", " +
                "oauth_version=\"1.0\"";
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(url + "?lat="+latitude+"&lon="+longitude+"&format=json");
        getRequest.setHeader("Authorization", authorizationLine);
        getRequest.setHeader("X-Yahoo-App-Id", appId);
        getRequest.setHeader("Content-Type", "application/json");
        HttpResponse httpResponse = client.execute(getRequest);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        System.out.println("========statusCode=============="+statusCode);
        if(statusCode != 200){
            return checkMsgResponse(statusCode);
        }
        StringBuilder builder = new StringBuilder();
        BufferedReader br = new BufferedReader(
                new InputStreamReader((httpResponse.getEntity().getContent())));
        String output;
        try {
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            return e1.getMessage();

        }
        return builder.toString();
    }
    /**
     * Check getWeather api response
     * @param code
     * @return
     */
    private static String checkMsgResponse(int code) {

        switch (code) {
            case 400:
                return "Malformed syntax or a bad query.";
            case 401:
                return "Action requires user authentication.";
            case 403:
                return "Authentication failure or invalid Application ID.";
            case 404:
                return "Resource not found.";
            case 405:
                return "Method not allowed on resource.";
            case 406:
                return "Requested representation not available for the resource.";
            case 410:
                return "The URI used to refer to a resource.";
            case 411:
                return "The server needs to know the size of the entity body and it should be specified in the Content Length header.";
            case 500:
                return "Internal Server Error.";
            case 501:
                return "Requested HTTP operation not supported.";

        }

        // Check response here
        // https://developer.yahoo.com/social/rest_api_guide/http-response-codes.html
        return "Need to check code";
    }
}

