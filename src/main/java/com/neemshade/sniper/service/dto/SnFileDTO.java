package com.neemshade.sniper.service.dto;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.neemshade.sniper.domain.Patient;
import com.neemshade.sniper.domain.Task;
import com.neemshade.sniper.domain.UserInfo;
import com.neemshade.sniper.domain.enumeration.ChosenFactor;

public class SnFileDTO {
    private Long id;
    private String filePath;
    private String fileName;
    private String fileExt;
    private String origin;
    private Boolean isInput;
    private Boolean isAudio;
    private Instant uploadedTime;
    private Integer actualTimeFrame;
    private Integer adjustedTimeFrame;
    private Integer finalTimeFrame;
    private Integer wsActualLineCount;
    private Integer wsAdjustedLineCount;
    private Integer wsFinalLineCount;
    private Integer wosActualLineCount;
    private Integer wosAdjustedLineCount;
    private Integer wosFinalLineCount;
    private ChosenFactor chosenFactor;
    private Integer peckOrder;
    private SnFileBlobDTO snFileBlob;
    private Set<Patient> patients = new HashSet<>();
    private Set<Task> tasks = new HashSet<>();
    private UserInfo uploader;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Boolean getInput() {
        return isInput;
    }

    public void setInput(Boolean input) {
        isInput = input;
    }

    public Boolean getAudio() {
        return isAudio;
    }

    public void setAudio(Boolean audio) {
        isAudio = audio;
    }

    public Instant getUploadedTime() {
        return uploadedTime;
    }

    public void setUploadedTime(Instant uploadedTime) {
        this.uploadedTime = uploadedTime;
    }

    public Integer getActualTimeFrame() {
        return actualTimeFrame;
    }

    public void setActualTimeFrame(Integer actualTimeFrame) {
        this.actualTimeFrame = actualTimeFrame;
    }

    public Integer getAdjustedTimeFrame() {
        return adjustedTimeFrame;
    }

    public void setAdjustedTimeFrame(Integer adjustedTimeFrame) {
        this.adjustedTimeFrame = adjustedTimeFrame;
    }

    public Integer getFinalTimeFrame() {
        return finalTimeFrame;
    }

    public void setFinalTimeFrame(Integer finalTimeFrame) {
        this.finalTimeFrame = finalTimeFrame;
    }

    public Integer getWsActualLineCount() {
        return wsActualLineCount;
    }

    public void setWsActualLineCount(Integer wsActualLineCount) {
        this.wsActualLineCount = wsActualLineCount;
    }

    public Integer getWsAdjustedLineCount() {
        return wsAdjustedLineCount;
    }

    public void setWsAdjustedLineCount(Integer wsAdjustedLineCount) {
        this.wsAdjustedLineCount = wsAdjustedLineCount;
    }

    public Integer getWsFinalLineCount() {
        return wsFinalLineCount;
    }

    public void setWsFinalLineCount(Integer wsFinalLineCount) {
        this.wsFinalLineCount = wsFinalLineCount;
    }

    public Integer getWosActualLineCount() {
        return wosActualLineCount;
    }

    public void setWosActualLineCount(Integer wosActualLineCount) {
        this.wosActualLineCount = wosActualLineCount;
    }

    public Integer getWosAdjustedLineCount() {
        return wosAdjustedLineCount;
    }

    public void setWosAdjustedLineCount(Integer wosAdjustedLineCount) {
        this.wosAdjustedLineCount = wosAdjustedLineCount;
    }

    public Integer getWosFinalLineCount() {
        return wosFinalLineCount;
    }

    public void setWosFinalLineCount(Integer wosFinalLineCount) {
        this.wosFinalLineCount = wosFinalLineCount;
    }

    public ChosenFactor getChosenFactor() {
        return chosenFactor;
    }

    public void setChosenFactor(ChosenFactor chosenFactor) {
        this.chosenFactor = chosenFactor;
    }

    public Integer getPeckOrder() {
        return peckOrder;
    }

    public void setPeckOrder(Integer peckOrder) {
        this.peckOrder = peckOrder;
    }

    public SnFileBlobDTO getSnFileBlob() {
        return snFileBlob;
    }

    public void setSnFileBlob(SnFileBlobDTO snFileBlob) {
        this.snFileBlob = snFileBlob;
    }

    public Set<Patient> getPatients() {
        return patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public UserInfo getUploader() {
        return uploader;
    }

    public void setUploader(UserInfo uploader) {
        this.uploader = uploader;
    }

    @Override
    public String toString() {
        return "SnFileDTO{" +
            "id=" + id +
            ", filePath='" + filePath + '\'' +
            ", fileName='" + fileName + '\'' +
            ", fileExt='" + fileExt + '\'' +
            ", origin='" + origin + '\'' +
            ", isInput=" + isInput +
            ", isAudio=" + isAudio +
            ", uploadedTime=" + uploadedTime +
            ", actualTimeFrame=" + actualTimeFrame +
            ", adjustedTimeFrame=" + adjustedTimeFrame +
            ", finalTimeFrame=" + finalTimeFrame +
            ", wsActualLineCount=" + wsActualLineCount +
            ", wsAdjustedLineCount=" + wsAdjustedLineCount +
            ", wsFinalLineCount=" + wsFinalLineCount +
            ", wosActualLineCount=" + wosActualLineCount +
            ", wosAdjustedLineCount=" + wosAdjustedLineCount +
            ", wosFinalLineCount=" + wosFinalLineCount +
            ", chosenFactor=" + chosenFactor +
            ", peckOrder=" + peckOrder +
            ", snFileBlob=" + snFileBlob +
            ", patients=" + patients +
            ", tasks=" + tasks +
            ", uploader=" + uploader +
            '}';
    }
}
