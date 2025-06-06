package jdev.kovalev;

import jdev.kovalev.exception.BufferOverflowException;
import jdev.kovalev.exception.EmptyBufferException;

public class RingBuffer<T> {
    private final int capacity;
    private int size = 0;
    private int head = 0;
    private int tail = 0;
    private final T[] array;

    @SuppressWarnings("unchecked")
    public RingBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Емкость буфера должна быть больше нуля");
        }
        this.capacity = capacity;
        this.array = (T[])new Object[capacity];
    }

    public synchronized void add(T element) throws BufferOverflowException {
        if (size >= capacity) {
            throw new BufferOverflowException();
        }

        array[tail] = element;

        tail = (tail + 1) % capacity;
        size++;
    }

    // Получить и удалить элемент
    public synchronized T get() throws EmptyBufferException {
        if (size == 0) {
            throw new EmptyBufferException();
        }

        T element = array[head];
        array[head] = null;

        head = (head + 1) % capacity;
        size--;

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
}