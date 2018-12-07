export class Service {

    public id: number;
    public name: string;
	public duration: number;

    constructor(id, name, duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

	public getId(): number {
		return this.id;
	}

	public setName(name: string): void {
		this.name = name;
	}

	public getName(): string {
		return this.name;
	}

	public setDuration(duration: number): void {
		this.duration = duration;
	}

	public getDuration(): number {
		return this.duration;
	}

}
