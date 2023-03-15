package utils.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


public class Planning implements Serializable {
    private Integer id;
    private String name;
    private String cnp;
    private LocalDateTime date;
    private Integer location;
    private Treatment treatment;
    private LocalDateTime treatmentDate;
    private Integer hour;

    public Planning(Integer id, String name, String cnp, LocalDateTime date, Integer location, Treatment treatment, LocalDateTime treatmentDate, Integer hour) {
        this.id = id;
        this.name = name;
        this.cnp = cnp;
        this.date = date;
        this.location = location;
        this.treatment = treatment;
        this.treatmentDate = treatmentDate;
        this.hour = hour;
    }

    public Planning(String name, String cnp, LocalDateTime date, Integer location, Treatment treatment, LocalDateTime treatmentDate, Integer hour) {
        this.name = name;
        this.cnp = cnp;
        this.date = date;
        this.location = location;
        this.treatment = treatment;
        this.treatmentDate = treatmentDate;
        this.hour = hour;
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

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
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
        return "Planning{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cnp='" + cnp + '\'' +
                ", date=" + date +
                ", location=" + location +
                ", treatment=" + treatment +
                ", treatmentDate=" + treatmentDate +
                ", hour=" + hour +
                '}';
    }
}
