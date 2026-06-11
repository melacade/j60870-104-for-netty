package com.melody.j60870.except;

public class NotEnoughData extends Exception {
    private static final long serialVersionUID = 1L;

    public NotEnoughData() {
        super();
    }

    public NotEnoughData(String message) {
        super(message);
    }

    public NotEnoughData(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughData(Throwable cause) {
        super(cause);
    }
}
