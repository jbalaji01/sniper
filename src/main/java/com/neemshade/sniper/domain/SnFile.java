package com.neemshade.sniper.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.neemshade.sniper.domain.enumeration.ChosenFactor;

/**
 * A SnFile.
 */
@Entity
@Table(name = "sn_file")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SnFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "origin")
    private String origin;

    @Column(name = "is_input")
    private Boolean isInput;

    @Column(name = "is_audio")
    private Boolean isAudio;

    @Column(name = "uploaded_time")
    private Instant uploadedTime;

    @Column(name = "actual_time_frame")
    private Integer actualTimeFrame;

    @Column(name = "adjusted_time_frame")
    private Integer adjustedTimeFrame;

    @Column(name = "final_time_frame")
    private Integer finalTimeFrame;

    @Column(name = "ws_actual_line_count")
    private Integer wsActualLineCount;

    @Column(name = "ws_adjusted_line_count")
    private Integer wsAdjustedLineCount;

    @Column(name = "ws_final_line_count")
    private Integer wsFinalLineCount;

    @Column(name = "wos_actual_line_count")
    private Integer wosActualLineCount;

    @Column(name = "wos_adjusted_line_count")
    private Integer wosAdjustedLineCount;

    @Column(name = "wos_final_line_count")
    private Integer wosFinalLineCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "chosen_factor")
    private ChosenFactor chosenFactor;

    @Column(name = "peck_order")
    private Integer peckOrder;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "sn_file_patient",
               joinColumns = @JoinColumn(name="sn_files_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="patients_id", referencedColumnName="id"))
    private Set<Patient> patients = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "sn_file_task",
               joinColumns = @JoinColumn(name="sn_files_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="tasks_id", referencedColumnName="id"))
    private Set<Task> tasks = new HashSet<>();

    @ManyToOne
    private UserInfo uploader;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public SnFile filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public SnFile fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public SnFile fileExt(String fileExt) {
        this.fileExt = fileExt;
        return this;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public SnFile fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getOrigin() {
        return origin;
    }

    public SnFile origin(String origin) {
        this.origin = origin;
        return this;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Boolean isIsInput() {
        return isInput;
    }

    public SnFile isInput(Boolean isInput) {
        this.isInput = isInput;
        return this;
    }

    public void setIsInput(Boolean isInput) {
        this.isInput = isInput;
    }

    public Boolean isIsAudio() {
        return isAudio;
    }

    public SnFile isAudio(Boolean isAudio) {
        this.isAudio = isAudio;
        return this;
    }

    public void setIsAudio(Boolean isAudio) {
        this.isAudio = isAudio;
    }

    public Instant getUploadedTime() {
        return uploadedTime;
    }

    public SnFile uploadedTime(Instant uploadedTime) {
        this.uploadedTime = uploadedTime;
        return this;
    }

    public void setUploadedTime(Instant uploadedTime) {
        this.uploadedTime = uploadedTime;
    }

    public Integer getActualTimeFrame() {
        return actualTimeFrame;
    }

    public SnFile actualTimeFrame(Integer actualTimeFrame) {
        this.actualTimeFrame = actualTimeFrame;
        return this;
    }

    public void setActualTimeFrame(Integer actualTimeFrame) {
        this.actualTimeFrame = actualTimeFrame;
    }

    public Integer getAdjustedTimeFrame() {
        return adjustedTimeFrame;
    }

    public SnFile adjustedTimeFrame(Integer adjustedTimeFrame) {
        this.adjustedTimeFrame = adjustedTimeFrame;
        return this;
    }

    public void setAdjustedTimeFrame(Integer adjustedTimeFrame) {
        this.adjustedTimeFrame = adjustedTimeFrame;
    }

    public Integer getFinalTimeFrame() {
        return finalTimeFrame;
    }

    public SnFile finalTimeFrame(Integer finalTimeFrame) {
        this.finalTimeFrame = finalTimeFrame;
        return this;
    }

    public void setFinalTimeFrame(Integer finalTimeFrame) {
        this.finalTimeFrame = finalTimeFrame;
    }

    public Integer getWsActualLineCount() {
        return wsActualLineCount;
    }

    public SnFile wsActualLineCount(Integer wsActualLineCount) {
        this.wsActualLineCount = wsActualLineCount;
        return this;
    }

    public void setWsActualLineCount(Integer wsActualLineCount) {
        this.wsActualLineCount = wsActualLineCount;
    }

    public Integer getWsAdjustedLineCount() {
        return wsAdjustedLineCount;
    }

    public SnFile wsAdjustedLineCount(Integer wsAdjustedLineCount) {
        this.wsAdjustedLineCount = wsAdjustedLineCount;
        return this;
    }

    public void setWsAdjustedLineCount(Integer wsAdjustedLineCount) {
        this.wsAdjustedLineCount = wsAdjustedLineCount;
    }

    public Integer getWsFinalLineCount() {
        return wsFinalLineCount;
    }

    public SnFile wsFinalLineCount(Integer wsFinalLineCount) {
        this.wsFinalLineCount = wsFinalLineCount;
        return this;
    }

    public void setWsFinalLineCount(Integer wsFinalLineCount) {
        this.wsFinalLineCount = wsFinalLineCount;
    }

    public Integer getWosActualLineCount() {
        return wosActualLineCount;
    }

    public SnFile wosActualLineCount(Integer wosActualLineCount) {
        this.wosActualLineCount = wosActualLineCount;
        return this;
    }

    public void setWosActualLineCount(Integer wosActualLineCount) {
        this.wosActualLineCount = wosActualLineCount;
    }

    public Integer getWosAdjustedLineCount() {
        return wosAdjustedLineCount;
    }

    public SnFile wosAdjustedLineCount(Integer wosAdjustedLineCount) {
        this.wosAdjustedLineCount = wosAdjustedLineCount;
        return this;
    }

    public void setWosAdjustedLineCount(Integer wosAdjustedLineCount) {
        this.wosAdjustedLineCount = wosAdjustedLineCount;
    }

    public Integer getWosFinalLineCount() {
        return wosFinalLineCount;
    }

    public SnFile wosFinalLineCount(Integer wosFinalLineCount) {
        this.wosFinalLineCount = wosFinalLineCount;
        return this;
    }

    public void setWosFinalLineCount(Integer wosFinalLineCount) {
        this.wosFinalLineCount = wosFinalLineCount;
    }

    public ChosenFactor getChosenFactor() {
        return chosenFactor;
    }

    public SnFile chosenFactor(ChosenFactor chosenFactor) {
        this.chosenFactor = chosenFactor;
        return this;
    }

    public void setChosenFactor(ChosenFactor chosenFactor) {
        this.chosenFactor = chosenFactor;
    }

    public Integer getPeckOrder() {
        return peckOrder;
    }

    public SnFile peckOrder(Integer peckOrder) {
        this.peckOrder = peckOrder;
        return this;
    }

    public void setPeckOrder(Integer peckOrder) {
        this.peckOrder = peckOrder;
    }

    public Set<Patient> getPatients() {
        return patients;
    }

    public SnFile patients(Set<Patient> patients) {
        this.patients = patients;
        return this;
    }

    public SnFile addPatient(Patient patient) {
        this.patients.add(patient);
        patient.getSnFiles().add(this);
        return this;
    }

    public SnFile removePatient(Patient patient) {
        this.patients.remove(patient);
        patient.getSnFiles().remove(this);
        return this;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public SnFile tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public SnFile addTask(Task task) {
        this.tasks.add(task);
        task.getSnFiles().add(this);
        return this;
    }

    public SnFile removeTask(Task task) {
        this.tasks.remove(task);
        task.getSnFiles().remove(this);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public UserInfo getUploader() {
        return uploader;
    }

    public SnFile uploader(UserInfo userInfo) {
        this.uploader = userInfo;
        return this;
    }

    public void setUploader(UserInfo userInfo) {
        this.uploader = userInfo;
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
        SnFile snFile = (SnFile) o;
        if (snFile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), snFile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SnFile{" +
            "id=" + getId() +
            ", filePath='" + getFilePath() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", fileExt='" + getFileExt() + "'" +
            ", fileSize=" + getFileSize() +
            ", origin='" + getOrigin() + "'" +
            ", isInput='" + isIsInput() + "'" +
            ", isAudio='" + isIsAudio() + "'" +
            ", uploadedTime='" + getUploadedTime() + "'" +
            ", actualTimeFrame=" + getActualTimeFrame() +
            ", adjustedTimeFrame=" + getAdjustedTimeFrame() +
            ", finalTimeFrame=" + getFinalTimeFrame() +
            ", wsActualLineCount=" + getWsActualLineCount() +
            ", wsAdjustedLineCount=" + getWsAdjustedLineCount() +
            ", wsFinalLineCount=" + getWsFinalLineCount() +
            ", wosActualLineCount=" + getWosActualLineCount() +
            ", wosAdjustedLineCount=" + getWosAdjustedLineCount() +
            ", wosFinalLineCount=" + getWosFinalLineCount() +
            ", chosenFactor='" + getChosenFactor() + "'" +
            ", peckOrder=" + getPeckOrder() +
            "}";
    }
}
