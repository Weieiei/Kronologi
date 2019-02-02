package appointmentscheduler.service.employee;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.ShiftRepository;
import appointmentscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeShiftService {
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;

    @Autowired
    EmployeeShiftService(ShiftRepository shiftRepository, UserRepository userRepository) {
        this.shiftRepository = shiftRepository;
        this.userRepository = userRepository;
    }

    public String createShfit(EmployeeShiftDTO employeeShiftDTO) {
        Optional<User> userList = userRepository.findById(employeeShiftDTO.getEmployeeId());
        User employee = userList.get();
        LocalDate localDate = LocalDate.of(employeeShiftDTO.getYear(), employeeShiftDTO.getMonth(), employeeShiftDTO.getDay());
        LocalTime startTime = LocalTime.of(employeeShiftDTO.getStartHour(), employeeShiftDTO.getStartMinute());
        LocalTime endTime = LocalTime.of(employeeShiftDTO.getEndHour(), employeeShiftDTO.getEndMinute());

        Shift shift = new Shift(employee,localDate, startTime, endTime);
        shiftRepository.save(shift);
        return "Shift sucessfully added";
    }
}
