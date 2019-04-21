export class Service {

    public readonly id: number;
    public name: string;
    public duration: number;
    public price: number;

    public constructor(id: number, name: string, duration: number) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

}
