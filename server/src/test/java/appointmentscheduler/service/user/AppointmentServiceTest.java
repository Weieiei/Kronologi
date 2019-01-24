package appointmentscheduler.service.user;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.service.AuthenticationService;
import appointmentscheduler.service.appointment.AppointmentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentServiceTest {

    private AppointmentService mockAppointmentService;
    private AuthenticationService mockAuthenticationService;

    @Before
    public void setup(){
        mockAppointmentService = Mockito.mock(AppointmentService.class);
        mockAuthenticationService = Mockito.mock(AuthenticationService.class);
    }


    @Test
    public void getAppointmentsForCurrentUser() {

        User mockClient = Mockito.mock(User.class);
        User mockEmployee = Mockito.mock(User.class);
        Service mockService = Mockito.mock(Service.class);

        String clientFirstName = "clientFirstName";
        String clientLastName = "clientLastName";
        String employeeFirstName = "clientFirstName";
        String employeeLastName = "clientLastName";
        String serviceName = "serviceName";
        int serviceDuration = 20;

        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.now();
        LocalTime endTime = LocalTime.now();

        Mockito.when(mockEmployee.getFirstName()).thenReturn(employeeFirstName);
        Mockito.when(mockEmployee.getLastName()).thenReturn(employeeLastName);
        Mockito.when(mockClient.getFirstName()).thenReturn(clientFirstName);
        Mockito.when(mockClient.getLastName()).thenReturn(clientLastName);
        Mockito.when(mockService.getDuration()).thenReturn(serviceDuration);
        Mockito.when(mockService.getName()).thenReturn(serviceName);


        String notes = "note";
        Appointment appointment = new Appointment(mockClient, mockEmployee, mockService, date, startTime, notes);
        appointment.setEndTime(endTime);
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        Mockito.when(mockAppointmentService.findByClientId(Mockito.anyLong())).thenReturn(appointments);

        long id = 5;
        Mockito.when(mockAuthenticationService.getCurrentUserId()).thenReturn(id);
        Mockito.when(mockAppointmentService.findByClientId(Mockito.anyLong())).thenReturn(appointments);

        Assert.assertEquals(1, appointments.size());
        Assert.assertEquals(endTime , appointments.get(0).getEndTime());
        Assert.assertEquals(startTime , appointments.get(0).getStartTime());
        Assert.assertEquals(notes, appointments.get(0).getNotes());
        Assert.assertEquals(employeeFirstName, appointments.get(0).getEmployee().getFirstName() );
        Assert.assertEquals(employeeLastName, appointments.get(0).getEmployee().getLastName() );
        Assert.assertEquals(clientFirstName, appointments.get(0).getEmployee().getFirstName() );
        Assert.assertEquals(serviceName, appointments.get(0).getService().getName() );
        Assert.assertEquals(serviceDuration, appointments.get(0).getService().getDuration() );
    }
}
