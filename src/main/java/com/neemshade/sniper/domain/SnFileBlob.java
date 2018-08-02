package com.neemshade.sniper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Objects;

/**
 * A SnFileBlob.
 */
@Entity
@Table(name = "sn_file_blob")
public class SnFileBlob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "file_content")
    private Blob fileContent;

    @Column(name = "file_content_content_type")
    private String fileContentContentType;

    @OneToOne(mappedBy = "snFileBlob")
    @JsonIgnore
    private SnFile snFile;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Blob getFileContent() {
        return fileContent;
    }

    public SnFileBlob fileContent(Blob fileContent) {
        this.fileContent = fileContent;
        return this;
    }

    public void setFileContent(Blob fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContentContentType() {
        return fileContentContentType;
    }

    public SnFileBlob fileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
        return this;
    }

    public void setFileContentContentType(String fileContentContentType) {
        this.fileContentContentType = fileContentContentType;
    }

    public SnFile getSnFile() {
        return snFile;
    }

    public SnFileBlob snFile(SnFile snFile) {
        this.snFile = snFile;
        return this;
    }

    public void setSnFile(SnFile snFile) {
        this.snFile = snFile;
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
        SnFileBlob snFileBlob = (SnFileBlob) o;
        if (snFileBlob.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), snFileBlob.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SnFileBlob{" +
            "id=" + getId() +
            ", fileContent='" + getFileContent() + "'" +
            ", fileContentContentType='" + getFileContentContentType() + "'" +
            "}";
    }
}
