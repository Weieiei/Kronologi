package appointmentscheduler.dto.user;

public class BankAccountDTO {
    private String routinNumber;

    private String accountNumber;
    private String bankAccountHolderFirstName;
    private String bankAccountHolderLastName;
    private String accountType;

    public String getRoutinNumber() {
        return routinNumber;
    }

    public void setRoutinNumber(String routinNumber) {
        this.routinNumber = routinNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankAccountHolderFirstName() {
        return bankAccountHolderFirstName;
    }

    public void setBankAccountHolderFirstName(String bankAccountHolderFirstName) {
        this.bankAccountHolderFirstName = bankAccountHolderFirstName;
    }

    public String getBankAccountHolderLastName() {
        return bankAccountHolderLastName;
    }

    public void setBankAccountHolderLastName(String bankAccountHolderLastName) {
        this.bankAccountHolderLastName = bankAccountHolderLastName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

}
