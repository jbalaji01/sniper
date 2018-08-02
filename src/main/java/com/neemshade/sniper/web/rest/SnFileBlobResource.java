package com.neemshade.sniper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.neemshade.sniper.domain.SnFileBlob;

import com.neemshade.sniper.repository.SnFileBlobRepository;
import com.neemshade.sniper.service.SnFileBlobService;
import com.neemshade.sniper.web.rest.errors.BadRequestAlertException;
import com.neemshade.sniper.web.rest.util.HeaderUtil;
import com.neemshade.sniper.service.dto.SnFileBlobDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * REST controller for managing SnFileBlob.
 */
@RestController
@RequestMapping("/api")
public class SnFileBlobResource {

    private final Logger log = LoggerFactory.getLogger(SnFileBlobResource.class);

    private static final String ENTITY_NAME = "snFileBlob";

    private final SnFileBlobRepository snFileBlobRepository;
    private final SnFileBlobService snFileBlobService;

    public SnFileBlobResource(SnFileBlobRepository snFileBlobRepository, SnFileBlobService snFileBlobService) {
        this.snFileBlobRepository = snFileBlobRepository;
        this.snFileBlobService = snFileBlobService;
    }

    /**
     * POST  /sn-file-blobs : Create a new snFileBlob.
     *
     * @param snFileBlob the snFileBlob to create
     * @return the ResponseEntity with status 201 (Created) and with body the new snFileBlob, or with status 400 (Bad Request) if the snFileBlob has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sn-file-blobs")
    @Timed
    public ResponseEntity<SnFileBlobDTO> createSnFileBlob(@RequestBody SnFileBlobDTO snFileBlob) throws URISyntaxException {
        log.debug("REST request to create SnFileBlob : {}", snFileBlob);
        if (snFileBlob.getId() != null) {
            throw new BadRequestAlertException("A new snFileBlob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SnFileBlobDTO result = snFileBlobService.save(snFileBlob);
        return ResponseEntity.created(new URI("/api/sn-file-blobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sn-file-blobs : Updates an existing snFileBlob.
     *
     * @param snFileBlob the snFileBlob to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated snFileBlob,
     * or with status 400 (Bad Request) if the snFileBlob is not valid,
     * or with status 500 (Internal Server Error) if the snFileBlob couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sn-file-blobs")
    @Timed
    public ResponseEntity<SnFileBlobDTO> updateSnFileBlob(@RequestBody SnFileBlobDTO snFileBlob) throws URISyntaxException {
        log.debug("REST request to update SnFileBlob : {}", snFileBlob);
        if (snFileBlob.getId() == null) {
            return createSnFileBlob(snFileBlob);
        }
        SnFileBlobDTO result = snFileBlobService.save(snFileBlob);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, snFileBlob.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sn-file-blobs : get all the snFileBlobs.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of snFileBlobs in body
     */
    @GetMapping("/sn-file-blobs")
    @Timed
    public List<SnFileBlobDTO> getAllSnFileBlobs(@RequestParam(required = false) String filter) {
        Stream<SnFileBlob> fileBlobStream = snFileBlobRepository.findAll().stream();
        if ("snfile-is-null".equals(filter)) {
            log.debug("REST request to get all SnFileBlobs where snFile is null");
            fileBlobStream = snFileBlobRepository.findAll().stream()
                .filter(snFileBlob -> snFileBlob.getSnFile() == null);
        }
        log.debug("REST request to get all SnFileBlobs");
        return fileBlobStream
            .map(snFileBlobService::toDto)
            .collect(Collectors.toList());
    }

    /**
     * GET  /sn-file-blobs/:id : get the "id" snFileBlob.
     *
     * @param id the id of the snFileBlob to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the snFileBlob, or with status 404 (Not Found)
     */
    @GetMapping("/sn-file-blobs/{id}")
    @Timed
    public ResponseEntity<SnFileBlobDTO> getSnFileBlob(@PathVariable Long id) {
        log.debug("REST request to get SnFileBlob : {}", id);
        SnFileBlobDTO snFileBlob = snFileBlobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(snFileBlob));
    }

    /**
     * DELETE  /sn-file-blobs/:id : delete the "id" snFileBlob.
     *
     * @param id the id of the snFileBlob to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sn-file-blobs/{id}")
    @Timed
    public ResponseEntity<Void> deleteSnFileBlob(@PathVariable Long id) {
        log.debug("REST request to delete SnFileBlob : {}", id);
        snFileBlobRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
