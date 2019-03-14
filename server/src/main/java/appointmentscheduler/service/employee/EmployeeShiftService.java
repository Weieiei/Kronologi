package appointmentscheduler.service.employee;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.Event;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.shift.ShiftFactory;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.exception.ShiftConflictException;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.repository.EmployeeRepository;
import appointmentscheduler.repository.ShiftRepository;
import appointmentscheduler.util.DateConflictChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
   // private final BusinessRepository businessRepository;

    @Autowired
    public EmployeeShiftService(ShiftRepository shiftRepository,
                                EmployeeRepository employeeRepository) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
     //   this.businessRepository = businessRepository;
    }

//    @Autowired
//    public EmployeeShiftService(ShiftRepository shiftRepository,
//                                EmployeeRepository employeeRepository,
//                                BusinessRepository businessRepository) {
//        this.shiftRepository = shiftRepository;
//        this.employeeRepository = employeeRepository;
//           this.businessRepository = businessRepository;
//    }


    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getEmployeesForBusiness(long businessId) {
        if (employeeRepository.findByBusinessId(businessId).isEmpty())
            throw new ResourceNotFoundException(String.format("Employees from business %d with not found.",
                    businessId));
        else return employeeRepository.findByBusinessId(businessId);
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
        return  DateConflictChecker.hasConflictList(shifts, shift);
    }

    public List<Shift> getEmployeeShifts(long employeeId) {
        return shiftRepository.findByEmployeeId(employeeId);
    }

    public List<Shift> getEmployeeShiftsForBusiness(long employeeId, long businessId) {
        return shiftRepository.findByEmployeeIdAndBusinessId(employeeId,businessId);
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
