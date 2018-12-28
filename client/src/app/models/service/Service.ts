export class Service {

    public readonly id: number;
    public name: string;
    public duration: number;
    public readonly createdAt: Date;
    public readonly updatedAt: Date;

    public constructor(id: number, name: string, duration: number, createdAt: Date, updatedAt: Date) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
