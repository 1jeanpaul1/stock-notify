package com.fcgl.madrid.stock.Twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TwilioSms {
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN  = "";

    // Create a phone number in the Twilio console
    public static final String TWILIO_NUMBER = "+12512559842";

    public String sendSms(String msg, String phoneNumber) {
        //TODO: What type of errors can Twilio and Message packages throw
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(TWILIO_NUMBER),
                msg)
                .create();
        return message.getSid();
    }

    public List<String> sendMultipleSms(List<String> messages, String phoneNumber) {
        List<String> sids = new ArrayList<>();
        for (String message : messages) {
            sids.add(sendSms(message, phoneNumber));
        }
        return sids;
    }
}
