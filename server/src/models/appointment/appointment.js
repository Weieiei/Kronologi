class Appointment {

    constructor(id, userId, serviceId, startTime, endTime, notes) {
        this.id = id;
        this.userId = userId;
        this.serviceId = serviceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
    }

}

module.exports = Appointment;
