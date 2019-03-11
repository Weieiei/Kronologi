package appointmentscheduler.service.file;

import appointmentscheduler.entity.file.File;
import appointmentscheduler.exception.FileStorageException;
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

    private FileStorageService fileStorageService;

    @Before
    public void setup() {
        fileStorageService = new FileStorageService(fileRepository);
    }

    @Test
    public void saveFile() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        fileStorageService.saveFile(multipartFile);
        //method to save file was called
        verify(fileRepository, times(1)).save(any());
    }

    @Test(expected = FileStorageException.class)
    public void saveInvalidFile() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test..txt",
                "text/plain", "Spring Framework".getBytes());
        fileStorageService.saveFile(multipartFile);
    }


    @Test
    public void getFile() {
        File expectedFile = new File("test","text/plain", "Spring Framework".getBytes());
        File retrievedFile;
        when(fileRepository.findById("test")).thenReturn(Optional.of(expectedFile));
        retrievedFile = fileStorageService.getFile("test");
        verify(fileRepository, times(1)).findById(any());
        assertEquals(expectedFile, retrievedFile);
    }

    @Test(expected = FileStorageException.class)
    public void getInvalidFile() {
        fileStorageService.getFile("test");
    }
}