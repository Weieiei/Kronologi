package appointmentscheduler.service.file;

import appointmentscheduler.repository.FileRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FileStorageServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Before
    public void setup() {
        fileStorageService = new FileStorageService(fileRepository);
    }

    @Test
    public void saveFile() {
    }

    @Test
    public void getFile() {
    }
}