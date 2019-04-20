package appointmentscheduler.service.file;


import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.file.UserFile;
import appointmentscheduler.exception.FileStorageException;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.repository.UserFileRepository;

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

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserFileStorageServiceTest {


    @Mock
    private UserRepository userRepository;
    @Mock
    private UserFileRepository userFileRepository;

    private User user;

    private UserFileStorageService userFileStorageService;
    private  MockMultipartFile multipartFile;

    @Before
    public void setup() {
        userFileStorageService = new UserFileStorageService(userFileRepository,userRepository);
        user = new User();
        user.setId(1);

    }

    @Test
    public void saveUserFile() throws IOException {
        multipartFile = new MockMultipartFile("userFile", "test.txt",
                "text/plain", "Spring Framework".getBytes());

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        userFileStorageService.saveUserFile(multipartFile,user.getId());
        //method to save file was called
        verify(userRepository, times(1)).findById(any());
        verify(userFileRepository, times(1)).save(any());
    }

    @Test(expected = FileStorageException.class)
    public void saveInvalidServiceFile() throws IOException {
        MockMultipartFile multipartFile1 = new MockMultipartFile("userFile", "test..txt",
                "text/plain", "Spring Framework".getBytes());


        MockMultipartFile multipartFile2 = new MockMultipartFile("userFile", "test#blob.txt",
                "text/plain", "Spring Framework".getBytes());


        MockMultipartFile multipartFile3 = new MockMultipartFile("userFile", "test.tx1t",
                "text/plain", "Spring Framework".getBytes());


        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        userFileStorageService.saveUserFile(multipartFile1, user.getId());

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        userFileStorageService.saveUserFile(multipartFile2, user.getId());

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        userFileStorageService.saveUserFile(multipartFile3, user.getId());

    }


    @Test
    public void getServiceFile() {
        UserFile expectedFile = new UserFile("userFile","text/plain", "Spring Framework".getBytes(), user);
        UserFile retrievedFile;
        when(userFileRepository.findByUserId(user.getId())).thenReturn(Optional.of(expectedFile));
        retrievedFile = userFileStorageService.getUserFile( user.getId());
        verify(userFileRepository, times(1)).findByUserId( user.getId());
        assertEquals(expectedFile, retrievedFile);
    }

    @Test(expected = FileStorageException.class)
    public void getInvalidFile() {
        userFileStorageService.getUserFile(user.getId());
    }

}

