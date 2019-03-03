export interface Business {
    readonly id: number;
    businessName: string;
    domain: string;
    description: string;
    readonly createdAt: Date;
    readonly updatedAt: Date;
}
