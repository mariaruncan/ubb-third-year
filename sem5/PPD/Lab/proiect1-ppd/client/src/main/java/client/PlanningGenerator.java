package client;

import utils.Container;
import utils.domain.PlanningDto;
import java.time.LocalDateTime;
import java.util.Random;

public class PlanningGenerator {
    private static final Random random = new Random();

    public static PlanningDto generate() {
        String name = "Raul";
        String cnp = "5010926260000";
        LocalDateTime date = LocalDateTime.now();
        int location = random.nextInt(Container.locationsNumber) + 1;
        int treatment = random.nextInt(Container.treatmentNumber) + 1;
        LocalDateTime treatmentDate = LocalDateTime.now();
        int hour = random.nextInt((18 - 10) + 1) + 10; // ora intre 10 si 18
        boolean cancel = random.nextBoolean();

        return new PlanningDto(name, cnp, date, location, treatment, treatmentDate, hour, cancel);
    }
}
