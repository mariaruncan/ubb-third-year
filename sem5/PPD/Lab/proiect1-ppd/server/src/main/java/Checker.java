import utils.Container;
import utils.domain.Payment;
import utils.domain.Planning;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Checker implements Runnable {
    private final OutputStream output;

    public Checker() throws FileNotFoundException {
        output = new FileOutputStream(Container.checkerFileName);
    }

    @Override
    public synchronized void run() {
        // suma din planificari vs suma din payments!
        // pentru fiecare planificare caut payment
        while (true) {
            check();
        }
    }

    private void check() {
        Map<Integer, LogData> logDataMap = new HashMap<>();
        synchronized (Container.planningRepository) {
            synchronized (Container.paymentRepository) {
                List<Planning> plannings = Container.planningRepository.findAll();
                List<Payment> payments = Container.paymentRepository.findAll();

                Map<Integer, List<Planning>> pl = plannings.stream()
                        .collect(Collectors.groupingBy(Planning::getLocation));

                pl.forEach((key, value) -> {
                    System.out.println("key: " + key);
                    AtomicInteger expected = new AtomicInteger();
                    AtomicInteger actual = new AtomicInteger();
                    List<Planning> unpaidPlannings = new ArrayList<>();
                    value.forEach(planning -> {
                        expected.addAndGet(planning.getTreatment().getCost());
                        if (payments.stream().anyMatch(payment -> Objects.equals(payment.getPlanningId(), planning.getId())))
                            actual.addAndGet(planning.getTreatment().getCost());
                        else
                            unpaidPlannings.add(planning);
                    });
                    logDataMap.put(key, new LogData(expected.get(), actual.get(), unpaidPlannings));
                    System.out.println("logdata: " + new LogData(expected.get(), actual.get(), unpaidPlannings));
                });
                try {
                    writeToFile(logDataMap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        sleep();
    }

    private void writeToFile(Map<Integer,LogData> logDataMap) throws IOException {
        output.write(("Time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n").getBytes());
        logDataMap.forEach((key, value) -> {
            try {
                output.write(("Location: " + key + "\n").getBytes());
                output.write(("Expected amount: " + value.getExpectedAmount() + "\n").getBytes());
                output.write(("Actual amount: " + value.getActualAmount() + "\n").getBytes());
                output.write(("Unpaid plannings: " + value.getUnpaidPlannings().stream()
                        .reduce("", (currentString, element) -> currentString + element.getId(), String::concat) + "\n").getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        output.write(("\n").getBytes());
    }

    private void sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
