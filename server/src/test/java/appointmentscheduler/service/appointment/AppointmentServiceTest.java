package appointmentscheduler.service.appointment;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.repository.AppointmentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceTest {
    private AppointmentService appointmentService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private AppointmentRepository appointmentRepository;

    @Before
    public void before() {
        this.appointmentService = new AppointmentService(appointmentRepository);
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findById() {
        long testId = 1;
        Appointment appointment = this.appointmentService.findById(testId);
        System.out.print("");

    }

    @Test
    public void findByEmployeeId() {
        List<Appointment> appointments = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            appointments.add(mock(Appointment.class));
        }
        when(appointmentRepository.findByEmployeeId(anyLong())).thenReturn(appointments);
        List<Appointment> result = this.appointmentService.findByEmployeeId(Long.valueOf(4));
        assertEquals(6, result.size());
    }

    @Test
    public void add() {
    }

    @Test
    public void update() {
    }

    @Test
    public void cancel() {
    }

    @Test
    public void delete() {
    }
}