package utils.domain;


import java.io.Serializable;

public class PayResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer paymentId;

    public PayResponse(Integer payment) {
        this.paymentId = paymentId;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }
}
