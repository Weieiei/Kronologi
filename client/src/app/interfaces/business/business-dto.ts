import { BusinessHoursDTO } from './businessHours-dto';

export interface BusinessDTO {
    id: number;
    name: string;
    domain: string;
    description: string;
    image : string;
    formattedAddress: string;
    business_hours: BusinessHoursDTO[];
    lat: any;
    lng: any;
}

