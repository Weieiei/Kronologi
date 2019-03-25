package appointmentscheduler.service.business;

import appointmentscheduler.entity.appointment.CancelledAppointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.AppointmentRepository;
import appointmentscheduler.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    public BusinessService(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    public Map<String, String> add(Business business) {
        try{
            businessRepository.save(business);
            return message ("Successfully added business");
        }
        catch (Exception e) {
            return message(e.getMessage());
        }
    }

    private Map<String, String> message(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }

    public Business findById(long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Business with id %d not found.", id)));
    }
}
