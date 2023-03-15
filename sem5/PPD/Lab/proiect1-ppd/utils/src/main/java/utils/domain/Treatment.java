package utils.domain;


import java.io.Serializable;


public class Treatment implements Serializable {
    private Integer id;
    private Integer cost;
    private Integer duration;

    public Treatment(Integer cost, Integer duration) {
        this.cost = cost;
        this.duration = duration;
    }

    public Treatment(Integer id, Integer cost, Integer duration) {
        this.id = id;
        this.cost = cost;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "id=" + id +
                ", cost=" + cost +
                ", duration=" + duration +
                '}';
    }
}
