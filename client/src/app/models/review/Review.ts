export class Review {

    public readonly id: number;
   public clientId: number;
   public appointmentId: number;
   public review: string;

    public constructor(
       id: number, clientId: number, appointmentId: number, review: string
   ) {
       this.id = id;
       this.clientId = clientId;
       this.appointmentId = appointmentId;
       this.review = review;
   }

}
