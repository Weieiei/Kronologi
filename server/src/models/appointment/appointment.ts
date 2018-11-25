export class Appointment {
    
    private _id : number;
    private _userId : number;
    private _serviceId : number;
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
	
	public getId(): number {
		return this._id;
	}
	
	public setUserId(userId: number): void {
		this._userId = userId;
	}
	
	public getUserId(): number {
		return this._userId;
	}
	
	public setServiceId(serviceId: number): void {
		this._serviceId = serviceId;
	}
	
	public getserviceId(): number {
		return this._serviceId;
	}
	
	public setStartTime(startTime: string): void {
		this._startTime = startTime;
	}
	
	public getStartTime(): string {
		return this._startTime;
	}

	public setEndTime(endTime: string): void {
		this._endTime = endTime;
	}

	public getEndTime(): string {
		return this._endTime;
	}
	
    public setNotes(notes: string): void {
        this._notes = notes;
	}
	
    public getNotes(): string {
        return this._notes;
    }

}
