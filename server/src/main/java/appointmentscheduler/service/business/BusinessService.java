package appointmentscheduler.service.business;

import appointmentscheduler.entity.appointment.CancelledAppointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.business.BusinessHours;
import appointmentscheduler.exception.BusinessAlreadyExistException;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.exception.UserAlreadyExistsException;
import appointmentscheduler.repository.AppointmentRepository;
import appointmentscheduler.repository.BusinessHoursRepository;
import appointmentscheduler.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessHoursRepository businessHoursRepository;

    public BusinessService(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }


    public long add(Business business) {

        if(businessRepository.findByName(business.getName()).orElse(null) != null){
            throw new BusinessAlreadyExistException(String.format("A user with the email %s already exists.", business.getName()));
        }

        business = businessRepository.save(business);
        long id = business.getId();
        return id;
    }


    public Business findByName(String name){
        return businessRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Business with name %d not found",name)));
    }
    public void addAll(List<BusinessHours> businessHours){
        businessHoursRepository.saveAll(businessHours);
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
