package appointmentscheduler.service.employee;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.event.AppEvent;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.exception.AppEventTimeConflict;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.exception.ShiftConflictException;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.repository.EmployeeRepository;
import appointmentscheduler.repository.ShiftRepository;
import appointmentscheduler.util.DateConflictChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final BusinessRepository businessRepository;

    @Autowired
    public EmployeeShiftService(ShiftRepository shiftRepository,
                                EmployeeRepository employeeRepository,
                                BusinessRepository businessRepository) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
        this.businessRepository = businessRepository;
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getEmployeesForBusiness(long businessId) {
        if (employeeRepository.findByBusinessId(businessId).isEmpty())
            throw new ResourceNotFoundException(String.format("Employees from business %d with not found.",
                    businessId));
        else return employeeRepository.findByBusinessId(businessId);
    }

    public Employee getEmployeeByBusinessId(long id, long businessId) {
        return employeeRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %d not found.", id)));
    }

    public Shift createShiftForBusiness(long employeeId, long businessId, EmployeeShiftDTO employeeShiftDTO) {

        Employee employee = employeeRepository.findByIdAndBusinessId(employeeId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %d not found.",
                        employeeId)));
        Business business =
                businessRepository.findById(businessId) .orElseThrow(() -> new ResourceNotFoundException(String.format("Business with id %d not found.", businessId)));
                if (employee == null) throw new ResourceNotFoundException(String.format("Employee with id %d not " +
                        "found.", employeeId));

        if(!shiftConflict(employeeId, businessId, employeeShiftDTO)) {
            Shift shift = employeeShiftDTO.convertToShift(employee, business);
            return shiftRepository.save(shift);
        }

        throw new ShiftConflictException("This shift conflicts with another one that the employee has.");
    }

    public List<EmployeeShiftDTO> addShiftList(long employeeId, long businessId, List<EmployeeShiftDTO> employeeShiftDTOS) {
        Employee employee = employeeRepository.findByIdAndBusinessId(employeeId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %d not found.",
                        employeeId)));
        Business business =
                businessRepository.findById(businessId) .orElseThrow(() -> new ResourceNotFoundException(String.format("Business with id %d not found.", businessId)));
        if (employee == null) throw new ResourceNotFoundException(String.format("Employee with id %d not " +
                "found.", employeeId));

        if(!shiftConflict(employeeId, businessId, employeeShiftDTOS)){
            for(int i = 0;i < employeeShiftDTOS.size(); i++) {
                shiftRepository.save(employeeShiftDTOS.get(i).convertToShift(employee, business));
            }
        } else {
            throw new AppEventTimeConflict("Conflict between new shifts and existing shifts");
        }

        return  employeeShiftDTOS;
    }

    private boolean shiftConflict(long employeeId, long businessId, AppEvent shift) {
        List<Shift> shifts = shiftRepository.findByEmployeeIdAndBusinessId(employeeId, businessId);
        return  DateConflictChecker.hasConflictList(shifts, shift);
    }

    private boolean shiftConflict(long employeeId, long businessId, List<? extends AppEvent> newShift) {
        List<Shift> shifts = shiftRepository.findByEmployeeIdAndBusinessId(employeeId, businessId);
        return  DateConflictChecker.hasConflictSeveralEvents(shifts, newShift);
    }

    public List<Shift> getEmployeeShifts(long employeeId) {
        return shiftRepository.findByEmployeeId(employeeId);
    }

    public List<Shift> getEmployeeShiftsForBusiness(long employeeId, long businessId) {
        return shiftRepository.findByEmployeeIdAndBusinessId(employeeId,businessId);
    }
     public Shift modifyShift(long businessId, long employeeId, EmployeeShiftDTO employeeShiftDTO, long shiftId) {

        Shift shift = shiftRepository.findByIdAndBusinessId(shiftId, businessId).orElseThrow(() -> new ResourceNotFoundException(String.format("Shift with id %d and business %d not found.", shiftId, businessId)));
        Business business = businessRepository.findById(businessId) .orElseThrow(() -> new ResourceNotFoundException(String.format("Business with id %d not found.", businessId)));

        shift.setDate(employeeShiftDTO.getDate());
        shift.setStartTime(employeeShiftDTO.getStartTime());
        shift.setEndTime(employeeShiftDTO.getEndTime());
        shift.setBusiness(business);
        if(!shiftConflict(businessId, employeeId, shift)) {
            return  shiftRepository.save(shift);
        }
        throw new IllegalArgumentException("Shift conflicts with other shift");
  }

    public void deleteShift(long shiftId, long businessId){
            Shift shift = shiftRepository.findByIdAndBusinessId(shiftId, businessId).orElseThrow(() -> new ResourceNotFoundException(String.format("Shift with id %d and business %d not found.", shiftId, businessId)));
            shiftRepository.delete(shift);
        }
}
