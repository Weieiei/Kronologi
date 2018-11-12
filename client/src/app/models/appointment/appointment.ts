import { Time } from '@angular/common';

export class Appointment {

  private _id: number;
  private _user_id: number;
  private _service_id: number;
  private _date: Date;
  private _start_time: Time;
  private _end_time: Time;
  private _notes: string;

  constructor(
      id: number = null,
      user_id: number = null,
      service_id: number = null,
      notes: string = '',
  ) {
      this._id = id;
      this._user_id = user_id;
      this._service_id = service_id;
      this._notes = notes;
  }

  public get id(): number {
      return this._id;
  }

  public get user_id(): number {
      return this._user_id;
  }

  public set user_id(user_id: number) {
      this._user_id = user_id;
  }


  public get service_id(): number {
    return this._service_id;
  }

  public set service_id(service_id: number) {
    this._service_id = service_id;
  }

  public get notes(): string {
    return this._notes;
  }

  public set notes(notes: string) {
    this._notes = notes;
  }

  public get start_time(): Time {
    return this._start_time;
  }

  public set start_time(start_time: Time) {
    this._start_time = start_time;
  }

  public get end_time(): Time {
    return this._end_time;
  }

  public set end_time(end_time: Time) {
    this._end_time = end_time;
  }

  public get date(): Date {
    return this._date;
  }

  public set date(date: Date) {
    this._date = date;
  }
}
