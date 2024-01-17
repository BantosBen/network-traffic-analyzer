import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileHandler {
    private List<DataModel> data;

    public List<DataModel> getData() {
        return data;
    }

    public FileHandler() {
        this.data = new ArrayList<>();
    }

    public void readCSV(String filePath) {
        List<DataModel> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                DataModel data = new DataModel(values[0], Integer.parseInt(values[1].trim()),
                        Integer.parseInt(values[2].trim()), Integer.parseInt(values[3].trim()));
                dataList.add(data);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        this.data = dataList;
    }

    public void writeToFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
