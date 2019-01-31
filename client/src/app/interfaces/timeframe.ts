import { Time } from '@angular/common';

interface Interval {
  start: Time;
  end: Time;
}

export interface TimeFrame {
  day: string;
  intervals: Interval[];
}
