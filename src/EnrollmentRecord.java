public class EnrollmentRecord {

    public String UserId;
    public String FirstName;
    public String LastName;
    public int Version;
    public String InsuranceCompany;

    public EnrollmentRecord(String userId, String firstName, String lastName, int version, String insuranceCompany) {
        UserId = userId;
        FirstName = firstName;
        LastName = lastName;
        Version = version;
        InsuranceCompany = insuranceCompany;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public int getVersion() {
        return Version;
    }

    public void setVersion(int version) {
        Version = version;
    }

    public String getInsuranceCompany() {
        return InsuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        InsuranceCompany = insuranceCompany;
    }

    @Override
    public String toString() {
        return UserId + "," + FirstName + "," + LastName + "," + Version + "," + InsuranceCompany;
    }
}
