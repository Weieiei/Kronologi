export class Appointment {

    private _id: number;
    private _userId: number;
    private _serviceId: number;
    private _startTime: string;
    private _endTime: string;
    private _notes: string;

    constructor(
        id: number = null,
        userId: number = null, serviceId: number = null,
        startTime: string = '', endTime: string = null, notes: string = null
    ) {
        this._id = id;
        this._userId = userId;
        this._serviceId = serviceId;
        this._startTime = startTime;
        this._endTime = endTime;
        this._notes = notes;
    }

    public get id(): number {
        return this._id;
    }

    public get userId(): number {
        return this._userId;
    }

    public get serviceId(): number {
        return this._serviceId;
    }

    public set serviceId(serviceId: number) {
        this._serviceId = serviceId;
    }

    public get startTime(): string {
        return this._startTime;
    }

    public set startTime(startTime: string) {
        this._startTime = startTime;
    }

    public get endTime(): string {
        return this._endTime;
    }

    public set endTime(endTime: string) {
        this._endTime = endTime;
    }

    public get notes(): string {
        return this._notes;
    }

    public set notes(notes: string) {
        this._notes = notes;
    }

}
