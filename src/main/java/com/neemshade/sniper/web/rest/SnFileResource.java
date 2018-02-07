package com.neemshade.sniper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.neemshade.sniper.domain.SnFile;
import com.neemshade.sniper.service.SnFileService;
import com.neemshade.sniper.web.rest.errors.BadRequestAlertException;
import com.neemshade.sniper.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SnFile.
 */
@RestController
@RequestMapping("/api")
public class SnFileResource {

    private final Logger log = LoggerFactory.getLogger(SnFileResource.class);

    private static final String ENTITY_NAME = "snFile";

    private final SnFileService snFileService;

    public SnFileResource(SnFileService snFileService) {
        this.snFileService = snFileService;
    }

    /**
     * POST  /sn-files : Create a new snFile.
     *
     * @param snFile the snFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new snFile, or with status 400 (Bad Request) if the snFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sn-files")
    @Timed
    public ResponseEntity<SnFile> createSnFile(@RequestBody SnFile snFile) throws URISyntaxException {
        log.debug("REST request to save SnFile : {}", snFile);
        if (snFile.getId() != null) {
            throw new BadRequestAlertException("A new snFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SnFile result = snFileService.save(snFile);
        return ResponseEntity.created(new URI("/api/sn-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sn-files : Updates an existing snFile.
     *
     * @param snFile the snFile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated snFile,
     * or with status 400 (Bad Request) if the snFile is not valid,
     * or with status 500 (Internal Server Error) if the snFile couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sn-files")
    @Timed
    public ResponseEntity<SnFile> updateSnFile(@RequestBody SnFile snFile) throws URISyntaxException {
        log.debug("REST request to update SnFile : {}", snFile);
        if (snFile.getId() == null) {
            return createSnFile(snFile);
        }
        SnFile result = snFileService.save(snFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, snFile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sn-files : get all the snFiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of snFiles in body
     */
    @GetMapping("/sn-files")
    @Timed
    public List<SnFile> getAllSnFiles() {
        log.debug("REST request to get all SnFiles");
        return snFileService.findAll();
        }

    /**
     * GET  /sn-files/:id : get the "id" snFile.
     *
     * @param id the id of the snFile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the snFile, or with status 404 (Not Found)
     */
    @GetMapping("/sn-files/{id}")
    @Timed
    public ResponseEntity<SnFile> getSnFile(@PathVariable Long id) {
        log.debug("REST request to get SnFile : {}", id);
        SnFile snFile = snFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(snFile));
    }

    /**
     * DELETE  /sn-files/:id : delete the "id" snFile.
     *
     * @param id the id of the snFile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sn-files/{id}")
    @Timed
    public ResponseEntity<Void> deleteSnFile(@PathVariable Long id) {
        log.debug("REST request to delete SnFile : {}", id);
        snFileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
