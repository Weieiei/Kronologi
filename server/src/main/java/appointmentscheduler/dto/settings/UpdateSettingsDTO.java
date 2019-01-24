package appointmentscheduler.dto.settings;

public class UpdateSettingsDTO {

    private boolean emailReminder;
    private boolean textReminder;

    public boolean isEmailReminder() {
        return emailReminder;
    }

    public void setEmailReminder(boolean emailReminder) {
        this.emailReminder = emailReminder;
    }

    public boolean isTextReminder() {
        return textReminder;
    }

    public void setTextReminder(boolean textReminder) {
        this.textReminder = textReminder;
    }
}
