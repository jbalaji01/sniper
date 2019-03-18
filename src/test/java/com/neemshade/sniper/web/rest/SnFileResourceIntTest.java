package com.neemshade.sniper.web.rest;

import com.neemshade.sniper.SniperApp;

import com.neemshade.sniper.domain.SnFile;
import com.neemshade.sniper.repository.SnFileRepository;
import com.neemshade.sniper.service.SnFileService;
import com.neemshade.sniper.web.rest.errors.ExceptionTranslator;

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

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.neemshade.sniper.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.neemshade.sniper.domain.enumeration.ChosenFactor;
/**
 * Test class for the SnFileResource REST controller.
 *
 * @see SnFileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SniperApp.class)
public class SnFileResourceIntTest {

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_EXT = "AAAAAAAAAA";
    private static final String UPDATED_FILE_EXT = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;

    private static final String DEFAULT_ORIGIN = "AAAAAAAAAA";
    private static final String UPDATED_ORIGIN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_INPUT = false;
    private static final Boolean UPDATED_IS_INPUT = true;

    private static final Boolean DEFAULT_IS_AUDIO = false;
    private static final Boolean UPDATED_IS_AUDIO = true;

    private static final Instant DEFAULT_UPLOADED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOADED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ACTUAL_TIME_FRAME = 1;
    private static final Integer UPDATED_ACTUAL_TIME_FRAME = 2;

    private static final Integer DEFAULT_ADJUSTED_TIME_FRAME = 1;
    private static final Integer UPDATED_ADJUSTED_TIME_FRAME = 2;

    private static final Integer DEFAULT_FINAL_TIME_FRAME = 1;
    private static final Integer UPDATED_FINAL_TIME_FRAME = 2;

    private static final Integer DEFAULT_WS_ACTUAL_LINE_COUNT = 1;
    private static final Integer UPDATED_WS_ACTUAL_LINE_COUNT = 2;

    private static final Integer DEFAULT_WS_ADJUSTED_LINE_COUNT = 1;
    private static final Integer UPDATED_WS_ADJUSTED_LINE_COUNT = 2;

    private static final Integer DEFAULT_WS_FINAL_LINE_COUNT = 1;
    private static final Integer UPDATED_WS_FINAL_LINE_COUNT = 2;

    private static final Integer DEFAULT_WOS_ACTUAL_LINE_COUNT = 1;
    private static final Integer UPDATED_WOS_ACTUAL_LINE_COUNT = 2;

    private static final Integer DEFAULT_WOS_ADJUSTED_LINE_COUNT = 1;
    private static final Integer UPDATED_WOS_ADJUSTED_LINE_COUNT = 2;

    private static final Integer DEFAULT_WOS_FINAL_LINE_COUNT = 1;
    private static final Integer UPDATED_WOS_FINAL_LINE_COUNT = 2;

    private static final ChosenFactor DEFAULT_CHOSEN_FACTOR = ChosenFactor.NONE;
    private static final ChosenFactor UPDATED_CHOSEN_FACTOR = ChosenFactor.TIME_FRAME;

    private static final Integer DEFAULT_PECK_ORDER = 1;
    private static final Integer UPDATED_PECK_ORDER = 2;

    @Autowired
    private SnFileRepository snFileRepository;

    @Autowired
    private SnFileService snFileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSnFileMockMvc;

    private SnFile snFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SnFileResource snFileResource = new SnFileResource(snFileService);
        this.restSnFileMockMvc = MockMvcBuilders.standaloneSetup(snFileResource)
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
    public static SnFile createEntity(EntityManager em) {
        SnFile snFile = new SnFile()
            .filePath(DEFAULT_FILE_PATH)
            .fileName(DEFAULT_FILE_NAME)
            .fileExt(DEFAULT_FILE_EXT)
            .fileSize(DEFAULT_FILE_SIZE)
            .origin(DEFAULT_ORIGIN)
            .isInput(DEFAULT_IS_INPUT)
            .isAudio(DEFAULT_IS_AUDIO)
            .uploadedTime(DEFAULT_UPLOADED_TIME)
            .actualTimeFrame(DEFAULT_ACTUAL_TIME_FRAME)
            .adjustedTimeFrame(DEFAULT_ADJUSTED_TIME_FRAME)
            .finalTimeFrame(DEFAULT_FINAL_TIME_FRAME)
            .wsActualLineCount(DEFAULT_WS_ACTUAL_LINE_COUNT)
            .wsAdjustedLineCount(DEFAULT_WS_ADJUSTED_LINE_COUNT)
            .wsFinalLineCount(DEFAULT_WS_FINAL_LINE_COUNT)
            .wosActualLineCount(DEFAULT_WOS_ACTUAL_LINE_COUNT)
            .wosAdjustedLineCount(DEFAULT_WOS_ADJUSTED_LINE_COUNT)
            .wosFinalLineCount(DEFAULT_WOS_FINAL_LINE_COUNT)
            .chosenFactor(DEFAULT_CHOSEN_FACTOR)
            .peckOrder(DEFAULT_PECK_ORDER);
        return snFile;
    }

    @Before
    public void initTest() {
        snFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createSnFile() throws Exception {
        int databaseSizeBeforeCreate = snFileRepository.findAll().size();

        // Create the SnFile
        restSnFileMockMvc.perform(post("/api/sn-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(snFile)))
            .andExpect(status().isCreated());

        // Validate the SnFile in the database
        List<SnFile> snFileList = snFileRepository.findAll();
        assertThat(snFileList).hasSize(databaseSizeBeforeCreate + 1);
        SnFile testSnFile = snFileList.get(snFileList.size() - 1);
        assertThat(testSnFile.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testSnFile.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testSnFile.getFileExt()).isEqualTo(DEFAULT_FILE_EXT);
        assertThat(testSnFile.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testSnFile.getOrigin()).isEqualTo(DEFAULT_ORIGIN);
        assertThat(testSnFile.isIsInput()).isEqualTo(DEFAULT_IS_INPUT);
        assertThat(testSnFile.isIsAudio()).isEqualTo(DEFAULT_IS_AUDIO);
        assertThat(testSnFile.getUploadedTime()).isEqualTo(DEFAULT_UPLOADED_TIME);
        assertThat(testSnFile.getActualTimeFrame()).isEqualTo(DEFAULT_ACTUAL_TIME_FRAME);
        assertThat(testSnFile.getAdjustedTimeFrame()).isEqualTo(DEFAULT_ADJUSTED_TIME_FRAME);
        assertThat(testSnFile.getFinalTimeFrame()).isEqualTo(DEFAULT_FINAL_TIME_FRAME);
        assertThat(testSnFile.getWsActualLineCount()).isEqualTo(DEFAULT_WS_ACTUAL_LINE_COUNT);
        assertThat(testSnFile.getWsAdjustedLineCount()).isEqualTo(DEFAULT_WS_ADJUSTED_LINE_COUNT);
        assertThat(testSnFile.getWsFinalLineCount()).isEqualTo(DEFAULT_WS_FINAL_LINE_COUNT);
        assertThat(testSnFile.getWosActualLineCount()).isEqualTo(DEFAULT_WOS_ACTUAL_LINE_COUNT);
        assertThat(testSnFile.getWosAdjustedLineCount()).isEqualTo(DEFAULT_WOS_ADJUSTED_LINE_COUNT);
        assertThat(testSnFile.getWosFinalLineCount()).isEqualTo(DEFAULT_WOS_FINAL_LINE_COUNT);
        assertThat(testSnFile.getChosenFactor()).isEqualTo(DEFAULT_CHOSEN_FACTOR);
        assertThat(testSnFile.getPeckOrder()).isEqualTo(DEFAULT_PECK_ORDER);
    }

    @Test
    @Transactional
    public void createSnFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = snFileRepository.findAll().size();

        // Create the SnFile with an existing ID
        snFile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSnFileMockMvc.perform(post("/api/sn-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(snFile)))
            .andExpect(status().isBadRequest());

        // Validate the SnFile in the database
        List<SnFile> snFileList = snFileRepository.findAll();
        assertThat(snFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSnFiles() throws Exception {
        // Initialize the database
        snFileRepository.saveAndFlush(snFile);

        // Get all the snFileList
        restSnFileMockMvc.perform(get("/api/sn-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(snFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].fileExt").value(hasItem(DEFAULT_FILE_EXT.toString())))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN.toString())))
            .andExpect(jsonPath("$.[*].isInput").value(hasItem(DEFAULT_IS_INPUT.booleanValue())))
            .andExpect(jsonPath("$.[*].isAudio").value(hasItem(DEFAULT_IS_AUDIO.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadedTime").value(hasItem(DEFAULT_UPLOADED_TIME.toString())))
            .andExpect(jsonPath("$.[*].actualTimeFrame").value(hasItem(DEFAULT_ACTUAL_TIME_FRAME)))
            .andExpect(jsonPath("$.[*].adjustedTimeFrame").value(hasItem(DEFAULT_ADJUSTED_TIME_FRAME)))
            .andExpect(jsonPath("$.[*].finalTimeFrame").value(hasItem(DEFAULT_FINAL_TIME_FRAME)))
            .andExpect(jsonPath("$.[*].wsActualLineCount").value(hasItem(DEFAULT_WS_ACTUAL_LINE_COUNT)))
            .andExpect(jsonPath("$.[*].wsAdjustedLineCount").value(hasItem(DEFAULT_WS_ADJUSTED_LINE_COUNT)))
            .andExpect(jsonPath("$.[*].wsFinalLineCount").value(hasItem(DEFAULT_WS_FINAL_LINE_COUNT)))
            .andExpect(jsonPath("$.[*].wosActualLineCount").value(hasItem(DEFAULT_WOS_ACTUAL_LINE_COUNT)))
            .andExpect(jsonPath("$.[*].wosAdjustedLineCount").value(hasItem(DEFAULT_WOS_ADJUSTED_LINE_COUNT)))
            .andExpect(jsonPath("$.[*].wosFinalLineCount").value(hasItem(DEFAULT_WOS_FINAL_LINE_COUNT)))
            .andExpect(jsonPath("$.[*].chosenFactor").value(hasItem(DEFAULT_CHOSEN_FACTOR.toString())))
            .andExpect(jsonPath("$.[*].peckOrder").value(hasItem(DEFAULT_PECK_ORDER)));
    }

    @Test
    @Transactional
    public void getSnFile() throws Exception {
        // Initialize the database
        snFileRepository.saveAndFlush(snFile);

        // Get the snFile
        restSnFileMockMvc.perform(get("/api/sn-files/{id}", snFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(snFile.getId().intValue()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.fileExt").value(DEFAULT_FILE_EXT.toString()))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.origin").value(DEFAULT_ORIGIN.toString()))
            .andExpect(jsonPath("$.isInput").value(DEFAULT_IS_INPUT.booleanValue()))
            .andExpect(jsonPath("$.isAudio").value(DEFAULT_IS_AUDIO.booleanValue()))
            .andExpect(jsonPath("$.uploadedTime").value(DEFAULT_UPLOADED_TIME.toString()))
            .andExpect(jsonPath("$.actualTimeFrame").value(DEFAULT_ACTUAL_TIME_FRAME))
            .andExpect(jsonPath("$.adjustedTimeFrame").value(DEFAULT_ADJUSTED_TIME_FRAME))
            .andExpect(jsonPath("$.finalTimeFrame").value(DEFAULT_FINAL_TIME_FRAME))
            .andExpect(jsonPath("$.wsActualLineCount").value(DEFAULT_WS_ACTUAL_LINE_COUNT))
            .andExpect(jsonPath("$.wsAdjustedLineCount").value(DEFAULT_WS_ADJUSTED_LINE_COUNT))
            .andExpect(jsonPath("$.wsFinalLineCount").value(DEFAULT_WS_FINAL_LINE_COUNT))
            .andExpect(jsonPath("$.wosActualLineCount").value(DEFAULT_WOS_ACTUAL_LINE_COUNT))
            .andExpect(jsonPath("$.wosAdjustedLineCount").value(DEFAULT_WOS_ADJUSTED_LINE_COUNT))
            .andExpect(jsonPath("$.wosFinalLineCount").value(DEFAULT_WOS_FINAL_LINE_COUNT))
            .andExpect(jsonPath("$.chosenFactor").value(DEFAULT_CHOSEN_FACTOR.toString()))
            .andExpect(jsonPath("$.peckOrder").value(DEFAULT_PECK_ORDER));
    }

    @Test
    @Transactional
    public void getNonExistingSnFile() throws Exception {
        // Get the snFile
        restSnFileMockMvc.perform(get("/api/sn-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSnFile() throws Exception {
        // Initialize the database
        snFileService.save(snFile);

        int databaseSizeBeforeUpdate = snFileRepository.findAll().size();

        // Update the snFile
        SnFile updatedSnFile = snFileRepository.findOne(snFile.getId());
        // Disconnect from session so that the updates on updatedSnFile are not directly saved in db
        em.detach(updatedSnFile);
        updatedSnFile
            .filePath(UPDATED_FILE_PATH)
            .fileName(UPDATED_FILE_NAME)
            .fileExt(UPDATED_FILE_EXT)
            .fileSize(UPDATED_FILE_SIZE)
            .origin(UPDATED_ORIGIN)
            .isInput(UPDATED_IS_INPUT)
            .isAudio(UPDATED_IS_AUDIO)
            .uploadedTime(UPDATED_UPLOADED_TIME)
            .actualTimeFrame(UPDATED_ACTUAL_TIME_FRAME)
            .adjustedTimeFrame(UPDATED_ADJUSTED_TIME_FRAME)
            .finalTimeFrame(UPDATED_FINAL_TIME_FRAME)
            .wsActualLineCount(UPDATED_WS_ACTUAL_LINE_COUNT)
            .wsAdjustedLineCount(UPDATED_WS_ADJUSTED_LINE_COUNT)
            .wsFinalLineCount(UPDATED_WS_FINAL_LINE_COUNT)
            .wosActualLineCount(UPDATED_WOS_ACTUAL_LINE_COUNT)
            .wosAdjustedLineCount(UPDATED_WOS_ADJUSTED_LINE_COUNT)
            .wosFinalLineCount(UPDATED_WOS_FINAL_LINE_COUNT)
            .chosenFactor(UPDATED_CHOSEN_FACTOR)
            .peckOrder(UPDATED_PECK_ORDER);

        restSnFileMockMvc.perform(put("/api/sn-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSnFile)))
            .andExpect(status().isOk());

        // Validate the SnFile in the database
        List<SnFile> snFileList = snFileRepository.findAll();
        assertThat(snFileList).hasSize(databaseSizeBeforeUpdate);
        SnFile testSnFile = snFileList.get(snFileList.size() - 1);
        assertThat(testSnFile.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testSnFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testSnFile.getFileExt()).isEqualTo(UPDATED_FILE_EXT);
        assertThat(testSnFile.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testSnFile.getOrigin()).isEqualTo(UPDATED_ORIGIN);
        assertThat(testSnFile.isIsInput()).isEqualTo(UPDATED_IS_INPUT);
        assertThat(testSnFile.isIsAudio()).isEqualTo(UPDATED_IS_AUDIO);
        assertThat(testSnFile.getUploadedTime()).isEqualTo(UPDATED_UPLOADED_TIME);
        assertThat(testSnFile.getActualTimeFrame()).isEqualTo(UPDATED_ACTUAL_TIME_FRAME);
        assertThat(testSnFile.getAdjustedTimeFrame()).isEqualTo(UPDATED_ADJUSTED_TIME_FRAME);
        assertThat(testSnFile.getFinalTimeFrame()).isEqualTo(UPDATED_FINAL_TIME_FRAME);
        assertThat(testSnFile.getWsActualLineCount()).isEqualTo(UPDATED_WS_ACTUAL_LINE_COUNT);
        assertThat(testSnFile.getWsAdjustedLineCount()).isEqualTo(UPDATED_WS_ADJUSTED_LINE_COUNT);
        assertThat(testSnFile.getWsFinalLineCount()).isEqualTo(UPDATED_WS_FINAL_LINE_COUNT);
        assertThat(testSnFile.getWosActualLineCount()).isEqualTo(UPDATED_WOS_ACTUAL_LINE_COUNT);
        assertThat(testSnFile.getWosAdjustedLineCount()).isEqualTo(UPDATED_WOS_ADJUSTED_LINE_COUNT);
        assertThat(testSnFile.getWosFinalLineCount()).isEqualTo(UPDATED_WOS_FINAL_LINE_COUNT);
        assertThat(testSnFile.getChosenFactor()).isEqualTo(UPDATED_CHOSEN_FACTOR);
        assertThat(testSnFile.getPeckOrder()).isEqualTo(UPDATED_PECK_ORDER);
    }

    @Test
    @Transactional
    public void updateNonExistingSnFile() throws Exception {
        int databaseSizeBeforeUpdate = snFileRepository.findAll().size();

        // Create the SnFile

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSnFileMockMvc.perform(put("/api/sn-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(snFile)))
            .andExpect(status().isCreated());

        // Validate the SnFile in the database
        List<SnFile> snFileList = snFileRepository.findAll();
        assertThat(snFileList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSnFile() throws Exception {
        // Initialize the database
        snFileService.save(snFile);

        int databaseSizeBeforeDelete = snFileRepository.findAll().size();

        // Get the snFile
        restSnFileMockMvc.perform(delete("/api/sn-files/{id}", snFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SnFile> snFileList = snFileRepository.findAll();
        assertThat(snFileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SnFile.class);
        SnFile snFile1 = new SnFile();
        snFile1.setId(1L);
        SnFile snFile2 = new SnFile();
        snFile2.setId(snFile1.getId());
        assertThat(snFile1).isEqualTo(snFile2);
        snFile2.setId(2L);
        assertThat(snFile1).isNotEqualTo(snFile2);
        snFile1.setId(null);
        assertThat(snFile1).isNotEqualTo(snFile2);
    }
}
