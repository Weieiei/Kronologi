package appointmentscheduler.service.employee;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.shift.ShiftFactory;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.exception.ShiftConflictException;
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

    public Shift createShift(long employeeId, EmployeeShiftDTO employeeShiftDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %d not found.", employeeId)));

        Shift shift = ShiftFactory.createShift(employee, employeeShiftDTO.getDate(), employeeShiftDTO.getStartTime(), employeeShiftDTO.getEndTime());

        if(!shiftConflict(employeeId, shift)) {
            return shiftRepository.save(shift);
        }

        throw new ShiftConflictException("This shift conflicts with another one that the employee has.");
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

    public Shift modifyShift(long employeeId, EmployeeShiftDTO employeeShiftDTO, long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Shift with id %d not found.", shiftId)));
        shift.setDate(employeeShiftDTO.getDate());
        shift.setStartTime(employeeShiftDTO.getStartTime());
        shift.setEndTime(employeeShiftDTO.getEndTime());
        if(!shiftConflict(employeeId, shift)) {
           return  shiftRepository.save(shift);
        }
        throw new IllegalArgumentException("Shift conflicts with other shift");
    }

    public void deleteShift(long shiftId){
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Shift with id %d not found.", shiftId)));
        shiftRepository.delete(shift);
    }
}
