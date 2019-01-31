package appointmentscheduler.service.user;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.service.ServiceEntity;
import appointmentscheduler.entity.user.User;
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

    @Before
    public void setup(){
        mockAppointmentService = Mockito.mock(AppointmentService.class);
    }


    @Test
    public void getAppointmentsForCurrentUser() {

        User mockClient = Mockito.mock(User.class);
        User mockEmployee = Mockito.mock(User.class);
        ServiceEntity mockService = Mockito.mock(ServiceEntity.class);

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


        List <Appointment> result = mockAppointmentService.findByClientId(5);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(endTime , result.get(0).getEndTime());
        Assert.assertEquals(startTime , result.get(0).getStartTime());
        Assert.assertEquals(notes, result.get(0).getNotes());
        Assert.assertEquals(employeeFirstName, result.get(0).getEmployee().getFirstName() );
        Assert.assertEquals(employeeLastName, result.get(0).getEmployee().getLastName() );
        Assert.assertEquals(clientFirstName, result.get(0).getEmployee().getFirstName() );
        Assert.assertEquals(serviceName, result.get(0).getService().getName() );
        Assert.assertEquals(serviceDuration, result.get(0).getService().getDuration() );
    }

    @Test
    public void getAppointmentsForCurrentEmployee() {

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
        Mockito.when(mockAppointmentService.findByEmployeeId(Mockito.anyLong())).thenReturn(appointments);


        List <Appointment> result = mockAppointmentService.findByEmployeeId(5);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(endTime , result.get(0).getEndTime());
        Assert.assertEquals(startTime , result.get(0).getStartTime());
        Assert.assertEquals(notes, result.get(0).getNotes());
        Assert.assertEquals(employeeFirstName, result.get(0).getEmployee().getFirstName() );
        Assert.assertEquals(employeeLastName, result.get(0).getEmployee().getLastName() );
        Assert.assertEquals(clientFirstName, result.get(0).getEmployee().getFirstName() );
        Assert.assertEquals(serviceName, result.get(0).getService().getName() );
        Assert.assertEquals(serviceDuration, result.get(0).getService().getDuration() );
    }
}
