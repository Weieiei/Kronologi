package appointmentscheduler.converters.business;

import appointmentscheduler.dto.business.BusinessDTO;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BusinessDTOToBusiness implements Converter<BusinessDTO, Business> {

    @Autowired
    private BusinessRepository businessRepository;

    @Override
    public Business convert(BusinessDTO businessDTO) {

        Business business = new Business();
        business.setName(businessDTO.getName());
        business.setDomain(businessDTO.getDomain());
        business.setDescription(businessDTO.getDescription());

        return business;
    }
}
