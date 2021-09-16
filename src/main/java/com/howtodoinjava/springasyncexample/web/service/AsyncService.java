package com.howtodoinjava.springasyncexample.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncService.class);
    private static int COUNTER = 0;
    @Async
    @Retryable(value = {Exception1.class, Exception2.class},
            maxAttempts = 1, backoff = @Backoff(5000))
    public void someAsyncService( String user) {
        COUNTER++;
        if (COUNTER == 1) {
            LOGGER.info("counter 1");

            throw new Exception1();
        } else if (COUNTER == 2) {
            LOGGER.info("counter 2");

            throw new Exception2();
        }
        else if( COUNTER == 3) {
            LOGGER.info("success");
            return;
         }
        else {
            LOGGER.info("error");
            throw new RuntimeException();
        }
    }

    @Recover
    public String recover( Throwable t) {
        LOGGER.info("AsyncService.recover");
        return "Error class ::" + t.getClass().getName();
    }
}
