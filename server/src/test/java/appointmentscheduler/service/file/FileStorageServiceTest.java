package appointmentscheduler.service.file;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.file.File;
import appointmentscheduler.exception.FileStorageException;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.repository.FileRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileStorageServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private BusinessRepository businessRepository;

    private Business business;

    private FileStorageService fileStorageService;

    @Before
    public void setup() {
        fileStorageService = new FileStorageService(fileRepository, businessRepository);
        business = new Business();
        business.setId(1);
    }

    @Test
    public void saveFile() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        when(businessRepository.findById(any())).thenReturn(Optional.of(business));
        fileStorageService.saveFile(multipartFile,business.getId());
        //method to save file was called
        verify(fileRepository, times(1)).save(any());
    }

    @Test(expected = FileStorageException.class)
    public void saveInvalidFile() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test..txt",
                "text/plain", "Spring Framework".getBytes());
        when(businessRepository.findById(any())).thenReturn(Optional.of(business));
        fileStorageService.saveFile(multipartFile, business.getId());
    }


    @Test
    public void getFile() {
        File expectedFile = new File("test","text/plain", "Spring Framework".getBytes(), business);
        File retrievedFile;
        when(fileRepository.findFileByBusinessIdAndAndId("test",business.getId())).thenReturn(Optional.of(expectedFile));
        retrievedFile = fileStorageService.getFile("test", business.getId());
        verify(fileRepository, times(1)).findFileByBusinessIdAndAndId("test", business.getId());
        assertEquals(expectedFile, retrievedFile);
    }

    @Test(expected = FileStorageException.class)
    public void getInvalidFile() {
        fileStorageService.getFile("test", business.getId());
    }
}