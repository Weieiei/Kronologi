export class Service {

    private readonly id: number;
    private name: string;
    private duration: number;
    private readonly createdAt: Date;
    private readonly updatedAt: Date;

    public constructor(id: number, name: string, duration: number, createdAt: Date, updatedAt: Date) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public getId(): number {
        return this.id;
    }

    public getName(): string {
        return this.name;
    }

    public setName(name: string): void {
        this.name = name;
    }

    public getDuration(): number {
        return this.duration;
    }

    public setDuration(duration: number): void {
        this.duration = duration;
    }

    public getCreatedAt(): Date {
        return this.createdAt;
    }

    public getUpdatedAt(): Date {
        return this.updatedAt;
    }

}
