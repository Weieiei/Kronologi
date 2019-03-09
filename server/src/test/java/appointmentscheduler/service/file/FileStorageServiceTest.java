package appointmentscheduler.service.file;

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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileStorageServiceTest {

    @Mock
    private FileRepository fileRepository;

    private FileStorageService fileStorageService;

    @Before
    public void setup() {
        fileStorageService = new FileStorageService(fileRepository);
    }

    @Ignore
    @Test
    public void saveFile() throws IOException {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        when(fileRepository.save(any())).thenReturn(multipartFile);
       assertEquals( multipartFile.getName(), fileStorageService.saveFile(multipartFile).getFileName());
    }

    @Test
    public void getFile() {
    }
}