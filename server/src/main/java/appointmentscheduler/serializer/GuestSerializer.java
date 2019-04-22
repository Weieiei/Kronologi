package appointmentscheduler.serializer;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.guest.Guest;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class GuestSerializer extends StdSerializer<Appointment> {

    public GuestSerializer() {
        this(null);
    }

    public GuestSerializer(Class<Appointment> t) {
        super(t);
    }

    @Override
    public void serialize(Appointment appointment, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("id", appointment.getId());
        gen.writeStringField("date", appointment.getDate().toString());
        gen.writeStringField("startTime", appointment.getStartTime().toString());
        gen.writeStringField("endTime", appointment.getEndTime().toString());
        gen.writeStringField("notes", appointment.getNotes());
        gen.writeObjectField("status", appointment.getStatus());
        serializeBusiness(appointment.getBusiness(), gen);
        serializeEmployee(appointment.getEmployee(), gen);
        serializeService(appointment.getService(), gen);
        serializeGuest(appointment.getGuest(), gen);

        gen.writeEndObject();
    }

    private void serializeEmployee(Employee employee, JsonGenerator gen) throws IOException {
        gen.writeObjectFieldStart("employee");
        gen.writeObjectField("id", employee.getId());
        gen.writeStringField("firstName", employee.getFirstName());
        gen.writeStringField("lastName", employee.getLastName());
        gen.writeEndObject();
    }

    private void serializeService(Service service, JsonGenerator gen) throws IOException {
        gen.writeObjectFieldStart("service");
        gen.writeObjectField("id", service.getId());
        gen.writeStringField("name", service.getName());
        gen.writeObjectField("duration", service.getDuration());
        gen.writeEndObject();
    }

    private void serializeGuest(Guest guest, JsonGenerator gen) throws IOException {
        gen.writeObjectFieldStart("guest");
        gen.writeObjectField("id", guest.getId());
        gen.writeStringField("firstName", guest.getFirstName());
        gen.writeObjectField("lastName", guest.getLastName());
        gen.writeObjectField("phoneNumber", guest.getPhoneNumber());
        gen.writeEndObject();
    }

    public void serializeBusiness(Business business, JsonGenerator gen) throws IOException {

        gen.writeObjectFieldStart("business");
        gen.writeObjectField("id", business.getId());
        gen.writeStringField("name", business.getName());
        gen.writeObjectField("domain", business.getDomain());
        gen.writeObjectField("description", business.getDescription());

        gen.writeEndObject();

    }
}
