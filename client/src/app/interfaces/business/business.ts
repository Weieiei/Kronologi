export interface Business {
    readonly id: number;
    name: string;
    domain: string;
    description: string;

    formattedAddress: string;
    readonly createdAt: Date;
    readonly updatedAt: Date;
}
