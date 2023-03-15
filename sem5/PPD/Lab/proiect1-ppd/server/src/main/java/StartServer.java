import server.ServiceImpl;
import utils.Container;
import utils.api.Service;

import java.io.FileNotFoundException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartServer {
    private static Service service;

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, FileNotFoundException {
        ExecutorService executor = Executors.newFixedThreadPool(8);

        Service service = new ServiceImpl(executor);
        Registry registry = LocateRegistry.createRegistry(8080);
        registry.bind("Service", service);
        System.out.println("Server ready");
        System.out.println("Serverul a fost pornit. Asteptare taskuri...");
        startChecker();
        stop();
    }

    private static void startChecker() throws FileNotFoundException {
        Checker checker = new Checker();
        checker.run();
    }

    private static void stop() {
        try {
            Thread.sleep(120000);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
