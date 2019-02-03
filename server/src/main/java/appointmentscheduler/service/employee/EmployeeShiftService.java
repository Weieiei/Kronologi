package appointmentscheduler.service.employee;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.ShiftRepository;
import appointmentscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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

    public List<User> getEmployees() {
        return userRepository.findByRoles_Role(RoleEnum.EMPLOYEE);
    }

    public Shift createShift(EmployeeShiftDTO employeeShiftDTO) {
        Optional<User> userList = userRepository.findById(employeeShiftDTO.getEmployeeId());
        User employee = userList.get();

        Shift shift = new Shift(employee,employeeShiftDTO.getDate(), employeeShiftDTO.getStartTime(), employeeShiftDTO.getEndTime());
        shiftRepository.save(shift);
        return shift;
    }

    public List<Shift> getEmployeeShifts(long employeeId) {
        return shiftRepository.findByEmployeeId(employeeId);
    }

    public Shift modifyShift(EmployeeShiftDTO employeeShiftDTO, long shiftId) {
        Optional<Shift> shiftList = shiftRepository.findById(shiftId);
        Shift shift = null;
        if(shiftList.isPresent()) {
            shift = shiftList.get();
            shift.setDate(employeeShiftDTO.getDate());
            shift.setStartTime(employeeShiftDTO.getStartTime());
            shift.setEndTime(employeeShiftDTO.getEndTime());
            shiftRepository.save(shift);
        }
        return shift;
    }

    public Shift deleteShift(long shiftId){
        Optional<Shift> shiftList = shiftRepository.findById(shiftId);
        Shift shift = null;
        if(shiftList.isPresent()) {
            shift = shiftList.get();
            shiftRepository.delete(shift);
        }
        return shift;
    }
}
