package callables;

import utils.Container;
import utils.domain.PayResponse;
import utils.domain.Payment;
import java.util.concurrent.Callable;


public class PayCallable implements Callable<PayResponse> {
    private final Payment payment;

    public PayCallable(Payment payment) {
        this.payment = payment;
    }

    @Override
    public PayResponse call() throws Exception {
        Payment p;
        synchronized (Container.paymentRepository) {
            p = Container.paymentRepository.save(payment);
        }

        return new PayResponse(p.getId());
    }
}
