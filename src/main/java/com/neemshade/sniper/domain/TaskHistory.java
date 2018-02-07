package com.neemshade.sniper.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.neemshade.sniper.domain.enumeration.TaskStatus;

/**
 * A TaskHistory.
 */
@Entity
@Table(name = "task_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaskHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private TaskStatus taskStatus;

    @Column(name = "punch_time")
    private Instant punchTime;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    private Task task;

    @ManyToOne
    private UserInfo userInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public TaskHistory taskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
        return this;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Instant getPunchTime() {
        return punchTime;
    }

    public TaskHistory punchTime(Instant punchTime) {
        this.punchTime = punchTime;
        return this;
    }

    public void setPunchTime(Instant punchTime) {
        this.punchTime = punchTime;
    }

    public String getNotes() {
        return notes;
    }

    public TaskHistory notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Task getTask() {
        return task;
    }

    public TaskHistory task(Task task) {
        this.task = task;
        return this;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public TaskHistory userInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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
        TaskHistory taskHistory = (TaskHistory) o;
        if (taskHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskHistory{" +
            "id=" + getId() +
            ", taskStatus='" + getTaskStatus() + "'" +
            ", punchTime='" + getPunchTime() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
