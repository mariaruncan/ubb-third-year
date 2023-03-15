package utils.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class PlanningDto implements Serializable {
    private static final long serialVersionUID = 2L;
    private Integer id;
    private String name;
    private String cnp;
    private LocalDateTime date;
    private Integer location;
    private Integer treatment;
    private LocalDateTime treatmentDate;
    private Integer hour;
    private Boolean cancel;

    public PlanningDto(Integer id, String name, String cnp, LocalDateTime date, Integer location, Integer treatment, LocalDateTime treatmentDate, Integer hour, Boolean cancel) {
        this.id = id;
        this.name = name;
        this.cnp = cnp;
        this.date = date;
        this.location = location;
        this.treatment = treatment;
        this.treatmentDate = treatmentDate;
        this.hour = hour;
        this.cancel = cancel;
    }

    public PlanningDto(String name, String cnp, LocalDateTime date, Integer location, Integer treatment, LocalDateTime treatmentDate, Integer hour, Boolean cancel) {
        this.name = name;
        this.cnp = cnp;
        this.date = date;
        this.location = location;
        this.treatment = treatment;
        this.treatmentDate = treatmentDate;
        this.hour = hour;
        this.cancel = cancel;
    }

    public Boolean getCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getTreatment() {
        return treatment;
    }

    public void setTreatment(Integer treatment) {
        this.treatment = treatment;
    }

    public LocalDateTime getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(LocalDateTime treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "PlanningDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cnp='" + cnp + '\'' +
                ", date=" + date +
                ", location=" + location +
                ", treatment=" + treatment +
                ", treatmentDate=" + treatmentDate +
                ", hour=" + hour +
                ", cancel=" + cancel +
                '}';
    }
}
