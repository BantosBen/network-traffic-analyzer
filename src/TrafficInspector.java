import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrafficInspector {

    private final List<DataModel> data;

    public TrafficInspector(List<DataModel> data) {
        this.data = data;
    }

    public Map<Integer, List<DataModel>> analyzeTrafficByUser() {
        Map<Integer, List<DataModel>> userTraffic = new HashMap<>();
        for (DataModel entry : data) {
            userTraffic.computeIfAbsent(entry.getLocalIP(), k -> new ArrayList<>()).add(entry);
        }
        return userTraffic;
    }

    public List<DataModel> detectAnomalies() {
        List<DataModel> anomalies = new ArrayList<>();
        Map<Integer, Integer> averageFlows = calculateAverageFlows();

        for (DataModel entry : data) {
            int avg = averageFlows.getOrDefault(entry.getLocalIP(), 0);
            if (entry.getFlows() > avg * 2.5) { // Example condition for anomaly
                if (anomalies.size() == 5) {
                    break;
                }
                anomalies.add(entry);
            }
        }

        return anomalies;
    }

    private Map<Integer, Integer> calculateAverageFlows() {
        Map<Integer, Integer> totalFlows = new HashMap<>();
        Map<Integer, Integer> flowCounts = new HashMap<>();

        for (DataModel entry : data) {
            totalFlows.merge(entry.getLocalIP(), entry.getFlows(), Integer::sum);
            flowCounts.merge(entry.getLocalIP(), 1, Integer::sum);
        }

        Map<Integer, Integer> averages = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : totalFlows.entrySet()) {
            averages.put(entry.getKey(), entry.getValue() / flowCounts.get(entry.getKey()));
        }

        return averages;
    }

    // Other analysis methods can be added here
}

