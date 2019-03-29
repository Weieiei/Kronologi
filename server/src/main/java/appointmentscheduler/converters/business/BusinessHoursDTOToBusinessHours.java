package appointmentscheduler.converters.business;

import appointmentscheduler.dto.business.BusinessDTO;
import appointmentscheduler.dto.business.BusinessHoursDTO;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.business.BusinessHours;
import appointmentscheduler.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class BusinessHoursDTOToBusinessHours implements Converter<BusinessHoursDTO, BusinessHours> {


    @Autowired
    private BusinessRepository businessRepository;

    @Override
    public BusinessHours convert(BusinessHoursDTO businessDTO) {

        DayOfWeek dayOfWeek = DayOfWeek.valueOf(businessDTO.day.toUpperCase());
        BusinessHours businessHours = new BusinessHours();
        businessHours.setDayOfWeek(dayOfWeek.getValue());
        businessHours.setStartTime(businessDTO.getOpenHour());
        businessHours.setEndTime(businessDTO.getCloseHour());

        return businessHours;
    }
}

