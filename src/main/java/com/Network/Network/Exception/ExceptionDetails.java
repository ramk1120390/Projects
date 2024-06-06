package com.Network.Network.Exception;


public class ExceptionDetails {
    String exceptionId;

    public ExceptionDetails(String exceptionId, String inputMessage) {
        this.exceptionId = exceptionId;
        this.inputMessage = inputMessage;
    }

    public String getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(String exceptionId) {
        this.exceptionId = exceptionId;
    }

    public String getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(String inputMessage) {
        this.inputMessage = inputMessage;
    }

    String inputMessage;
}
