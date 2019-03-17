package appointmentscheduler.service.business;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.service.file.FileStorageService;
import org.junit.*;
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
public class BusinessServiceTest {
    @Mock
    private BusinessRepository businessRepository;
    @Mock
    private Business business;

    private BusinessService businessService;

    @Before

    public void setup() {
        businessService = new BusinessService( businessRepository);

        when(businessRepository.save(business)).thenReturn(business);

        when(business.getId()).thenReturn((long) 1);

    }
    @Test
    public void testAdd() throws IOException {
        long id = businessService.add(business);

        verify(businessRepository).save(business);
        verify(business).getId();
        Assert.assertEquals(id,business.getId() );


    }
}
