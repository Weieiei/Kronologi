package appointmentscheduler.service.google;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.AppointmentFactory;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.googleEntity.SyncEntity;
import appointmentscheduler.service.googleService.GoogleSyncService;
import com.google.common.base.Verify;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import appointmentscheduler.repository.SyncSettingRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GoogleSyncServiceTest {

    private GoogleSyncService googleSyncService;

    SyncSettingRepository syncSettingRepository = Mockito.mock(SyncSettingRepository.class);

    @Before
    public void setup(){
        googleSyncService = new GoogleSyncService(syncSettingRepository);
    }

    @Test
    public void saveSyncTokenTest(){
        SyncEntity syncEntity = Mockito.mock(SyncEntity.class);

        googleSyncService.saveSyncToken(syncEntity);
        verify(syncSettingRepository).save(any(SyncEntity.class));
    }

    @Test
    public void findByIdTest(){
        SyncEntity syncEntity = Mockito.mock(SyncEntity.class);
        long userId = 1;
       // User user = Mockito.mock(User.class);
       // when(user.getId()).thenReturn(userId);

        when(syncSettingRepository.findByUserId(anyLong())).thenReturn(Optional.of(syncEntity));
        SyncEntity newSyncEntity = googleSyncService.findById(1);

        verify(syncSettingRepository).findByUserId(anyLong());

    }

}
