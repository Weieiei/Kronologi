export class Appointment {
    
    private _id : string;
    private _userId : string;
    private _serviceId : string;
    private _startTime : string;
    private _endTime : string;
    private _notes : string; 

    constructor(id, userId, serviceId, startTime, endTime, notes) {
        this._id = id;
        this._userId = userId;
        this._serviceId = serviceId;
        this._startTime = startTime;
        this._endTime = endTime;
        this._notes = notes;
    }

    public setId(id:string): void{
		this._id = id;
	}
	public getId():string{
		return this._id;
	}
	public setUserId(userId:string): void{
		this._userId = userId;
	}
	public getUserId():string{
		return this._userId;
	}
	public setServiceId(serviceId:string): void{
		this._serviceId = serviceId;
	}
	public getserviceId():string{
		return this._serviceId;
	}
	public setStartTime(startTime:string): void{
		this._startTime = startTime;
	}
	public getStartTime():string{
		return this._startTime;
	}
	public setEndTime(endTime:string): void{
		this._endTime = endTime;
	}
	public getEndTime():string{
		return this._endTime;
    }
    public setNotes(notes:string): void{
        this._notes = notes;
    }
    public getNotes(): string{
        return this._notes;
    }

}
