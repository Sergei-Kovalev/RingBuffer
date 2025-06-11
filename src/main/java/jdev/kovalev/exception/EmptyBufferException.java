package jdev.kovalev.exception;

public class EmptyBufferException extends Exception {
    private static final String DEFAULT_MESSAGE = "Невозможно получить элемент - буфер пуст";

    public EmptyBufferException() {
        super(DEFAULT_MESSAGE);
    }
}
