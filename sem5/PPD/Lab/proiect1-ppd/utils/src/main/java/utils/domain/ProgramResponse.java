package utils.domain;

import java.io.Serializable;

public class ProgramResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer planningId;
    private Integer cost;

    public ProgramResponse(Integer planningId, Integer cost) {
        this.planningId = planningId;
        this.cost = cost;
    }

    public Integer getPlanningId() {
        return planningId;
    }

    public void setPlanningId(Integer planningId) {
        this.planningId = planningId;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
