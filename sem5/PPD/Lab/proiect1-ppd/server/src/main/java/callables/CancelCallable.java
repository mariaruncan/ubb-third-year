package callables;

import utils.Container;
import utils.domain.Payment;

import java.util.concurrent.Callable;


public class CancelCallable implements Callable<Boolean> {
    private final Integer planningId;
    private final Payment payment;

    public CancelCallable(Integer planningId, Payment payment) {
        this.payment = payment;
        this.planningId = planningId;
    }

    @Override
    public Boolean call() throws Exception {
        payment.setValue(-payment.getValue());
        synchronized (Container.planningRepository) {
            synchronized (Container.paymentRepository) {
                Container.planningRepository.deleteById(planningId);
                Container.paymentRepository.save(payment);
            }
        }

        return true;
    }
}

