export class Service {

    private readonly id: number;
    private name: string;
    private duration: number;

    public constructor(id: number, name: string, duration: number) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public getId(): number {
        return this.id;
    }

    public getName(): string {
        return this.name;
    }

    public setName(name: string) {
        this.name = name;
    }

    public getDuration(): number {
        return this.duration;
    }

    public setDuration(duration: number) {
        this.duration = duration;
    }
}
