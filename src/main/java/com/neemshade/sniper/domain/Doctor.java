package com.neemshade.sniper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.neemshade.sniper.domain.enumeration.ChosenFactor;

/**
 * A Doctor.
 */
@Entity
@Table(name = "doctor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "city")
    private String city;

    @Column(name = "template_count")
    private Integer templateCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "chosen_factor")
    private ChosenFactor chosenFactor;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> tasks = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "doctor_hospital",
               joinColumns = @JoinColumn(name="doctors_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="hospitals_id", referencedColumnName="id"))
    private Set<Hospital> hospitals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Doctor doctorName(String doctorName) {
        this.doctorName = doctorName;
        return this;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getCity() {
        return city;
    }

    public Doctor city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getTemplateCount() {
        return templateCount;
    }

    public Doctor templateCount(Integer templateCount) {
        this.templateCount = templateCount;
        return this;
    }

    public void setTemplateCount(Integer templateCount) {
        this.templateCount = templateCount;
    }

    public ChosenFactor getChosenFactor() {
        return chosenFactor;
    }

    public Doctor chosenFactor(ChosenFactor chosenFactor) {
        this.chosenFactor = chosenFactor;
        return this;
    }

    public void setChosenFactor(ChosenFactor chosenFactor) {
        this.chosenFactor = chosenFactor;
    }

    public String getNotes() {
        return notes;
    }

    public Doctor notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Doctor tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public Doctor addTask(Task task) {
        this.tasks.add(task);
        task.setDoctor(this);
        return this;
    }

    public Doctor removeTask(Task task) {
        this.tasks.remove(task);
        task.setDoctor(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Hospital> getHospitals() {
        return hospitals;
    }

    public Doctor hospitals(Set<Hospital> hospitals) {
        this.hospitals = hospitals;
        return this;
    }

    public Doctor addHospital(Hospital hospital) {
        this.hospitals.add(hospital);
        hospital.getDoctors().add(this);
        return this;
    }

    public Doctor removeHospital(Hospital hospital) {
        this.hospitals.remove(hospital);
        hospital.getDoctors().remove(this);
        return this;
    }

    public void setHospitals(Set<Hospital> hospitals) {
        this.hospitals = hospitals;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Doctor doctor = (Doctor) o;
        if (doctor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), doctor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Doctor{" +
            "id=" + getId() +
            ", doctorName='" + getDoctorName() + "'" +
            ", city='" + getCity() + "'" +
            ", templateCount=" + getTemplateCount() +
            ", chosenFactor='" + getChosenFactor() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
