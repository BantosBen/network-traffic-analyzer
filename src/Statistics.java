import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
    private final List<DataModel> data;

    public Statistics(List<DataModel> data) {
        this.data = data;
    }

    public void displayTrafficForUser(int ip) {
        System.out.println("\n" + formatColumn("Date", 15) + formatColumn("Local IP", 10) +
                formatColumn("Remote ASN", 15) + formatColumn("Flows", 10));
        System.out.println("------------------------------------------------------------");
        this.data.stream()
                .filter(d -> d.getLocalIP() == ip)
                .forEach(d -> System.out.println(formatColumn(d.getDate(), 15) +
                        formatColumn(String.valueOf(d.getLocalIP()), 10) +
                        formatColumn(String.valueOf(d.getRemoteASN()), 15) +
                        formatColumn(String.valueOf(d.getFlows()), 10)));
    }

    public void displayAllStatistics() {
        System.out.println(formatColumn("Statistics", 35) + formatColumn("Value", 15));
        System.out.println("------------------------------------------------------------");
        calculateAndDisplayTotalConnections();
        calculateAndDisplayTotalFlows();
        calculateAndDisplayAverageFlows();
        calculateAndDisplayMostActiveUsers();
        calculateAndDisplayDailyAverageFlows();
    }


    private void calculateAndDisplayTotalConnections() {
        System.out.println(formatColumn("Total Connections", 35) + formatColumn(String.valueOf(data.size()), 15));
    }

    private void calculateAndDisplayTotalFlows() {
        int totalFlows = data.stream().mapToInt(DataModel::getFlows).sum();
        System.out.println(formatColumn("Total Flows", 35) + formatColumn(String.valueOf(totalFlows), 15));
    }

    private void calculateAndDisplayAverageFlows() {
        double averageFlows = data.stream().mapToInt(DataModel::getFlows).average().orElse(0.0);
        System.out.println(formatColumn("Average Flows per Connection", 35) + formatColumn(String.format("%.2f", averageFlows), 15));
    }

    private void calculateAndDisplayMostActiveUsers() {
        Map<Integer, Integer> userActivity = new HashMap<>();
        for (DataModel entry : data) {
            userActivity.merge(entry.getLocalIP(), entry.getFlows(), Integer::sum);
        }

        int mostActiveUser = Collections.max(userActivity.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println(formatColumn("Most Active User IP", 35) + formatColumn(String.valueOf(mostActiveUser), 15));
    }

    private void calculateAndDisplayDailyAverageFlows() {
        Map<String, Integer> dailyFlows = new HashMap<>();
        Map<String, Integer> dailyCounts = new HashMap<>();

        for (DataModel entry : data) {
            dailyFlows.merge(entry.getDate(), entry.getFlows(), Integer::sum);
            dailyCounts.merge(entry.getDate(), 1, Integer::sum);
        }

        Map<String, Double> dailyAverages = new HashMap<>();
        dailyFlows.forEach((date, totalFlows) -> dailyAverages.put(date, totalFlows / (double) dailyCounts.get(date)));

        System.out.println("\nTop 5 Days with Highest Average Flows:");
        System.out.println(formatColumn("Date", 30) + formatColumn("Avg. Flows", 15));
        System.out.println("------------------------------------------------------------");
        dailyAverages.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> System.out.println(formatColumn(entry.getKey(), 30) + formatColumn(String.format("%.2f", entry.getValue()), 15)));
    }

    private String formatColumn(String input, int length) {
        // If the input string is shorter than the length, pad it with spaces
        if (input.length() < length) {
            return String.format("%1$-" + length + "s", input);
        }
        // If the input string is longer than the length, truncate it
        else if (input.length() > length) {
            return input.substring(0, length);
        }
        // If it's just right, return it as is
        return input;
    }
}
