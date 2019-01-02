package appointmentscheduler.controller.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${rest.api.path}/user")
@PreAuthorize("hasRole('CLIENT')")
public class UserController {

}
