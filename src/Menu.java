import java.util.Scanner;

public class Menu {
    private final Scanner scanner;

    public Menu() {
        scanner = new Scanner(System.in);
    }

    public int displayMenuAndGetChoice() {
        System.out.println("\nNetwork Traffic Analysis System");
        System.out.println("1. Input data from datafile");
        System.out.println("2. Display Statistics");
        System.out.println("3. Display traffic for a user");
        System.out.println("4. Save statistics to file");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }

    public void close() {
        scanner.close();
    }
}

