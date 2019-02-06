package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.serializer.AdminEmployeeSerializer;
import appointmentscheduler.serializer.AdminEmployeeShiftSerializer;
import appointmentscheduler.serializer.ObjectMapperFactory;
import appointmentscheduler.service.employee.EmployeeShiftService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${rest.api.path}/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController extends AbstractController {

    private final EmployeeShiftService employeeShiftService;
    private final ObjectMapperFactory objectMapperFactory;

    @Autowired
    public AdminController(EmployeeShiftService employeeShiftService, ObjectMapperFactory objectMapperFactory) {
        this.employeeShiftService = employeeShiftService;
        this.objectMapperFactory = objectMapperFactory;
    }

    @GetMapping("/employee")
    public ResponseEntity<String> getEmployees() {
        List<Employee> employees = employeeShiftService.getEmployees();
        final ObjectMapper mapper = objectMapperFactory.createMapper(Employee.class, new AdminEmployeeSerializer());
        return getJson(mapper, employees);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<String> getEmployees(@PathVariable long id) {
        Employee employee = employeeShiftService.getEmployee(id);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Employee.class, new AdminEmployeeSerializer());
        return getJson(mapper, employee);
    }

    @GetMapping("/employee/{employeeId}/shift")
    public ResponseEntity<String> getEmployeeShifts(@PathVariable long employeeId) {
        List<Shift> shifts = employeeShiftService.getEmployeeShifts(employeeId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
        return getJson(mapper, shifts);
    }

    @PostMapping("/employee/{employeeId}/shift")
    public ResponseEntity<String> createShift(@PathVariable long employeeId, @RequestBody EmployeeShiftDTO employeeShiftDTO) {
        Shift shift = employeeShiftService.createShift(employeeId, employeeShiftDTO);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
        return getJson(mapper, shift);
    }

//    @PutMapping("/employee/{employeeId}/shift/{shiftId}")
//    public ResponseEntity<String> modifyShift(@PathVariable long employeeId, @PathVariable long shiftId, @RequestBody EmployeeShiftDTO employeeShiftDTO) {
//        employeeShiftDTO.setEmployeeId(employeeId);
//        Shift shift = employeeShiftService.modifyShift(employeeShiftDTO, shiftId);
//        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
//        return getJson(mapper, shift);
//    }

    @DeleteMapping("/employee/shift/{shiftId}")
    public ResponseEntity<String> deleteShift(@PathVariable long shiftId){
        Shift shift = employeeShiftService.deleteShift(shiftId);
        final ObjectMapper mapper = objectMapperFactory.createMapper(Shift.class, new AdminEmployeeShiftSerializer());
        return getJson(mapper, shift);
    }

}
