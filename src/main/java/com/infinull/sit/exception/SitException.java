package com.infinull.sit.exception;

import com.infinull.sit.message.MessageUtil;

public class SitException extends IllegalArgumentException {

    private final int statusCode;  // Custom status code

    public SitException() {
        super("Sit exception thrown with no message.");
        this.statusCode = 1;
    }

    public SitException(int statusCode, String messageKey, Object... args) {
        super(MessageUtil.getMsg(messageKey, args));
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
