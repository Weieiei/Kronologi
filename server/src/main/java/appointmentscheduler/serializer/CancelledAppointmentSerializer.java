package appointmentscheduler.serializer;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.CancelledAppointment;
import appointmentscheduler.entity.user.Employee;
import appointmentscheduler.entity.user.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CancelledAppointmentSerializer extends StdSerializer<CancelledAppointment> {

    public CancelledAppointmentSerializer() {
        this(null);
    }

    public CancelledAppointmentSerializer(Class<CancelledAppointment> t) {
        super(t);
    }

    @Override
    public void serialize(CancelledAppointment cancelledAppointment, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("id", cancelledAppointment.getId());
        serializeUser(cancelledAppointment.getCanceller(),gen);
        serializeEmployee(cancelledAppointment.getAppointment().getEmployee(), gen);
        gen.writeObjectField("reason",cancelledAppointment.getReason());
        gen.writeEndObject();
    }

    private void serializeEmployee(Employee employee, JsonGenerator gen) throws IOException {
        gen.writeObjectFieldStart("employee");
        gen.writeObjectField("id", employee.getId());
        gen.writeStringField("firstName", employee.getFirstName());
        gen.writeStringField("lastName", employee.getLastName());
        gen.writeEndObject();
    }

    private void serializeUser(User user, JsonGenerator gen) throws IOException {
        gen.writeObjectFieldStart("client");
        gen.writeObjectField("id", user.getId());
        gen.writeStringField("firstName", user.getFirstName());
        gen.writeObjectField("lastName", user.getLastName());
        gen.writeEndObject();
    }
}
