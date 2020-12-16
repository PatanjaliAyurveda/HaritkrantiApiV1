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
public class AmazonSNSTesting {

    public static void main(String[] args) {
        // Your Credentials
        String ACCESS_KEY = "AKIAI4YZFFHLSITFVOCQ";
        String SECRET_KEY = "jCa4y8eOuE2aiJEJE4wtD2JAO1mQBqRbDK3YWohL";

        BasicAWSCredentials creds = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        AmazonSNS sns = AmazonSNSClient.builder()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();

        String message = "YOUR MESSAGE east ";
        String phoneNumber = "919465313100";  // Ex: +91XXX4374XX
        sendSMSMessage(sns, message, phoneNumber);
    }
    // Send SMS to a Phone Number
    public static void sendSMSMessage(AmazonSNS snsClient,
                                      String message, String phoneNumber) {
        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber));
        System.out.println(result); // Prints the message ID.
    }

}
