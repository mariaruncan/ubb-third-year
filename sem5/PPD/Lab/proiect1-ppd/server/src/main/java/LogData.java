import utils.domain.Planning;

import java.util.List;

public class LogData {
    private Integer expectedAmount;
    private Integer actualAmount;
    private List<Planning> unpaidPlannings;

    public LogData(Integer expectedAmount, Integer actualAmount, List<Planning> unpaidPlannings) {
        this.expectedAmount = expectedAmount;
        this.actualAmount = actualAmount;
        this.unpaidPlannings = unpaidPlannings;
    }

    public Integer getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(Integer expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    public Integer getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Integer actualAmount) {
        this.actualAmount = actualAmount;
    }

    public List<Planning> getUnpaidPlannings() {
        return unpaidPlannings;
    }

    public void setUnpaidPlannings(List<Planning> unpaidPlannings) {
        this.unpaidPlannings = unpaidPlannings;
    }

    @Override
    public String toString() {
        return "LogData{" +
                "expectedAmount=" + expectedAmount +
                ", actualAmount=" + actualAmount +
                ", unpaidPlannings=" + unpaidPlannings +
                '}';
    }
}
