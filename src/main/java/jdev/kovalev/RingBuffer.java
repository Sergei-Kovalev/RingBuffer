package jdev.kovalev;

import jdev.kovalev.exception.ClosedBufferException;
import jdev.kovalev.exception.EmptyBufferException;

public class RingBuffer<T> {
    private final int capacity;
    private int size = 0;
    private int head = 0;
    private int tail = 0;
    private final T[] array;
    private volatile boolean closed = false;

    @SuppressWarnings("unchecked")
    public RingBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Емкость буфера должна быть больше нуля");
        }
        this.capacity = capacity;
        this.array = (T[])new Object[capacity];
    }

    public synchronized void put(T element) throws InterruptedException, ClosedBufferException {
        if (closed) {
            throw new ClosedBufferException();
        }
        while (size == capacity) {
            wait();
            if (closed) {
                throw new ClosedBufferException();
            }
        }

        array[tail] = element;
        tail = (tail + 1) % capacity;
        size++;
        notifyAll();
    }

    // Получить и удалить элемент
    public synchronized T take() throws ClosedBufferException, InterruptedException {
        if (closed) {
            throw new ClosedBufferException();
        }
        while (size == 0){
            wait();
            if (closed) {
                throw new ClosedBufferException();
            }
        }

        T element = array[head];
        array[head] = null;

        head = (head + 1) % capacity;
        size--;
        notifyAll();
        return element;
    }

    // Получить элемент не удаляя его
    public synchronized T peek() throws EmptyBufferException {
        if (size == 0) {
            throw new EmptyBufferException();
        }
        return array[head];
    }

    public synchronized boolean isEmpty() {
        return size == 0;
    }

    public synchronized boolean isFull() {
        return size == capacity;
    }

    public synchronized int getSize() {
        return size;
    }

    public synchronized void close() {
        closed = true;
    }

    public synchronized boolean isClosed() {
        return closed;
    }
}