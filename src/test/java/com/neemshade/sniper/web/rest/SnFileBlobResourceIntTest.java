package com.neemshade.sniper.web.rest;

import com.neemshade.sniper.SniperApp;

import com.neemshade.sniper.domain.SnFileBlob;
import com.neemshade.sniper.repository.SnFileBlobRepository;
import com.neemshade.sniper.service.SnFileBlobService;
import com.neemshade.sniper.service.dto.SnFileBlobDTO;
import com.neemshade.sniper.web.rest.errors.ExceptionTranslator;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;

import java.io.InputStream;
import java.sql.Blob;
import java.util.List;

import static com.neemshade.sniper.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SnFileBlobResource REST controller.
 *
 * @see SnFileBlobResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SniperApp.class)
public class SnFileBlobResourceIntTest {

    private static final byte[] DEFAULT_FILE_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_CONTENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILE_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_CONTENT_TYPE = "image/png";

    @Autowired
    private SnFileBlobRepository snFileBlobRepository;

    @Autowired
    private SnFileBlobService snFileBlobService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSnFileBlobMockMvc;

    private SnFileBlob snFileBlob;
    private SnFileBlobDTO snFileBlobDto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SnFileBlobResource snFileBlobResource = new SnFileBlobResource(snFileBlobRepository, snFileBlobService);
        this.restSnFileBlobMockMvc = MockMvcBuilders.standaloneSetup(snFileBlobResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SnFileBlob createEntity(EntityManager em) {
        Blob defaultFileContent = ((Session) em.getDelegate()).getLobHelper().createBlob(DEFAULT_FILE_CONTENT);
        return new SnFileBlob()
            .fileContent(defaultFileContent)
            .fileContentContentType(DEFAULT_FILE_CONTENT_CONTENT_TYPE);
    }

    public static SnFileBlobDTO createEntityDto() {
        SnFileBlobDTO snFileBlobDTO = new SnFileBlobDTO();
        snFileBlobDTO.setFileContent(DEFAULT_FILE_CONTENT);
        snFileBlobDTO.setFileContentContentType(DEFAULT_FILE_CONTENT_CONTENT_TYPE);
        return snFileBlobDTO;
    }

    @Before
    public void initTest() {
        snFileBlob = createEntity(em);
        snFileBlobDto = createEntityDto();
        snFileBlobRepository.deleteAll();
    }

    @Test
    @Transactional
    public void createSnFileBlob() throws Exception {
        int databaseSizeBeforeCreate = snFileBlobRepository.findAll().size();

        // Create the SnFileBlob
        restSnFileBlobMockMvc.perform(post("/api/sn-file-blobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(snFileBlobDto)))
            .andExpect(status().isCreated());

        // Validate the SnFileBlob in the database
        List<SnFileBlob> snFileBlobList = snFileBlobRepository.findAll();
        assertThat(snFileBlobList).hasSize(databaseSizeBeforeCreate + 1);
        SnFileBlob testSnFileBlob = snFileBlobList.get(snFileBlobList.size() - 1);
        InputStream binaryStream = testSnFileBlob.getFileContent().getBinaryStream();
        assertThat(IOUtils.toByteArray(binaryStream)).isEqualTo(DEFAULT_FILE_CONTENT);
        IOUtils.closeQuietly(binaryStream);
        assertThat(testSnFileBlob.getFileContentContentType()).isEqualTo(DEFAULT_FILE_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createSnFileBlobWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = snFileBlobRepository.findAll().size();

        // Create the SnFileBlob with an existing ID
        snFileBlobDto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSnFileBlobMockMvc.perform(post("/api/sn-file-blobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(snFileBlobDto)))
            .andExpect(status().isBadRequest());

        // Validate the SnFileBlob in the database
        List<SnFileBlob> snFileBlobList = snFileBlobRepository.findAll();
        assertThat(snFileBlobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSnFileBlobs() throws Exception {
        // Initialize the database
        snFileBlobRepository.saveAndFlush(snFileBlob);

        // Get all the snFileBlobList
        restSnFileBlobMockMvc.perform(get("/api/sn-file-blobs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(snFileBlob.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileContentContentType").value(hasItem(DEFAULT_FILE_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileContent").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT))));
    }

    @Test
    @Transactional
    public void getSnFileBlob() throws Exception {
        // Initialize the database
        snFileBlobRepository.saveAndFlush(snFileBlob);

        // Get the snFileBlob
        restSnFileBlobMockMvc.perform(get("/api/sn-file-blobs/{id}", snFileBlob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(snFileBlob.getId().intValue()))
            .andExpect(jsonPath("$.fileContentContentType").value(DEFAULT_FILE_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileContent").value(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT)));
    }

    @Test
    @Transactional
    public void getNonExistingSnFileBlob() throws Exception {
        // Get the snFileBlob
        restSnFileBlobMockMvc.perform(get("/api/sn-file-blobs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSnFileBlob() throws Exception {
        // Initialize the database
        snFileBlobRepository.saveAndFlush(snFileBlob);
        int databaseSizeBeforeUpdate = snFileBlobRepository.findAll().size();

        SnFileBlobDTO updatedSnFileBlobDto = new SnFileBlobDTO();
        updatedSnFileBlobDto.setId(snFileBlob.getId());
        updatedSnFileBlobDto.setFileContent(UPDATED_FILE_CONTENT);
        updatedSnFileBlobDto.setFileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE);

        restSnFileBlobMockMvc.perform(put("/api/sn-file-blobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSnFileBlobDto)))
            .andExpect(status().isOk());

        // Validate the SnFileBlob in the database
        assertThat(snFileBlobRepository.count()).isEqualTo(databaseSizeBeforeUpdate);
        SnFileBlob testSnFileBlob = snFileBlobRepository.findOne(snFileBlob.getId());
        InputStream binaryStream = testSnFileBlob.getFileContent().getBinaryStream();
        assertThat(IOUtils.toByteArray(binaryStream)).isEqualTo(UPDATED_FILE_CONTENT);
        IOUtils.closeQuietly(binaryStream);
        assertThat(testSnFileBlob.getFileContentContentType()).isEqualTo(UPDATED_FILE_CONTENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingSnFileBlob() throws Exception {
        int databaseSizeBeforeUpdate = snFileBlobRepository.findAll().size();

        // Create the SnFileBlob

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSnFileBlobMockMvc.perform(put("/api/sn-file-blobs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(snFileBlobDto)))
            .andExpect(status().isCreated());

        // Validate the SnFileBlob in the database
        List<SnFileBlob> snFileBlobList = snFileBlobRepository.findAll();
        assertThat(snFileBlobList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSnFileBlob() throws Exception {
        // Initialize the database
        snFileBlobRepository.saveAndFlush(snFileBlob);
        int databaseSizeBeforeDelete = snFileBlobRepository.findAll().size();

        // Get the snFileBlob
        restSnFileBlobMockMvc.perform(delete("/api/sn-file-blobs/{id}", snFileBlob.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SnFileBlob> snFileBlobList = snFileBlobRepository.findAll();
        assertThat(snFileBlobList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SnFileBlob.class);
        SnFileBlob snFileBlob1 = new SnFileBlob();
        snFileBlob1.setId(1L);
        SnFileBlob snFileBlob2 = new SnFileBlob();
        snFileBlob2.setId(snFileBlob1.getId());
        assertThat(snFileBlob1).isEqualTo(snFileBlob2);
        snFileBlob2.setId(2L);
        assertThat(snFileBlob1).isNotEqualTo(snFileBlob2);
        snFileBlob1.setId(null);
        assertThat(snFileBlob1).isNotEqualTo(snFileBlob2);
    }
}
