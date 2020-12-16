package com.bharuwa.haritkranti.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

/**
 * @author anuragdhunna
 */
public class SendMsgAWS {

    public static String sendMsgAws(String otp, String phoneNumber) {
        // Your Credentials
        String ACCESS_KEY = "AKIAI4YZFFHLSITFVOCQ";
        String SECRET_KEY = "jCa4y8eOuE2aiJEJE4wtD2JAO1mQBqRbDK3YWohL";

        BasicAWSCredentials creds = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        AmazonSNS sns = AmazonSNSClient.builder()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();

        String message = "Your verification code is:"+otp;
//        String phoneNumber = "919465313100";  // Ex: +91XXX4374XX
        return sendSMSMessage(sns, message, phoneNumber);
    }
    // Send SMS to a Phone Number
    private static String  sendSMSMessage(AmazonSNS snsClient,
                                          String message, String phoneNumber) {
        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber));
        System.out.println(result); // Prints the message ID.
        return result.getMessageId();
    }
}
