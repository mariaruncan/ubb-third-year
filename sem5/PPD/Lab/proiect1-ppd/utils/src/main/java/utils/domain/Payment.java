package utils.domain;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


public class Payment implements Serializable {
    private Integer id;
    private Integer value;
    private String cnp;
    private LocalDateTime date;
    private Integer planningId;

    public Payment(Integer id, Integer value, String cnp, LocalDateTime date, Integer planningId) {
        this.id = id;
        this.value = value;
        this.cnp = cnp;
        this.date = date;
        this.planningId = planningId;
    }

    public Payment(Integer value, String cnp, LocalDateTime date, Integer planningId) {
        this.value = value;
        this.cnp = cnp;
        this.date = date;
        this.planningId = planningId;
    }

    public Integer getPlanningId() {
        return planningId;
    }

    public void setPlanningId(Integer planningId) {
        this.planningId = planningId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", value=" + value +
                ", cnp='" + cnp + '\'' +
                ", date=" + date +
                ", planningId=" + planningId +
                '}';
    }
}
