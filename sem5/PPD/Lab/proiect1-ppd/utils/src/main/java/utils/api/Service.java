package utils.api;

import utils.domain.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface Service extends Remote {
    ProgramResponse program(PlanningDto planningDto) throws ExecutionException, InterruptedException, RemoteException;
    PayResponse pay(Payment payment) throws RemoteException, ExecutionException, InterruptedException;
    boolean cancel(Integer planningId, Payment payment) throws RemoteException, ExecutionException, InterruptedException;
}
