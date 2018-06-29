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
 * A Hospital.
 */
@Entity
@Table(name = "hospital")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Hospital implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "city")
    private String city;

    @Column(name = "template_count")
    private Integer templateCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "chosen_factor")
    private ChosenFactor chosenFactor;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "hospital")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> tasks = new HashSet<>();

    @ManyToMany(mappedBy = "hospitals")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Doctor> doctors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public Hospital hospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
        return this;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getCity() {
        return city;
    }

    public Hospital city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getTemplateCount() {
        return templateCount;
    }

    public Hospital templateCount(Integer templateCount) {
        this.templateCount = templateCount;
        return this;
    }

    public void setTemplateCount(Integer templateCount) {
        this.templateCount = templateCount;
    }

    public ChosenFactor getChosenFactor() {
        return chosenFactor;
    }

    public Hospital chosenFactor(ChosenFactor chosenFactor) {
        this.chosenFactor = chosenFactor;
        return this;
    }

    public void setChosenFactor(ChosenFactor chosenFactor) {
        this.chosenFactor = chosenFactor;
    }

    public String getNotes() {
        return notes;
    }

    public Hospital notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Hospital tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public Hospital addTask(Task task) {
        this.tasks.add(task);
        task.setHospital(this);
        return this;
    }

    public Hospital removeTask(Task task) {
        this.tasks.remove(task);
        task.setHospital(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Doctor> getDoctors() {
        return doctors;
    }

    public Hospital doctors(Set<Doctor> doctors) {
        this.doctors = doctors;
        return this;
    }

    public Hospital addDoctor(Doctor doctor) {
        this.doctors.add(doctor);
        doctor.getHospitals().add(this);
        return this;
    }

    public Hospital removeDoctor(Doctor doctor) {
        this.doctors.remove(doctor);
        doctor.getHospitals().remove(this);
        return this;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
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
        Hospital hospital = (Hospital) o;
        if (hospital.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hospital.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Hospital{" +
            "id=" + getId() +
            ", hospitalName='" + getHospitalName() + "'" +
            ", city='" + getCity() + "'" +
            ", templateCount=" + getTemplateCount() +
            ", chosenFactor='" + getChosenFactor() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
