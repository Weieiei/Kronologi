package appointmentscheduler.service.employee;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.ResourceNotFoundException;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeShiftServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ShiftRepository shiftRepository;

    private EmployeeShiftService employeeShiftService;


    @Before
    public void before() {
        employeeShiftService = new EmployeeShiftService(shiftRepository, employeeRepository);
    }

//    @Test
//    public void createShift() {
//        final Employee mockEmployee = mock(Employee.class);
//        final EmployeeShiftDTO employeeShiftDTO = mock(EmployeeShiftDTO.class);
//        final LocalDate localDate = LocalDate.now();
//        final LocalTime startTime = LocalTime.of(1,0);
//        final LocalTime endTime = LocalTime.of(2,0);
//        Shift createdShift;
//
//        when(employeeShiftDTO.getDate()).thenReturn(localDate);
//        when(employeeShiftDTO.getStartTime()).thenReturn(startTime);
//        when(employeeShiftDTO.getEndTime()).thenReturn(endTime);
//
//        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(mockEmployee));
//
//        createdShift = employeeShiftService.createShift(1, employeeShiftDTO);
//
//        assertNotNull(createdShift);
//        assertEquals(localDate, createdShift.getDate());
//        assertEquals(startTime, createdShift.getStartTime());
//        assertEquals(endTime, createdShift.getEndTime());
//    }

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

//    @Test
//    public void deleteShift() {
//        final Shift mockedShift1 = mock(Shift.class);
//        Shift deletedShift;
//
//        when(mockedShift1.getId()).thenReturn((long) 1);
//        when(shiftRepository.findById(anyLong())).thenReturn(Optional.of(mockedShift1));
//
//        deletedShift = employeeShiftService.deleteShift(0);
//
//        assertEquals(mockedShift1.getId(), deletedShift.getId());
//    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteInvalidShift() {
        employeeShiftService.deleteShift(1L);
        fail("Should have thrown an exception.");
    }

//    @Test
//    public void modifyShift() {
//        final LocalDate localDate = LocalDate.now();
//        final EmployeeShiftDTO employeeShiftDTO = mock(EmployeeShiftDTO.class);
//        final LocalTime startTime = LocalTime.of(1,0);
//        final LocalTime endTime = LocalTime.of(2,0);
//        final LocalTime newStartTime = LocalTime.of(2,0);
//        final LocalTime newEndTime = LocalTime.of(3,0);
//        Shift shift = new Shift();
//        Shift modifiedShift;
//
//
//        shift.setId(1);
//        shift.setDate(localDate);
//        shift.setStartTime(startTime);
//        shift.setEndTime(endTime);
//
//        when(shiftRepository.findById(anyLong())).thenReturn(Optional.of(shift));
//        when(employeeShiftDTO.getDate()).thenReturn(localDate);
//        when(employeeShiftDTO.getStartTime()).thenReturn(newStartTime);
//        when(employeeShiftDTO.getEndTime()).thenReturn(newEndTime);
//
//        modifiedShift = employeeShiftService.modifyShift(employeeShiftDTO, 1);
//
//        assertEquals(1, modifiedShift.getId());
//        assertEquals(localDate, modifiedShift.getDate());
//        assertEquals(newStartTime, modifiedShift.getStartTime());
//        assertEquals(newEndTime, modifiedShift.getEndTime());
//
//    }

//    @Test
//    public void modifyInvalidShift() {
//        final EmployeeShiftDTO employeeShiftDTO = mock(EmployeeShiftDTO.class);
//        Shift modifiedShift;
//
//        modifiedShift = employeeShiftService.modifyShift(employeeShiftDTO, anyLong());
//
//        assertNull(modifiedShift);
//    }

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
    public void noConflict() {
        List<Shift> oldShifts = new ArrayList<>();
        Shift oldShift = new Shift();
        Shift newShift = new Shift();

        oldShift.setDate(LocalDate.of(2019,1,1));
        oldShift.setStartTime(LocalTime.of(1,0));
        oldShift.setEndTime(LocalTime.of(2,0));

        oldShifts.add(oldShift);

        newShift.setDate(LocalDate.of(2019,1,1));
        newShift.setStartTime(LocalTime.of(3,0));
        newShift.setEndTime(LocalTime.of(4,0));

        when(shiftRepository.findByEmployeeId(anyLong())).thenReturn(oldShifts);

        assertFalse(employeeShiftService.shiftConflict(anyLong(),newShift));
    }

    @Test
    public void conflictStart() {
        List<Shift> oldShifts = new ArrayList<>();
        Shift oldShift = new Shift();
        Shift newShift = new Shift();

        oldShift.setDate(LocalDate.of(2019,1,1));
        oldShift.setStartTime(LocalTime.of(1,0));
        oldShift.setEndTime(LocalTime.of(2,0));

        oldShifts.add(oldShift);

        newShift.setDate(LocalDate.of(2019,1,1));
        newShift.setStartTime(LocalTime.of(0,30));
        newShift.setEndTime(LocalTime.of(1,30));

        when(shiftRepository.findByEmployeeId(anyLong())).thenReturn(oldShifts);

        assertTrue(employeeShiftService.shiftConflict(anyLong(),newShift));
    }

    @Test
    public void conflictEnd() {
        List<Shift> oldShifts = new ArrayList<>();
        Shift oldShift = new Shift();
        Shift newShift = new Shift();

        oldShift.setDate(LocalDate.of(2019,1,1));
        oldShift.setStartTime(LocalTime.of(1,0));
        oldShift.setEndTime(LocalTime.of(2,0));

        oldShifts.add(oldShift);

        newShift.setDate(LocalDate.of(2019,1,1));
        newShift.setStartTime(LocalTime.of(1,30));
        newShift.setEndTime(LocalTime.of(4,0));

        when(shiftRepository.findByEmployeeId(anyLong())).thenReturn(oldShifts);

        assertTrue(employeeShiftService.shiftConflict(anyLong(),newShift));
    }

    @Test
    public void conflictOver() {
        List<Shift> oldShifts = new ArrayList<>();
        Shift oldShift = new Shift();
        Shift newShift = new Shift();

        oldShift.setDate(LocalDate.of(2019,1,1));
        oldShift.setStartTime(LocalTime.of(1,0));
        oldShift.setEndTime(LocalTime.of(2,0));

        oldShifts.add(oldShift);

        newShift.setDate(LocalDate.of(2019,1,1));
        newShift.setStartTime(LocalTime.of(0,30));
        newShift.setEndTime(LocalTime.of(3,30));

        when(shiftRepository.findByEmployeeId(anyLong())).thenReturn(oldShifts);

        assertTrue(employeeShiftService.shiftConflict(anyLong(),newShift));
    }
}
