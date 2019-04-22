package appointmentscheduler.serializer;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.business.BusinessHours;
import appointmentscheduler.entity.employee_service.EmployeeService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public class BusinessSerializer extends StdSerializer<Business> {

    public BusinessSerializer() {
        this(null);
    }

    public BusinessSerializer(Class<Business> t) {
        super(t);
    }

    @Override
    public void serialize(Business business, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {

        gen.writeStartObject();

        gen.writeObjectField("id", business.getId());
        gen.writeStringField("name", business.getName());
        gen.writeObjectField("domain", business.getDomain());
        gen.writeObjectField("description", business.getDescription());
        gen.writeObjectField("formattedAddress", business.getAddress());
        gen.writeObjectField("image", business.getBusinessLogo());

        serializeAllHours(business.getBusinessHours(), gen);
        gen.writeEndObject();

    }

    private void serializeAllHours(List<BusinessHours> hours, JsonGenerator gen) throws IOException {
        gen.writeArrayFieldStart("business_hours");

        for (BusinessHours businessHours : hours) {
            gen.writeStartObject();

            String dayOfWeek = DayOfWeek.of(businessHours.getDayOfWeek()+1).toString().toLowerCase();
            dayOfWeek = dayOfWeek.substring(0,1).toUpperCase() + dayOfWeek.substring(1).toLowerCase();
            gen.writeObjectField("day", dayOfWeek);
            gen.writeObjectField("openHour", businessHours.getStartTime().toString());
            gen.writeObjectField("closeHour", businessHours.getEndTime().toString());
            gen.writeEndObject();
        }

        gen.writeEndArray();
    }

}
