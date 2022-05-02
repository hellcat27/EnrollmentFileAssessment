import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInterface {

    public void Run(){
        HandleCSVFile();
    }
    public void HandleCSVFile(){
        System.out.print("Enter the filename for the CSV import: ");
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));

            // Reading data using readLine
            String path = reader.readLine();

            // Printing the read line
            System.out.println(path);
            CSVFileHandler csvReader = new CSVFileHandler();
            csvReader.ReadFile(path);
            System.out.print("Task complete.");
        } catch(IOException ie) {
            ie.printStackTrace();
        }
    }

}
