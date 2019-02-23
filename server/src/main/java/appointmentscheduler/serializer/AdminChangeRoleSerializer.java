package appointmentscheduler.serializer;

import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.RoleRepository;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class AdminChangeRoleSerializer extends StdSerializer<User>{

    private RoleRepository roleRepository;

    public AdminChangeRoleSerializer() {
        this(null);
    }

    public AdminChangeRoleSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(User user, JsonGenerator generator, SerializerProvider serializer) throws IOException{
        generator.writeStartObject();
        generator.writeObjectField("role", roleRepository.findByRole(RoleEnum.EMPLOYEE));
        generator.writeEndObject();
    }
}


