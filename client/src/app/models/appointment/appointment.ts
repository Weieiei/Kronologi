import { IAppointment } from 'src/app/interfaces/appointment';

export class Appointment implements IAppointment {

  public id: number;
  public user_id: number;
  public service_id: number;
  public date: string;
  public start_time: string;
  public end_time: string;
  public notes: string;

}
