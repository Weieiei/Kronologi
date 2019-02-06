package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.serializer.AdminEmployeeSerializer;
import appointmentscheduler.serializer.ObjectMapperFactory;
import appointmentscheduler.service.employee.EmployeeShiftService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
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

    @GetMapping("/employee/{employeeId}/shift")
    public List<Shift> getEmployeeShifts(@PathVariable long employeeId) {
        return employeeShiftService.getEmployeeShifts(employeeId);
    }

    @PostMapping("/employee/{employeeId}/shift")
    public Shift createShift(@PathVariable long employeeId, @RequestBody EmployeeShiftDTO employeeShiftDTO) throws IOException, MessagingException {
        employeeShiftDTO.setEmployeeId(employeeId);
        return employeeShiftService.createShift(employeeShiftDTO);
    }

    @PutMapping("/employee/{employeeId}/shift/{shiftId}")
    public Shift modifyShift(@PathVariable long employeeId, @PathVariable long shiftId, @RequestBody EmployeeShiftDTO employeeShiftDTO) {
        employeeShiftDTO.setEmployeeId(employeeId);
        return employeeShiftService.modifyShift(employeeShiftDTO, shiftId);
    }

    @DeleteMapping("/employee/{employeeId}/shift/{shiftId}")
    public Shift deleteShift(@PathVariable long shiftId){
        return employeeShiftService.deleteShift(shiftId);
    }

}
