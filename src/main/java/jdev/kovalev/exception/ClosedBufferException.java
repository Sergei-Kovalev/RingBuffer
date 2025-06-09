package jdev.kovalev.exception;

public class ClosedBufferException extends Exception {
    private final static String DEFAULT_MESSAGE = "Буфер закрыт для операций чтения/записи.";

    public ClosedBufferException() {
        super(DEFAULT_MESSAGE);
    }
}
