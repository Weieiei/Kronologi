export interface Business {
    readonly id: number;
    name: string;
    domain: string;
    description: string;
    readonly createdAt: Date;
    readonly updatedAt: Date;
}
