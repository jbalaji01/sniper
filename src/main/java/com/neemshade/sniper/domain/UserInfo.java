package com.neemshade.sniper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UserInfo.
 */
@Entity
@Table(name = "user_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_code")
    private String empCode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "date_of_join")
    private LocalDate dateOfJoin;

    @Column(name = "bank_info")
    private String bankInfo;

    @Column(name = "pan")
    private String pan;

    @Column(name = "addr")
    private String addr;

    @Column(name = "city")
    private String city;

    @Column(name = "last_login")
    private Instant lastLogin;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "uploader")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SnFile> snFiles = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> ownerTasks = new HashSet<>();

    @OneToMany(mappedBy = "transcript")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> transcriptTasks = new HashSet<>();

    @OneToMany(mappedBy = "editor")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> editorTasks = new HashSet<>();

    @OneToMany(mappedBy = "manager")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> managerTasks = new HashSet<>();

    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TaskHistory> taskHistories = new HashSet<>();

    @ManyToOne
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpCode() {
        return empCode;
    }

    public UserInfo empCode(String empCode) {
        this.empCode = empCode;
        return this;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getPhone() {
        return phone;
    }

    public UserInfo phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public UserInfo dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfJoin() {
        return dateOfJoin;
    }

    public UserInfo dateOfJoin(LocalDate dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
        return this;
    }

    public void setDateOfJoin(LocalDate dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }

    public String getBankInfo() {
        return bankInfo;
    }

    public UserInfo bankInfo(String bankInfo) {
        this.bankInfo = bankInfo;
        return this;
    }

    public void setBankInfo(String bankInfo) {
        this.bankInfo = bankInfo;
    }

    public String getPan() {
        return pan;
    }

    public UserInfo pan(String pan) {
        this.pan = pan;
        return this;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAddr() {
        return addr;
    }

    public UserInfo addr(String addr) {
        this.addr = addr;
        return this;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCity() {
        return city;
    }

    public UserInfo city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    public UserInfo lastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }

    public void setLastLogin(Instant lastLogin) {
        this.lastLogin = lastLogin;
    }

    public User getUser() {
        return user;
    }

    public UserInfo user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<SnFile> getSnFiles() {
        return snFiles;
    }

    public UserInfo snFiles(Set<SnFile> snFiles) {
        this.snFiles = snFiles;
        return this;
    }

    public UserInfo addSnFile(SnFile snFile) {
        this.snFiles.add(snFile);
        snFile.setUploader(this);
        return this;
    }

    public UserInfo removeSnFile(SnFile snFile) {
        this.snFiles.remove(snFile);
        snFile.setUploader(null);
        return this;
    }

    public void setSnFiles(Set<SnFile> snFiles) {
        this.snFiles = snFiles;
    }

    public Set<Task> getOwnerTasks() {
        return ownerTasks;
    }

    public UserInfo ownerTasks(Set<Task> tasks) {
        this.ownerTasks = tasks;
        return this;
    }

    public UserInfo addOwnerTask(Task task) {
        this.ownerTasks.add(task);
        task.setOwner(this);
        return this;
    }

    public UserInfo removeOwnerTask(Task task) {
        this.ownerTasks.remove(task);
        task.setOwner(null);
        return this;
    }

    public void setOwnerTasks(Set<Task> tasks) {
        this.ownerTasks = tasks;
    }

    public Set<Task> getTranscriptTasks() {
        return transcriptTasks;
    }

    public UserInfo transcriptTasks(Set<Task> tasks) {
        this.transcriptTasks = tasks;
        return this;
    }

    public UserInfo addTranscriptTask(Task task) {
        this.transcriptTasks.add(task);
        task.setTranscript(this);
        return this;
    }

    public UserInfo removeTranscriptTask(Task task) {
        this.transcriptTasks.remove(task);
        task.setTranscript(null);
        return this;
    }

    public void setTranscriptTasks(Set<Task> tasks) {
        this.transcriptTasks = tasks;
    }

    public Set<Task> getEditorTasks() {
        return editorTasks;
    }

    public UserInfo editorTasks(Set<Task> tasks) {
        this.editorTasks = tasks;
        return this;
    }

    public UserInfo addEditorTask(Task task) {
        this.editorTasks.add(task);
        task.setEditor(this);
        return this;
    }

    public UserInfo removeEditorTask(Task task) {
        this.editorTasks.remove(task);
        task.setEditor(null);
        return this;
    }

    public void setEditorTasks(Set<Task> tasks) {
        this.editorTasks = tasks;
    }

    public Set<Task> getManagerTasks() {
        return managerTasks;
    }

    public UserInfo managerTasks(Set<Task> tasks) {
        this.managerTasks = tasks;
        return this;
    }

    public UserInfo addManagerTask(Task task) {
        this.managerTasks.add(task);
        task.setManager(this);
        return this;
    }

    public UserInfo removeManagerTask(Task task) {
        this.managerTasks.remove(task);
        task.setManager(null);
        return this;
    }

    public void setManagerTasks(Set<Task> tasks) {
        this.managerTasks = tasks;
    }

    public Set<TaskHistory> getTaskHistories() {
        return taskHistories;
    }

    public UserInfo taskHistories(Set<TaskHistory> taskHistories) {
        this.taskHistories = taskHistories;
        return this;
    }

    public UserInfo addTaskHistory(TaskHistory taskHistory) {
        this.taskHistories.add(taskHistory);
        taskHistory.setUserInfo(this);
        return this;
    }

    public UserInfo removeTaskHistory(TaskHistory taskHistory) {
        this.taskHistories.remove(taskHistory);
        taskHistory.setUserInfo(null);
        return this;
    }

    public void setTaskHistories(Set<TaskHistory> taskHistories) {
        this.taskHistories = taskHistories;
    }

    public Company getCompany() {
        return company;
    }

    public UserInfo company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        UserInfo userInfo = (UserInfo) o;
        if (userInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "id=" + getId() +
            ", empCode='" + getEmpCode() + "'" +
            ", phone='" + getPhone() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", dateOfJoin='" + getDateOfJoin() + "'" +
            ", bankInfo='" + getBankInfo() + "'" +
            ", pan='" + getPan() + "'" +
            ", addr='" + getAddr() + "'" +
            ", city='" + getCity() + "'" +
            ", lastLogin='" + getLastLogin() + "'" +
            "}";
    }
}
