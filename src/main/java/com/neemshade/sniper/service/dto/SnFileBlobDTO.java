package com.neemshade.sniper.service.dto;

public class SnFileBlobDTO {

    private Long id;
    private byte[] fileContent;
    private String fileContentContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContentContentType() {
        return fileContentContentType;
    }

    public void setFileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
    }

    @Override
    public String toString() {
        return "SnFileBlobDTO{" +
            "id=" + id +
            ", fileContentContentType='" + fileContentContentType + '\'' +
            '}';
    }
}
