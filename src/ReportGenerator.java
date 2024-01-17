import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    private final List<DataModel> data;
    private final TrafficInspector trafficInspector;
    private final FileHandler fileHandler;

    public ReportGenerator(FileHandler fileHandler, TrafficInspector trafficInspector) {
        this.data = fileHandler.getData();
        this.fileHandler = fileHandler;
        this.trafficInspector = trafficInspector;
    }

    private String generateReport() {
        StringBuilder report = new StringBuilder();
        Map<Integer, Integer> userActivity = getUserActivity();

        report.append("Network Traffic Analysis Report\n");
        report.append("====================================\n");

        // Add statistics summary
        report.append("Statistics Summary:\n");
        report.append(formatColumn("Total Connections", 30)).append(formatColumn(String.valueOf(data.size()), 15)).append("\n");
        report.append(formatColumn("Total Flows", 30)).append(formatColumn(String.valueOf(data.stream().mapToInt(DataModel::getFlows).sum()), 15)).append("\n");
        report.append(formatColumn("Average Flows per Connection", 30)).append(formatColumn(String.format("%.2f", data.stream().mapToInt(DataModel::getFlows).average().orElse(0.0)), 15)).append("\n");
        report.append(formatColumn("Most Active User IP", 35)).append(formatColumn(String.valueOf(Collections.max(userActivity.entrySet(), Map.Entry.comparingByValue()).getKey()), 15)).append("\n");

        // Add most active users
        report.append("\nMost Active Users:\n");

        userActivity.entrySet().stream().sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed()).limit(5) // Top 5 most active users
                .forEach(entry -> report.append(formatColumn("IP " + entry.getKey(), 30)).append(formatColumn(entry.getValue() + " flows", 15)).append("\n"));

        // Add most active days
        report.append("\nMost Active Days:\n");
        Map<String, Double> dailyAverages = calculateDailyAverages();
        dailyAverages.entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed()).limit(5).forEach(entry -> report.append(formatColumn(entry.getKey(), 30)).append(formatColumn(String.format("%.2f avg flows", entry.getValue()), 30)).append("\n"));

        // Add anomalies summary
        report.append("\nTop 5 Anomaly traffic:\n");
        report.append(formatColumn("Date", 15)).append(formatColumn("Local IP", 10)).append(formatColumn("Remote ASN", 15)).append(formatColumn("Flows", 10)).append("\n");
        List<DataModel> anomalies = trafficInspector.detectAnomalies();
        anomalies.stream().limit(5).forEach(anomaly -> report.append(formatColumn(anomaly.getDate(), 15)).append(formatColumn(String.valueOf(anomaly.getLocalIP()), 10)).append(formatColumn(String.valueOf(anomaly.getRemoteASN()), 15)).append(formatColumn(String.valueOf(anomaly.getFlows()), 10)).append("\n"));

        return report.toString();
    }

    private Map<String, Double> calculateDailyAverages() {
        Map<String, Integer> dailyFlows = new HashMap<>();
        Map<String, Integer> dailyCounts = new HashMap<>();

        for (DataModel entry : data) {
            dailyFlows.merge(entry.getDate(), entry.getFlows(), Integer::sum);
            dailyCounts.merge(entry.getDate(), 1, Integer::sum);
        }

        Map<String, Double> dailyAverages = new HashMap<>();
        dailyFlows.forEach((date, totalFlows) -> dailyAverages.put(date, totalFlows / (double) dailyCounts.get(date)));
        return dailyAverages;
    }

    public void saveReportToFile() {
                fileHandler.writeToFile(generateUniqueReportName(), generateReport());
    }

    private String generateUniqueReportName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return "data/report_" + now.format(formatter) + ".txt";
    }

    private Map<Integer, Integer> getUserActivity() {
        Map<Integer, Integer> userActivity = new HashMap<>();
        for (DataModel entry : data) {
            userActivity.merge(entry.getLocalIP(), entry.getFlows(), Integer::sum);
        }
        return userActivity;
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
