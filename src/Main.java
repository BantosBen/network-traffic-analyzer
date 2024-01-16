import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize the necessary components
        Menu menu = new Menu();
        FileHandler fileHandler = new FileHandler();
        List<DataModel> data = fileHandler.readCSV("data/traffic_data.csv");
        TrafficInspector trafficInspector = new TrafficInspector(data);
        Statistics statistics = new Statistics(data);
        ReportGenerator reportGenerator = new ReportGenerator(data, trafficInspector, statistics);

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            // Display menu
            choice = menu.displayMenuAndGetChoice();

            switch (choice) {
                case 1:
                    // Input data from datafile
                    System.out.println("Enter the file path:");
                    String mFilePath = scanner.next();
                    data = fileHandler.readCSV(mFilePath);
                    trafficInspector = new TrafficInspector(data);
                    statistics = new Statistics(data);
                    reportGenerator = new ReportGenerator(data, trafficInspector, statistics);
                    System.out.println("Data loaded successfully.");
                    break;
                case 2:
                    // Calculate and Display network statistics
                    System.out.println("\nDisplaying statistics...\n");
                    statistics.displayAllStatistics();
                    break;
                case 3:
                    // Display traffic for a user given the IP
                    System.out.print("Enter the IP number to inspect (10-19): ");
                    int ip = scanner.nextInt();
                    statistics.displayTrafficForUser(ip);
                    break;
                case 4:
                    // Save statistics to file
                    System.out.println("Saving statistics to file...");
                    reportGenerator.saveReportToFile();
                    System.out.println("Report saved successfully.");
                    break;
                case 5:
                    // Exit the program
                    menu.close();
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }
}


