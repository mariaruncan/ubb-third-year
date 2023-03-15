package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccountV2 {
    double amount;

    public BankAccountV2(double amount) {
        this.amount = amount;
    }

    synchronized double getAmount() {
        return this.amount;
    }

    synchronized boolean deposit(double sum) {
        amount += sum;
        return true;
    }

    synchronized boolean withdraw(double sum) {
        if (amount < sum) {
            return false;
        } else {
            amount -= sum;
            return true;
        }
    }

    boolean transfer(BankAccountV2 other, double sum) {
        synchronized (this) {
            synchronized (other) {
                if (amount < sum) {
                    return false;
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    other.amount += sum;
                    amount -= sum;
                    return true;
                }
            }
        }
    }

    boolean transfer2(BankAccountV2 other, double sum) {
        synchronized (other) {
            synchronized (this) {
                if (amount < sum) {
                    return false;
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    other.amount += sum;
                    amount -= sum;
                    return true;
                }
            }
        }
    }
}
