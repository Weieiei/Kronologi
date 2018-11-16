export class Service {

    public _id : string;
    public _name : string;
    public _duration : string;
    constructor(id, name, duration) {
        this._id = id;
        this._name = name;
        this._duration = duration;
    }

    public setId(id:string): void{
		this._id = id;
	}
	public getId():string{
		return this._id;
	}
	public setName(name:string): void{
		this._name = name;
	}
	public getFName():string{
		return this._name;
	}
	public setDuration(duration:string): void{
		this._duration = duration;
	}
	public getDuration():string{
		return this._duration;
	}
}

