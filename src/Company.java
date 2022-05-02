import java.util.ArrayList;
import java.util.List;

public class Company {
    public String name;
    public List<EnrollmentRecord> records;

    public Company(String name) {
        this.name = name;
        this.records = new ArrayList<>();
    }

    public Company(String name, List<EnrollmentRecord> records) {
        this.name = name;
        this.records = records;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EnrollmentRecord> getRecords() {
        return records;
    }

    public void setRecords(List<EnrollmentRecord> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", records=" + records +
                '}';
    }

    public void addRecord(EnrollmentRecord record){
        this.records.add(record);
    }
}
