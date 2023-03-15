package server;


import callables.CancelCallable;
import callables.PayCallable;
import callables.ProgramCallable;
import utils.Container;
import utils.Mapper;
import utils.api.Service;
import utils.domain.PayResponse;
import utils.domain.Payment;
import utils.domain.PlanningDto;
import utils.domain.ProgramResponse;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.*;

public class ServiceImpl extends UnicastRemoteObject implements Service {
    private final ExecutorService executor;
    public final int[][] N = new int[10][10];

    public ServiceImpl(ExecutorService executor) throws RemoteException {
        super();
        this.executor = executor;
        computeMatrix();
    }

    public void computeMatrix() {
        N[1][1] = 3;
        N[1][2] = 1;
        N[1][3] = 1;
        N[1][4] = 2;
        N[1][5] = 1;

        for (int i = 2; i <= Container.locationsNumber; i++) {
            for (int j = 1; j <= Container.treatmentNumber; j++) {
                N[i][j] = N[1][j] * (i - 1);
            }
        }
    }

    @Override
    public ProgramResponse program(PlanningDto planningDto) throws ExecutionException, InterruptedException, RemoteException {
        System.out.println("program: " + planningDto);
        ProgramCallable programCallable = new ProgramCallable(Mapper.planningDtoToPlanning(planningDto), N);
        Future<ProgramResponse> future = executor.submit(programCallable);
        return future.get();
    }

    @Override
    public PayResponse pay(Payment payment) throws RemoteException, ExecutionException, InterruptedException {
        System.out.println("pay: " + payment);
        PayCallable payCallable = new PayCallable(payment);
        Future<PayResponse> future = executor.submit(payCallable);
        return future.get();
    }

    @Override
    public boolean cancel(Integer planningId, Payment payment) throws RemoteException, ExecutionException, InterruptedException {
        System.out.println("cancel: " + planningId + " " + payment);
        CancelCallable cancelCallable = new CancelCallable(planningId, payment);
        Future<Boolean> future = executor.submit(cancelCallable);
        return future.get();
    }
}
