package appointmentscheduler.service.file;


import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.file.ServiceFile;
import appointmentscheduler.exception.FileStorageException;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.repository.ServiceFileRepository;

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
public class ServiceFileStorageServiceTest {

    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private ServiceFileRepository serviceFileRepository;

    private Service service;

    private ServiceFileStorageService serviceFileStorageService;
    private  MockMultipartFile multipartFile;

    @Before
    public void setup() {
        serviceFileStorageService = new ServiceFileStorageService(serviceFileRepository,serviceRepository);
        service = new Service();
        service.setId(1);

    }

    @Test
    public void saveServiceFile() throws IOException {
        multipartFile = new MockMultipartFile("serviceFile", "test.txt",
                "text/plain", "Spring Framework".getBytes());

        when(serviceRepository.findById(any())).thenReturn(Optional.of(service));

        serviceFileStorageService.saveServiceFile(multipartFile,service.getId());
        //method to save file was called
        verify(serviceRepository, times(1)).findById(any());
        verify(serviceFileRepository, times(1)).save(any());
    }

    @Test(expected = FileStorageException.class)
    public void saveInvalidServiceFile() throws IOException {
        MockMultipartFile multipartFile1 = new MockMultipartFile("serviceFile", "test..txt",
                "text/plain", "Spring Framework".getBytes());


        MockMultipartFile multipartFile2 = new MockMultipartFile("serviceFile", "test#blob.txt",
                "text/plain", "Spring Framework".getBytes());


        MockMultipartFile multipartFile3 = new MockMultipartFile("serviceFile", "test.tx1t",
                "text/plain", "Spring Framework".getBytes());


        when(serviceRepository.findById(any())).thenReturn(Optional.of(service));
        serviceFileStorageService.saveServiceFile(multipartFile1, service.getId());

        when(serviceRepository.findById(any())).thenReturn(Optional.of(service));
        serviceFileStorageService.saveServiceFile(multipartFile2, service.getId());

        when(serviceRepository.findById(any())).thenReturn(Optional.of(service));
        serviceFileStorageService.saveServiceFile(multipartFile3, service.getId());

    }


    @Test
    public void getServiceFile() {
        ServiceFile expectedFile = new ServiceFile("test","text/plain", "Spring Framework".getBytes(), service);
        ServiceFile retrievedFile;
        when(serviceFileRepository.findByServiceId(service.getId())).thenReturn(Optional.of(expectedFile));
        retrievedFile = serviceFileStorageService.getServiceFile( service.getId());
        verify(serviceFileRepository, times(1)).findByServiceId( service.getId());
        assertEquals(expectedFile, retrievedFile);
    }

    @Test(expected = FileStorageException.class)
    public void getInvalidFile() {
        serviceFileStorageService.getServiceFile(service.getId());
    }

}
