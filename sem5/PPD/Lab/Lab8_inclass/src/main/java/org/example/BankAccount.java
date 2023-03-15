package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    double amount;
    Lock lock = new ReentrantLock();

    public BankAccount(double amount) {
        this.amount = amount;
    }

    double getAmount() {
        return this.amount;
    }

    boolean deposit(double sum) {
        lock.lock();
        amount += sum;
        lock.unlock();

        return true;
    }

    boolean withdraw(double sum) {
        lock.lock();
        if (amount < sum) {
            lock.unlock();
            return false;
        } else {
            amount -= sum;
            lock.unlock();
            return true;
        }
    }

    boolean transfer(BankAccount other, double sum) {
        this.lock.lock();
        other.lock.lock();
        if (amount < sum) {
            other.lock.unlock();
            this.lock.unlock();
            return false;
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            other.amount += sum;
            amount -= sum;
            other.lock.unlock();
            this.lock.unlock();
            return true;
        }
    }
}
