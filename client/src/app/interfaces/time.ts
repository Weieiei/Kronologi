/**
 * Used for the time picker component used for booking.
 * A time is enabled if the employee we want to book with is available at that time.
 */
export interface Time {
    hour: number;
    minute: number;
    enabled: boolean;
}
