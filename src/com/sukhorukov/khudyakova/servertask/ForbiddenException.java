package com.sukhorukov.khudyakova.servertask;

/**

 */
class ForbiddenException extends Exception {

    ForbiddenException() {
    }

    ForbiddenException(String message) {
        super(message);
    }

    ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    ForbiddenException(Throwable cause) {
        super(cause);
    }

    ForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
