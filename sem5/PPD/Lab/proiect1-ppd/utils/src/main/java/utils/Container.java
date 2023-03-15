package utils;

import utils.persistence.PaymentRepository;
import utils.persistence.PlanningRepository;
import utils.persistence.TreatmentRepository;

public class Container {
    public final static TreatmentRepository treatmentRepository = new TreatmentRepository();
    public final static PlanningRepository planningRepository = new PlanningRepository();
    public final static PaymentRepository paymentRepository = new PaymentRepository();

    public final static int locationsNumber = 3;
    public final static int treatmentNumber = 5;

    public static String checkerFileName = "D:\\Facultate\\PPD\\teme\\proiect1\\server\\src\\main\\resources\\output.txt";
}
