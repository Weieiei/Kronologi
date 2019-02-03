package appointmentscheduler.service.employee;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.ShiftRepository;
import appointmentscheduler.repository.UserRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeShiftServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ShiftRepository shiftRepository;

    private EmployeeShiftService employeeShiftService;


    @Before
    public void before() {
        employeeShiftService = new EmployeeShiftService(shiftRepository, userRepository);
    }
    @Test
    public void createShfit() {
        final User mockedUser = mock(User.class);
        final EmployeeShiftDTO employeeShiftDTO = mock(EmployeeShiftDTO.class);
        final LocalDate localDate = LocalDate.now();
        final LocalTime startTime = LocalTime.of(1,0);
        final LocalTime endTime = LocalTime.of(2,0);
        Shift createdShift;

        when(employeeShiftDTO.getEmployeeId()).thenReturn((long)1);
        when(employeeShiftDTO.getDate()).thenReturn(localDate);
        when(employeeShiftDTO.getStartTime()).thenReturn(startTime);
        when(employeeShiftDTO.getEndTime()).thenReturn(endTime);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockedUser));

        createdShift = employeeShiftService.createShift(employeeShiftDTO);

        assertNotNull(createdShift);
        assertEquals(localDate, createdShift.getDate());
        assertEquals(startTime, createdShift.getStartTime());
        assertEquals(endTime, createdShift.getEndTime());
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
    public void deleteShift() {
        final Shift mockedShift1 = mock(Shift.class);
        Shift deletedShift;

        when(mockedShift1.getId()).thenReturn((long) 1);
        when(shiftRepository.findById(anyLong())).thenReturn(Optional.of(mockedShift1));

        deletedShift = employeeShiftService.deleteShift(0);

        assertEquals(mockedShift1.getId(), deletedShift.getId());
    }
}