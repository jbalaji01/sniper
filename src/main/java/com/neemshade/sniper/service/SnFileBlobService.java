package com.neemshade.sniper.service;

import java.io.InputStream;
import java.sql.Blob;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.neemshade.sniper.domain.SnFileBlob;
import com.neemshade.sniper.repository.SnFileBlobRepository;
import com.neemshade.sniper.service.dto.SnFileBlobDTO;
import org.apache.commons.io.IOUtils;
import org.hibernate.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SnFileBlobService {
    @PersistenceContext
    private EntityManager em;

    private final SnFileBlobRepository snFileBlobRepository;

    @Autowired
    public SnFileBlobService(SnFileBlobRepository snFileBlobRepository) {
        this.snFileBlobRepository = snFileBlobRepository;
    }

    public SnFileBlobDTO save(SnFileBlobDTO snFileBlobDto) {
        SnFileBlob snFileBlob = fromDto(snFileBlobDto);

        SnFileBlob savedFileBlob = snFileBlobRepository.save(snFileBlob);
        snFileBlobDto.setId(savedFileBlob.getId());
        return snFileBlobDto;
    }

    @Transactional(readOnly = true)
    public SnFileBlobDTO findOne(Long id) {
        SnFileBlob snFileBlob = snFileBlobRepository.findOne(id);
        if (snFileBlob == null) {
            return null;
        }
        return toDto(snFileBlob);
    }

    public SnFileBlob fromDto(SnFileBlobDTO snFileBlobDto) {
        SnFileBlob snFileBlob = new SnFileBlob();
        snFileBlob.setId(snFileBlobDto.getId());
        snFileBlob.setFileContentContentType(snFileBlobDto.getFileContentContentType());
        snFileBlob.setFileContent(createBlob(snFileBlobDto.getFileContent()));
        return snFileBlob;
    }

    public SnFileBlobDTO toDto(SnFileBlob snFileBlob) {
        SnFileBlobDTO snFileBlobDTO = new SnFileBlobDTO();
        snFileBlobDTO.setId(snFileBlob.getId());
        snFileBlobDTO.setFileContentContentType(snFileBlob.getFileContentContentType());
        try {
            InputStream binaryStream = snFileBlob.getFileContent().getBinaryStream();
            snFileBlobDTO.setFileContent(IOUtils.toByteArray(binaryStream));
            IOUtils.closeQuietly(binaryStream);
        } catch (Exception e) {
            throw new RuntimeException("Could not read file content", e);
        }
        return snFileBlobDTO;
    }

    private Blob createBlob(byte[] content) {
        return ((Session) em.getDelegate()).getLobHelper().createBlob(content);
    }
}
