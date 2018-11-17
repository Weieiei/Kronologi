export class Service {

    public id : string;
    public name : string;
    public duration : string;
    constructor(id, name, duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public setId(id:string): void{
		this.id = id;
	}
	public getId():string{
		return this.id;
	}
	public setName(name:string): void{
		this.name = name;
	}
	public getFName():string{
		return this.name;
	}
	public setDuration(duration:string): void{
		this.duration = duration;
	}
	public getDuration():string{
		return this.duration;
	}
}

