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

/**
 * A TaskGroup.
 */
@Entity
@Table(name = "task_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaskGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "created_time")
    private Instant createdTime;

    @OneToMany(mappedBy = "taskGroup")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> tasks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public TaskGroup groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public TaskGroup createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public TaskGroup tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public TaskGroup addTask(Task task) {
        this.tasks.add(task);
        task.setTaskGroup(this);
        return this;
    }

    public TaskGroup removeTask(Task task) {
        this.tasks.remove(task);
        task.setTaskGroup(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
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
        TaskGroup taskGroup = (TaskGroup) o;
        if (taskGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskGroup{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            "}";
    }
}
