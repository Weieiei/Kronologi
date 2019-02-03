package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.service.employee.EmployeeShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${rest.api.path}/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final EmployeeShiftService employeeShiftService;

    @Autowired
    AdminController (EmployeeShiftService employeeShiftService) {
        this.employeeShiftService = employeeShiftService;
    }

    // TODO remove this
    @GetMapping
    public String areYouAnAdmin(@RequestAttribute long userId) {
        return String.format("You are an admin, your id is %d.", userId);
    }

    @GetMapping("/employee")
    public  List<User> getEmployees() {
        return employeeShiftService.getEmployees();
    }


    @GetMapping("/employee/{employeeId}")
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
