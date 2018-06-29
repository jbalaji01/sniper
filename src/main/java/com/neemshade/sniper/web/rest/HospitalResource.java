package com.neemshade.sniper.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.neemshade.sniper.domain.Hospital;

import com.neemshade.sniper.repository.HospitalRepository;
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
 * REST controller for managing Hospital.
 */
@RestController
@RequestMapping("/api")
public class HospitalResource {

    private final Logger log = LoggerFactory.getLogger(HospitalResource.class);

    private static final String ENTITY_NAME = "hospital";

    private final HospitalRepository hospitalRepository;

    public HospitalResource(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    /**
     * POST  /hospitals : Create a new hospital.
     *
     * @param hospital the hospital to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hospital, or with status 400 (Bad Request) if the hospital has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hospitals")
    @Timed
    public ResponseEntity<Hospital> createHospital(@RequestBody Hospital hospital) throws URISyntaxException {
        log.debug("REST request to save Hospital : {}", hospital);
        if (hospital.getId() != null) {
            throw new BadRequestAlertException("A new hospital cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hospital result = hospitalRepository.save(hospital);
        return ResponseEntity.created(new URI("/api/hospitals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hospitals : Updates an existing hospital.
     *
     * @param hospital the hospital to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hospital,
     * or with status 400 (Bad Request) if the hospital is not valid,
     * or with status 500 (Internal Server Error) if the hospital couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hospitals")
    @Timed
    public ResponseEntity<Hospital> updateHospital(@RequestBody Hospital hospital) throws URISyntaxException {
        log.debug("REST request to update Hospital : {}", hospital);
        if (hospital.getId() == null) {
            return createHospital(hospital);
        }
        Hospital result = hospitalRepository.save(hospital);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hospital.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hospitals : get all the hospitals.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hospitals in body
     */
    @GetMapping("/hospitals")
    @Timed
    public List<Hospital> getAllHospitals() {
        log.debug("REST request to get all Hospitals");
        return hospitalRepository.findAll();
        }

    /**
     * GET  /hospitals/:id : get the "id" hospital.
     *
     * @param id the id of the hospital to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hospital, or with status 404 (Not Found)
     */
    @GetMapping("/hospitals/{id}")
    @Timed
    public ResponseEntity<Hospital> getHospital(@PathVariable Long id) {
        log.debug("REST request to get Hospital : {}", id);
        Hospital hospital = hospitalRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hospital));
    }

    /**
     * DELETE  /hospitals/:id : delete the "id" hospital.
     *
     * @param id the id of the hospital to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hospitals/{id}")
    @Timed
    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
        log.debug("REST request to delete Hospital : {}", id);
        hospitalRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
