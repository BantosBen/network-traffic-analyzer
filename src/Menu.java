import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner;
    private final FileHandler fileHandler;
    private TrafficInspector trafficInspector;
    private Statistics statistics;
    private ReportGenerator reportGenerator;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.fileHandler = new FileHandler();
        fileHandler.readCSV("data/traffic_data.csv");
        trafficInspector = new TrafficInspector(fileHandler.getData());
        statistics = new Statistics(fileHandler.getData());
        reportGenerator = new ReportGenerator(fileHandler, trafficInspector);
    }

    public void start() {
        int choice;
        do {
            // Display menu
            choice = this.displayMenuAndGetChoice();
            this.processUserChoice(choice);
        } while (choice != 5);
    }

    private int displayMenuAndGetChoice() {
        System.out.println("\nNetwork Traffic Analysis System");
        System.out.println("1. Input data from datafile");
        System.out.println("2. Display Statistics");
        System.out.println("3. Display traffic for a user");
        System.out.println("4. Save statistics to file");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
        return this.scanner.nextInt();
    }

    private void processUserChoice(int choice) {
        switch (choice) {
            case 1:
                // Input data from datafile
                System.out.println("Enter the file path:");
                String mFilePath = scanner.next();
                fileHandler.readCSV(mFilePath);
                trafficInspector = new TrafficInspector(fileHandler.getData());
                statistics = new Statistics(fileHandler.getData());
                reportGenerator = new ReportGenerator(fileHandler, trafficInspector);
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
                this.close();
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice, please try again.");
        }
    }

    private void close() {
        scanner.close();
    }
}