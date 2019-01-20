export class SubmitReview {

    public clientId: number;
   public appointmentId: number;
   public review: string;

    public constructor(
      clientId: number, appointmentId: number, review: string
   ) {
       this.clientId = clientId;
       this.appointmentId = appointmentId;
       this.review = review;
   }

}
