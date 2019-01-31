import { TimeFrame } from './timeframe';

export interface Employee {
  name: string;
  availabilities: TimeFrame[];
}
