package com.fcgl.madrid.stock.schedule;

import com.fcgl.madrid.stock.Payload.response.Response;
import com.fcgl.madrid.stock.Service.BusinessWire;
import com.fcgl.madrid.stock.Service.GlobalNewsWire;
import com.fcgl.madrid.stock.Twilio.TwilioSms;
import com.fcgl.madrid.stock.model.IArticleBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private BusinessWire businessWire;
    private GlobalNewsWire globalNewsWire;

    @Autowired
    public void setUp(BusinessWire businessWire, GlobalNewsWire globalNewsWire) {
        this.businessWire = businessWire;
        this.globalNewsWire = globalNewsWire;
    }

    public void scheduleTaskWithFixedRate() {
    }

    @Scheduled(fixedDelay = 1000 * 60 * 2)
    public void scheduleTaskWithFixedDelay() {
        logger.info("Fixed Delay Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        ResponseEntity<Response<List<IArticleBody>>> businessWireResponse = businessWire.getAllImportantArticles();
        ResponseEntity<Response<List<IArticleBody>>> globalNewsWireResponse = globalNewsWire.getAllImportantArticles();
        if (businessWireResponse.getStatusCode().equals(HttpStatus.OK) && globalNewsWireResponse.getStatusCode().equals(HttpStatus.OK)){
            List<String> messages = new ArrayList<>();
            logger.info("Sending...");
            for (IArticleBody articleBody: Objects.requireNonNull(businessWireResponse.getBody()).getResponse()) {
                String message = articleBody.toStringMax(TwilioSms.SMS_MAX);
                logger.info(message);
                messages.add(message);
            }
            for (IArticleBody articleBody: Objects.requireNonNull(globalNewsWireResponse.getBody()).getResponse()) {
                String message = articleBody.toStringMax(TwilioSms.SMS_MAX);
                logger.info(message);
                messages.add(message);
            }
            logger.info("Sent this many text messages: " + messages.size());
            TwilioSms.sendMultipleSms(messages, TwilioSms.DEFAULT_NUMBER);
        } else {
            logger.error("****THERE WAS AN ERROR, CHECK ABOVE****");
            TwilioSms.sendSms("ERROR WITH URL PARSING, CHECK LOGS", TwilioSms.DEFAULT_NUMBER);
        }
    }

    public void scheduleTaskWithInitialDelay() {}

    public void scheduleTaskWithCronExpression() {}
}
