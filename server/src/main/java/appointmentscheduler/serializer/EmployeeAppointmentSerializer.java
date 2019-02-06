package appointmentscheduler.serializer;

import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.user.Employee;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class EmployeeAppointmentSerializer extends StdSerializer<Appointment> {

    public EmployeeAppointmentSerializer() {
        this(null);
    }

    public EmployeeAppointmentSerializer(Class<Appointment> t) {
        super(t);
    }

    @Override
    public void serialize(Appointment appointment, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("id", appointment.getId());
        serializeEmployee(appointment.getEmployee(), gen);
        gen.writeStringField("date", appointment.getDate().toString());
        gen.writeStringField("startTime", appointment.getStartTime().toString());
        gen.writeStringField("endTime", appointment.getEndTime().toString());

        gen.writeEndObject();
    }

    private void serializeEmployee(Employee employee, JsonGenerator gen) throws IOException {
        gen.writeObjectFieldStart("employee");
        gen.writeObjectField("id", employee.getId());
        gen.writeStringField("firstName", employee.getFirstName());
        gen.writeStringField("lastName", employee.getLastName());
        gen.writeEndObject();
    }
}
