
package appointmentscheduler.service.user;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.AppointmentFactory;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.business.Business;
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
        Employee mockEmployee = Mockito.mock(Employee.class);
        Service mockService = Mockito.mock(Service.class);
        Business mockBusiness =  Mockito.mock(Business.class);


        String clientFirstName = "clientFirstName";
        String clientLastName = "clientLastName";
        String employeeFirstName = "clientFirstName";
        String employeeLastName = "clientLastName";
        String serviceName = "serviceName";
        int serviceDuration = 20;
        long businessId = 1;

        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.now();
        LocalTime endTime = LocalTime.now();

        Mockito.when(mockEmployee.getFirstName()).thenReturn(employeeFirstName);
        Mockito.when(mockEmployee.getLastName()).thenReturn(employeeLastName);
        Mockito.when(mockClient.getFirstName()).thenReturn(clientFirstName);
        Mockito.when(mockClient.getLastName()).thenReturn(clientLastName);
        Mockito.when(mockService.getDuration()).thenReturn(serviceDuration);
        Mockito.when(mockService.getName()).thenReturn(serviceName);
        Mockito.when(mockBusiness.getId()).thenReturn(businessId);


        String notes = "note";
        Appointment appointment = AppointmentFactory.createAppointment(mockClient, mockEmployee, mockService, date, startTime, notes);
        appointment.setEndTime(endTime);
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        Mockito.when(mockAppointmentService.findByClientIdAndBusinessId(Mockito.anyLong(),Mockito.anyLong())).thenReturn(appointments);


        List <Appointment> result = mockAppointmentService.findByClientIdAndBusinessId(5,mockBusiness.getId());

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
        Employee mockEmployee = Mockito.mock(Employee.class);
        Service mockService = Mockito.mock(Service.class);
        Business mockBusiness =  Mockito.mock(Business.class);

        String clientFirstName = "clientFirstName";
        String clientLastName = "clientLastName";
        String employeeFirstName = "clientFirstName";
        String employeeLastName = "clientLastName";
        String serviceName = "serviceName";
        int serviceDuration = 20;
        long businessId = 1;

        LocalDate date = LocalDate.now();
        LocalTime startTime = LocalTime.now();
        LocalTime endTime = LocalTime.now();

        Mockito.when(mockEmployee.getFirstName()).thenReturn(employeeFirstName);
        Mockito.when(mockEmployee.getLastName()).thenReturn(employeeLastName);
        Mockito.when(mockClient.getFirstName()).thenReturn(clientFirstName);
        Mockito.when(mockClient.getLastName()).thenReturn(clientLastName);
        Mockito.when(mockService.getDuration()).thenReturn(serviceDuration);
        Mockito.when(mockService.getName()).thenReturn(serviceName);
        Mockito.when(mockBusiness.getId()).thenReturn(businessId);

        String notes = "note";
        Appointment appointment = new Appointment(mockClient, mockEmployee, mockService, date, startTime, notes);
        appointment.setEndTime(endTime);
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        Mockito.when(mockAppointmentService.findByEmployeeIdAndBusinessId(Mockito.anyLong(),Mockito.anyLong())).thenReturn(appointments);


        List <Appointment> result = mockAppointmentService.findByEmployeeIdAndBusinessId(5, mockBusiness.getId());

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

