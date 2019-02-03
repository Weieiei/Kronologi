package appointmentscheduler.controller.rest;

import appointmentscheduler.dto.employee.EmployeeShiftDTO;
import appointmentscheduler.entity.shift.Shift;
import appointmentscheduler.service.employee.EmployeeShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
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

    @PostMapping("/employee/{employeeId}/shift")
    public Shift createShift(@PathVariable long employeeId, @RequestBody EmployeeShiftDTO employeeShiftDTO) throws IOException, MessagingException {
        employeeShiftDTO.setEmployeeId(employeeId);
        return employeeShiftService.createShfit(employeeShiftDTO);
    }

}
