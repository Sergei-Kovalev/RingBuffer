package jdev.kovalev.exception;

public class BufferOverflowException extends Exception {
    private static final String DEFAULT_MESSAGE = "Буфер заполнен";

    public BufferOverflowException() {
        super(DEFAULT_MESSAGE);
    }
}
