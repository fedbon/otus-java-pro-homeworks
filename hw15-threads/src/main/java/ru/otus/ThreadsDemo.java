package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadsDemo {
    private static final Logger log = LoggerFactory.getLogger(ThreadsDemo.class);
    private static final String MESSAGE = "************************* {} : [{}]";
    private static final int MIN = 1;
    private static final int MAX = 10;
    private static final String FIRST_THREAD = "THREAD 1";
    private static final String SECOND_THREAD = "THREAD 2";
    private static final Lock R_LOCK = new ReentrantLock();
    private static final Condition TRY_AGAIN = R_LOCK.newCondition();

    private static boolean next;
    private final String threadName;
    private int counter = 0;

    public ThreadsDemo() {
        this.threadName = Thread.currentThread().getName();
    }

    public static void main(String... args) {
        new Thread(() -> new ThreadsDemo().task(), FIRST_THREAD).start();
        new Thread(() -> new ThreadsDemo().task(), SECOND_THREAD).start();
    }

    private void task() {
        while (counter < MAX) {
            lock();
            increment();
            unlock();
        }
        while (counter > MIN) {
            lock();
            decrement();
            unlock();
        }
    }

    private void lock() {
        R_LOCK.lock();
        log.debug("{}: Locked", threadName);
        orderThreads();
    }

    private synchronized void orderThreads() {
        if (isFirst()) {
            next = true;
        } else if (isNext()) {
            next = false;
        } else {
            await();
            orderThreads();
        }
    }

    private boolean isFirst() {
        return threadName.equals(FIRST_THREAD) && !next;
    }

    private boolean isNext() {
        return !threadName.equals(FIRST_THREAD) && next;
    }

    private void await() {
        try {
            TRY_AGAIN.await();
            log.debug("{}: Waiting...", threadName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void unlock() {
        try {
            TRY_AGAIN.signalAll();
            log.debug("{}: Signal All", threadName);
        } finally {
            R_LOCK.unlock();
            log.debug("{}: Unlocked", threadName);
        }
    }

    private void increment() {
        ++counter;
        log();
    }

    private void decrement() {
        --counter;
        log();
    }

    private void log() {
        log.info(MESSAGE, Thread.currentThread().getName(), counter);
    }
}
