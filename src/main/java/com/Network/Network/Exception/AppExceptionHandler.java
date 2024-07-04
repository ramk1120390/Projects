package com.Network.Network.Exception;


import java.time.Instant;

import com.Network.Network.DevicemetamodelPojo.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AppExceptionHandler {
    Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    public void raiseException(String errorMessage) {
        logger.error("Exception thrown with error message: {}", errorMessage);
        String exceptionId = String.valueOf(Instant.now());
        ExceptionDetails exceptionDetails = new ExceptionDetails(exceptionId, errorMessage);
        throw new ServiceException(errorMessage, exceptionDetails);
    }


}
