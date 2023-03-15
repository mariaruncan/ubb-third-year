package org.example;

public class Main {
    public static void main(String[] args) {
        BankAccountV2 first = new BankAccountV2(10000);
        BankAccountV2 second = new BankAccountV2(10000);

        Thread t1 = new Thread(() -> first.withdraw(50));
        Thread t2 = new Thread(() -> first.deposit(1000));
        Thread t3 = new Thread(() -> first.transfer(second, 10));
        Thread t4 = new Thread(() -> second.withdraw(50));
        Thread t5 = new Thread(() -> second.transfer(first, 100));
        Thread t6 = new Thread(() -> second.transfer2(first, 100));

        System.out.println("First amount: " + first.getAmount());
        System.out.println("Second amount: " + second.getAmount());

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();

//        first.withdraw(50);
//        first.deposit(1000);
//        first.transfer(second, 10);
//        second.withdraw(50);
//        second.transfer(first, 100);

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("After operations");
        System.out.println("First amount: " + first.getAmount());
        System.out.println("Second amount: " + second.getAmount());
    }
}