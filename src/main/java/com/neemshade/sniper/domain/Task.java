package com.neemshade.sniper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.neemshade.sniper.domain.enumeration.TaskStatus;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_title")
    private String taskTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private TaskStatus taskStatus;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "has_pm_signed_off")
    private Boolean hasPMSignedOff;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "peck_order")
    private Integer peckOrder;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "task")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TaskHistory> taskHistories = new HashSet<>();

    @ManyToOne
    private Company company;

    @ManyToOne
    private TaskGroup taskGroup;

    @ManyToOne
    private UserInfo owner;

    @ManyToOne
    private UserInfo transcript;

    @ManyToOne
    private UserInfo editor;

    @ManyToOne
    private UserInfo manager;

    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private Hospital hospital;

    @ManyToMany(mappedBy = "tasks")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SnFile> snFiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public Task taskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
        return this;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public Task taskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
        return this;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public Task createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Boolean isHasPMSignedOff() {
        return hasPMSignedOff;
    }

    public Task hasPMSignedOff(Boolean hasPMSignedOff) {
        this.hasPMSignedOff = hasPMSignedOff;
        return this;
    }

    public void setHasPMSignedOff(Boolean hasPMSignedOff) {
        this.hasPMSignedOff = hasPMSignedOff;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Task isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getPeckOrder() {
        return peckOrder;
    }

    public Task peckOrder(Integer peckOrder) {
        this.peckOrder = peckOrder;
        return this;
    }

    public void setPeckOrder(Integer peckOrder) {
        this.peckOrder = peckOrder;
    }

    public String getNotes() {
        return notes;
    }

    public Task notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<TaskHistory> getTaskHistories() {
        return taskHistories;
    }

    public Task taskHistories(Set<TaskHistory> taskHistories) {
        this.taskHistories = taskHistories;
        return this;
    }

    public Task addTaskHistory(TaskHistory taskHistory) {
        this.taskHistories.add(taskHistory);
        taskHistory.setTask(this);
        return this;
    }

    public Task removeTaskHistory(TaskHistory taskHistory) {
        this.taskHistories.remove(taskHistory);
        taskHistory.setTask(null);
        return this;
    }

    public void setTaskHistories(Set<TaskHistory> taskHistories) {
        this.taskHistories = taskHistories;
    }

    public Company getCompany() {
        return company;
    }

    public Task company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public TaskGroup getTaskGroup() {
        return taskGroup;
    }

    public Task taskGroup(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
        return this;
    }

    public void setTaskGroup(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
    }

    public UserInfo getOwner() {
        return owner;
    }

    public Task owner(UserInfo userInfo) {
        this.owner = userInfo;
        return this;
    }

    public void setOwner(UserInfo userInfo) {
        this.owner = userInfo;
    }

    public UserInfo getTranscript() {
        return transcript;
    }

    public Task transcript(UserInfo userInfo) {
        this.transcript = userInfo;
        return this;
    }

    public void setTranscript(UserInfo userInfo) {
        this.transcript = userInfo;
    }

    public UserInfo getEditor() {
        return editor;
    }

    public Task editor(UserInfo userInfo) {
        this.editor = userInfo;
        return this;
    }

    public void setEditor(UserInfo userInfo) {
        this.editor = userInfo;
    }

    public UserInfo getManager() {
        return manager;
    }

    public Task manager(UserInfo userInfo) {
        this.manager = userInfo;
        return this;
    }

    public void setManager(UserInfo userInfo) {
        this.manager = userInfo;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Task doctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public Task hospital(Hospital hospital) {
        this.hospital = hospital;
        return this;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Set<SnFile> getSnFiles() {
        return snFiles;
    }

    public Task snFiles(Set<SnFile> snFiles) {
        this.snFiles = snFiles;
        return this;
    }

    public Task addSnFile(SnFile snFile) {
        this.snFiles.add(snFile);
        snFile.getTasks().add(this);
        return this;
    }

    public Task removeSnFile(SnFile snFile) {
        this.snFiles.remove(snFile);
        snFile.getTasks().remove(this);
        return this;
    }

    public void setSnFiles(Set<SnFile> snFiles) {
        this.snFiles = snFiles;
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
        Task task = (Task) o;
        if (task.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", taskTitle='" + getTaskTitle() + "'" +
            ", taskStatus='" + getTaskStatus() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", hasPMSignedOff='" + isHasPMSignedOff() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", peckOrder=" + getPeckOrder() +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
