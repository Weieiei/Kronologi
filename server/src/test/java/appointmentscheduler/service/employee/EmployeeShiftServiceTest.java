package appointmentscheduler.service.employee;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.AppEventTimeConflict;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.repository.EmployeeRepository;
import appointmentscheduler.repository.ShiftRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeShiftServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ShiftRepository shiftRepository;

    @Mock
    private BusinessRepository businessRepository;

    private EmployeeShiftService employeeShiftService;


    @Before
    public void before() {
        employeeShiftService = new EmployeeShiftService(shiftRepository, employeeRepository, businessRepository);
    }

    @Test
    public void createShift() {
        final Employee mockEmployee = mock(Employee.class);
        final Business mockBusiness= mock(Business.class);

        final LocalDate localDate = LocalDate.now();
        final LocalTime startTime = LocalTime.of(1,0);
        final LocalTime endTime = LocalTime.of(2,0);

        EmployeeShiftDTO employeeShiftDTO = new EmployeeShiftDTO();
        employeeShiftDTO.setDate(localDate);
        employeeShiftDTO.setStartTime(startTime);
        employeeShiftDTO.setEndTime(endTime);


        when(employeeRepository.findByIdAndBusinessId(anyLong(), anyLong())).thenReturn(Optional.of(mockEmployee));
        when(businessRepository.findById(anyLong())).thenReturn(Optional.of(mockBusiness));

        employeeShiftService.createShiftForBusiness(anyLong(), anyLong(), employeeShiftDTO);

        verify(shiftRepository,times(1)).save(any());
    }

    @Test
    public void getEmployeeShifts() {
        final Shift mockedShift1 = mock(Shift.class);
        final Shift mockedShift2 = mock(Shift.class);
        final List<Shift> shifts = new ArrayList<>();
        List<Shift> employeeShifts;

        when(mockedShift1.getId()).thenReturn((long) 1);
        when(mockedShift2.getId()).thenReturn((long) 2);

        shifts.add(mockedShift1);
        shifts.add(mockedShift2);

        when(shiftRepository.findByEmployeeId(anyLong())).thenReturn(shifts);

        employeeShifts = employeeShiftService.getEmployeeShifts(1);

        assertEquals(shifts.size(), employeeShifts.size());
        assertEquals(shifts.get(0).getId(), employeeShifts.get(0).getId());
        assertEquals(shifts.get(1).getId(), employeeShifts.get(1).getId());

    }

    @Test
    public void getShiftsInvalidEmployee() {
        List<Shift> employeeShifts;
        employeeShifts = employeeShiftService.getEmployeeShifts(anyLong());

       assertTrue(employeeShifts.isEmpty());
    }

    @Test
    public void deleteShift() {
        final Shift mockedShift1 = mock(Shift.class);

        when(shiftRepository.findByIdAndBusinessId(anyLong(), anyLong())).thenReturn(Optional.of(mockedShift1));

        employeeShiftService.deleteShift(anyLong(), anyLong());
        verify(shiftRepository,times(1)).delete(any());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteInvalidShift() {
        employeeShiftService.deleteShift(anyLong(), anyLong());
        fail("Should have thrown an exception.");
    }

    @Test
    public void modifyShift() {
        final Business mockBusiness = mock(Business.class);
        final LocalDate localDate = LocalDate.now();
        final LocalTime startTime = LocalTime.of(1,0);
        final LocalTime endTime = LocalTime.of(2,0);
        final LocalTime newStartTime = LocalTime.of(2,0);
        final LocalTime newEndTime = LocalTime.of(3,0);

        Shift shift = new Shift();
        shift.setId(1);
        shift.setDate(localDate);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        EmployeeShiftDTO employeeShiftDTO = new EmployeeShiftDTO();
        employeeShiftDTO.setDate(localDate);
        employeeShiftDTO.setStartTime(newStartTime);
        employeeShiftDTO.setEndTime(newEndTime);

        when(shiftRepository.findByIdAndBusinessId(anyLong(),anyLong())).thenReturn(Optional.of(shift));
        when(businessRepository.findById(anyLong())).thenReturn(Optional.of(mockBusiness));

        employeeShiftService.modifyShift(anyLong(), anyLong(), employeeShiftDTO, 1);

        verify(shiftRepository,times(1)).save(shift);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void modifyInvalidShift() {
        final EmployeeShiftDTO employeeShiftDTO = mock(EmployeeShiftDTO.class);

        employeeShiftService.modifyShift(anyLong(), anyLong(), employeeShiftDTO, 1);
        fail("Should have thrown an exception.");
    }

    @Test
    public void getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        List<Employee> retrievedEmployees;
        final Employee employee1 = mock(Employee.class);
        final Employee employee2 = mock(Employee.class);

        employees.add(employee1);
        employees.add(employee2);

        when(employee1.getId()).thenReturn((long) 1);
        when(employee2.getId()).thenReturn((long) 2);

        when(employeeRepository.findAll()).thenReturn(employees);

        retrievedEmployees = employeeShiftService.getEmployees();

        assertEquals(employees.size(), retrievedEmployees.size());
        assertEquals(1, retrievedEmployees.get(0).getId());
        assertEquals(2, retrievedEmployees.get(1).getId());
    }

    @Test
    public void getAddShiftListValid() {
        List<EmployeeShiftDTO> newShifts = new ArrayList<>();
        List<EmployeeShiftDTO> storedShifts;
        List<Shift> oldShifts = new ArrayList<>();

        final Employee mockEmployee = mock(Employee.class);
        final Business mockBusiness= mock(Business.class);

        LocalDate localDate = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,0);
        LocalTime endTime = LocalTime.of(2,0);

        oldShifts.add(new Shift(mockBusiness,mockEmployee,localDate, startTime, endTime));
        oldShifts.add(new Shift(mockBusiness,mockEmployee,localDate, startTime.plusHours(1), endTime.plusHours(1)));
        oldShifts.add(new Shift(mockBusiness,mockEmployee,localDate, startTime.plusHours(2), endTime.plusHours(2)));

        newShifts.add(new EmployeeShiftDTO(localDate,startTime.plusHours(3), endTime.plusHours(3)));
        newShifts.add(new EmployeeShiftDTO(localDate,startTime.plusHours(4), endTime.plusHours(4)));

        when(employeeRepository.findByIdAndBusinessId(anyLong(), anyLong())).thenReturn(Optional.of(mockEmployee));
        when(businessRepository.findById(anyLong())).thenReturn(Optional.of(mockBusiness));
        when(shiftRepository.findByEmployeeIdAndBusinessId(anyLong(), anyLong())).thenReturn(oldShifts);

        storedShifts = employeeShiftService.addShiftList(0,0,newShifts);

        assertNotNull(storedShifts);
        assertEquals(newShifts.size(), storedShifts.size());
    }

    @Test(expected = AppEventTimeConflict.class)
    public void getAddShiftListInalid() {
        List<EmployeeShiftDTO> newShifts = new ArrayList<>();
        List<Shift> oldShifts = new ArrayList<>();

        final Employee mockEmployee = mock(Employee.class);
        final Business mockBusiness= mock(Business.class);

        LocalDate localDate = LocalDate.now();
        LocalTime startTime = LocalTime.of(1,0);
        LocalTime endTime = LocalTime.of(2,0);

        oldShifts.add(new Shift(mockBusiness,mockEmployee,localDate, startTime, endTime));
        oldShifts.add(new Shift(mockBusiness,mockEmployee,localDate, startTime.plusHours(1), endTime.plusHours(1)));
        oldShifts.add(new Shift(mockBusiness,mockEmployee,localDate, startTime.plusHours(2), endTime.plusHours(2)));

        newShifts.add(new EmployeeShiftDTO(localDate,startTime.plusHours(2), endTime.plusHours(3)));
        newShifts.add(new EmployeeShiftDTO(localDate,startTime.plusHours(4), endTime.plusHours(4)));

        when(employeeRepository.findByIdAndBusinessId(anyLong(), anyLong())).thenReturn(Optional.of(mockEmployee));
        when(businessRepository.findById(anyLong())).thenReturn(Optional.of(mockBusiness));
        when(shiftRepository.findByEmployeeIdAndBusinessId(anyLong(), anyLong())).thenReturn(oldShifts);

        employeeShiftService.addShiftList(0,0,newShifts);
        fail("Should have thrown an exception.");
    }


}
