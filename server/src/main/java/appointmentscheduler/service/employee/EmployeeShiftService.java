package appointmentscheduler.service.employee;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.EmployeeRepository;
import appointmentscheduler.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeShiftService(ShiftRepository shiftRepository, EmployeeRepository employeeRepository) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployee(long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %d not found.", id)));
    }

    public Shift createShift(EmployeeShiftDTO employeeShiftDTO) {
        Optional<Employee> userList = employeeRepository.findById(employeeShiftDTO.getEmployeeId());
        Employee employee = userList.get();

        Shift shift = new Shift(employee, employeeShiftDTO.getDate(), employeeShiftDTO.getStartTime(), employeeShiftDTO.getEndTime());
        if(!shiftConflict(employeeShiftDTO.getEmployeeId(), shift)) {
            shiftRepository.save(shift);
            return shift;
        }
        return null;
    }

    public boolean shiftConflict(long employeeId, Shift shift) {
        List<Shift> shifts = shiftRepository.findByEmployeeId(employeeId);
        Shift currentShift;
       for(int i = 0; i < shifts.size();i++) {
           currentShift = shifts.get(i);
           if(shift.getDate().isEqual(currentShift.getDate())){
               if((shift.getStartTime().isBefore(currentShift.getEndTime()) && shift.getStartTime().isAfter(currentShift.getStartTime()))
                   || (shift.getEndTime().isAfter(currentShift.getStartTime()) && shift.getEndTime().isBefore(currentShift.getEndTime()))
                   || shift.getStartTime().isBefore(currentShift.getStartTime()) && shift.getEndTime().isAfter(currentShift.getEndTime()))
                   return true;
           }
       }
       return false;
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
            if(!shiftConflict(employeeShiftDTO.getEmployeeId(), shift)) {
                shiftRepository.save(shift);
                return shift;
            }
            return null;
        }
        return shift;
    }

    public Shift deleteShift(long shiftId){
        return shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Shift with id %d not found.", shiftId)));
    }
}
