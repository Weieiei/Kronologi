export class Service {
    private _id: number;
    private _name: string;
    private _duration: number;

    constructor(
        id: number = null, name: string = '', duration: number = null
    ) {
        this._id = id;
        this._name = name;
        this._duration = duration;
    }

    public get id(): number {
        return this._id;
    }

    public get name(): string {
        return this._name;
    }

    public set name(name: string) {
        this._name = name;
    }

    public get duration(): number {
        return this._duration;
    }

    public set duration(duration: number) {
        this._duration = duration;
    }
}
