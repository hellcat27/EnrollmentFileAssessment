import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CSVFileHandler implements FileHandler {

    List<EnrollmentRecord> records = new ArrayList<>();
    HashMap<String, Integer> headers = new HashMap<>();
    HashMap<String, EnrollmentRecord> recordsMap = new HashMap<>();
    HashMap<String, Company> companyMap = new HashMap<>();
    Comparator<EnrollmentRecord> FirstNameComparator = (record1, record2) -> record1.getFirstName().compareTo(record2.getFirstName());
    Comparator<EnrollmentRecord> LastNameComparator = (record1, record2) -> record1.getLastName().compareTo(record2.getLastName());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    @Override
    public void ReadFile(String file) {
        String extension = GetExtensionOfFile(file);
        if(!Objects.equals(extension, "csv")){
            System.out.println("File provided is not a csv file. Extension: " + extension);
            return;
        }

        String separator = ",";
        Path filePath = Paths.get(file);

        try(BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.US_ASCII)){
            String line = br.readLine();
            boolean getHeaders = true;
            while(line != null){
                String[] attributes = line.split(separator);
                if(getHeaders){
                    for(int a = 0; a < attributes.length; a++){
                        headers.put(attributes[a].toLowerCase().replaceAll("\\s", ""), a);
                    }
                    getHeaders = false;
                }
                else {
                    EnrollmentRecord record = CreateRecord(attributes, headers);

                    if(!recordsMap.containsKey(record.UserId)){
                        records.add(record);
                        recordsMap.put(record.UserId, record);
                    }
                    else{
                        EnrollmentRecord recordLookup = recordsMap.get(record.UserId);
                        if(record.Version > recordLookup.Version){
                            recordLookup.FirstName = record.FirstName;
                            recordLookup.LastName = record.LastName;
                            recordLookup.Version = record.Version;
                            recordLookup.InsuranceCompany = record.InsuranceCompany;
                            System.out.println("Utility detected a duplicate record for UserId " + record.UserId + ". The version number was greater than the one already recorded, therefore it will be overwritten.");
                        }
                        else{
                            System.out.println("Utility detected a duplicate record for UserId " + record.UserId + ". The version number was not greater than the one already recorded, therefore it will not be overwritten.");
                        }
                    }
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Utility encountered an error while reading the file.");
            e.printStackTrace();
        }
        CreateCompanyLists();
        SaveCompanyFiles();
    }

    private static EnrollmentRecord CreateRecord(String[] data, HashMap<String, Integer> headers) {
        Integer userIdIndex = headers.get("userid");
        Integer firstNameIndex = headers.get("firstname");
        Integer lastNameIndex = headers.get("lastname");
        Integer versionIndex = headers.get("version");
        Integer insuranceCompanyIndex = headers.get("insurancecompany");

        String userId = data[userIdIndex];
        String firstName = data[firstNameIndex];
        String lastName = data[lastNameIndex];
        Integer version = null;
        try {
            version = Integer.parseInt(data[versionIndex]);
        } catch (NumberFormatException e) {
            System.out.println("Problem parsing version number in record for userId: " + userId);
            e.printStackTrace();
        }
        String insuranceCompany = data[insuranceCompanyIndex];

        EnrollmentRecord record = new EnrollmentRecord(userId, firstName, lastName, version, insuranceCompany);
        return record;
    }

    private void CreateCompanyLists(){
        System.out.println("Creating record list for each company.");
        for(EnrollmentRecord record : records){
            if(!companyMap.containsKey(record.InsuranceCompany)){
                Company newCompany = new Company(record.InsuranceCompany);
                newCompany.addRecord(record);
                companyMap.put(record.InsuranceCompany, newCompany);
            }
            else{
                Company company = companyMap.get(record.InsuranceCompany);
                company.addRecord(record);
            }
        }
    }

    private void SaveCompanyFiles() {
        companyMap.forEach((key, value) -> {
            Company company = value;
            List<EnrollmentRecord> records = company.records;
            List<EnrollmentRecord> sortedRecords = records.stream().sorted(LastNameComparator.thenComparing(FirstNameComparator)).collect(Collectors.toList());
            String fileName = company.name + "_" + dateFormat.format(new Timestamp(System.currentTimeMillis())) + ".csv";
            try {
                FileWriter csvWriter = new FileWriter(fileName);
                //Add column names to writer
                csvWriter.append("UserId,FirstName,LastName,Version,InsuranceCompany" + System.lineSeparator());
                for(var s : sortedRecords){
                    csvWriter.append(s.toString() + System.lineSeparator());
                }
                System.out.println("Saving file for company: " + company.name);
                csvWriter.flush();
                csvWriter.close();
            } catch (IOException e) {
                System.out.println("Problem saving file for company: " + company.name);
                e.printStackTrace();
            }
        });
    }

    private String GetExtensionOfFile(String file){
        String extension = "";

        int i = file.lastIndexOf('.');
        if(i > 0){
            extension = file.substring(i+1);
        }
        return extension;
    }
}
