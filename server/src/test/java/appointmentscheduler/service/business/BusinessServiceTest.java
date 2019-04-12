package appointmentscheduler.service.business;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.BusinessRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.fail;
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

    @Test
    public void testFindById() {

        when(businessRepository.findById(anyLong()).get()).thenReturn(business);
        Assert.assertEquals(businessRepository.findById((long) 1), business);
    }

    @Test
    public void testFindByIdNoBusinessFound() {
        when(businessRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            businessService.findById((long) 1);
            fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertEquals(e.getMessage(), "Business with id 1 not found.");
        }

    }


}
