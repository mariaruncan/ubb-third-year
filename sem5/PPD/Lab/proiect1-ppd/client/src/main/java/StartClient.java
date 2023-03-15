import client.ClientWorker;
import utils.api.Service;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class StartClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 8080);
            Service service = (Service) registry.lookup("Service");
            ClientWorker worker = new ClientWorker();
            worker.run(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

