class Appointment {
    
    private id : string;
    private userId : string;
    private serviceId : string;
    private startTime : string;
    private endTime : string;
    private notes : string; 

    constructor(id, userId, serviceId, startTime, endTime, notes) {
        this.id = id;
        this.userId = userId;
        this.serviceId = serviceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
    }

    public setId(id:string): void{
		this.id = id;
	}
	public getId():string{
		return this.id;
	}
	public setUserId(userId:string): void{
		this.userId = userId;
	}
	public getUserId():string{
		return this.userId;
	}
	public setServiceId(serviceId:string): void{
		this.serviceId = serviceId;
	}
	public getserviceId():string{
		return this.serviceId;
	}
	public setStartTime(startTime:string): void{
		this.startTime = startTime;
	}
	public getStartTime():string{
		return this.startTime;
	}
	public setEndTime(endTime:string): void{
		this.endTime = endTime;
	}
	public getEndTime():string{
		return this.endTime;
    }
    public setNotes(notes:string): void{
        this.notes = notes;
    }
    public getNotes(): string{
        return this.notes;
    }

}

module.exports = Appointment;
