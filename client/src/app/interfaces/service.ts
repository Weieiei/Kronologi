export interface Service {
    readonly id: number;
    name: string;
    duration: number;
    readonly createdAt: Date;
    readonly updatedAt: Date;
}
