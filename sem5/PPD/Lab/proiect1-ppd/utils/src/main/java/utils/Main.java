package utils;

import utils.domain.Planning;
import utils.domain.Treatment;
import utils.persistence.PlanningRepository;
import utils.persistence.TreatmentRepository;

import java.time.LocalDateTime;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        TreatmentRepository treatmentRepository = Container.treatmentRepository;
        PlanningRepository planningRepository = Container.planningRepository;
        //treatmentRepository.save(new Treatment(21, 12));

        planningRepository.save(new Planning("dd", "dd", LocalDateTime.now(), 12, treatmentRepository.findAll().get(0), LocalDateTime.now(), 12));
       // System.out.println(treatmentRepository.findAll());
        System.out.println(planningRepository.findAll());


    }
}
