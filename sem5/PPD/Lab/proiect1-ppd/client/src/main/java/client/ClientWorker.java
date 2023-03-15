package client;

import utils.api.Service;
import utils.domain.PayResponse;
import utils.domain.Payment;
import utils.domain.PlanningDto;
import utils.domain.ProgramResponse;


import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

public class ClientWorker {
    public void run(Service server) throws ExecutionException, RemoteException, InterruptedException {
        while (true) {
            PlanningDto planningDto = PlanningGenerator.generate();
            ProgramResponse programResponse = server.program(planningDto);
            System.out.println(programResponse);
            if (programResponse != null) {
                Payment payment = new Payment(programResponse.getCost(), planningDto.getCnp(), LocalDateTime.now(), programResponse.getPlanningId());
                PayResponse payResponse = server.pay(payment);
                payment.setId(payResponse.getPaymentId());
                if (planningDto.getCancel())
                    System.out.println("cancel: " + server.cancel(programResponse.getPlanningId(), payment));
            }
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
