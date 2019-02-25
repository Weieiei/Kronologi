package appointmentscheduler.entity.service;

import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.room.Room;

import java.util.Collections;
import java.util.Set;

public class ServiceFactory {
    public static Service createService(String name, int duration) {
        return createService(name, duration, Collections.emptySet());
    }

    public static Service createService(Business business, String name, int duration) {
        return createService(business, name, duration, Collections.emptySet());
    }

    public static Service createService(String name, int duration, Set<Room> rooms) {
        final Service service = new Service();

        service.setName(name);
        service.setDuration(duration);
        service.setRooms(rooms);

        return service;
    }

    public static Service createService(Business business, String name, int duration, Set<Room> rooms) {
        final Service service = new Service();

        service.setBusiness(business);
        service.setName(name);
        service.setDuration(duration);
        service.setRooms(rooms);

        return service;
    }
}
