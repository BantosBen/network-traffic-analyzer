public class DataModel {
    private String date;
    private int localIP;
    private int remoteASN;
    private int flows;

    public DataModel(String date, int localIP, int remoteASN, int flows) {
        this.date = date;
        this.localIP = localIP;
        this.remoteASN = remoteASN;
        this.flows = flows;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLocalIP() {
        return localIP;
    }

    public void setLocalIP(int localIP) {
        this.localIP = localIP;
    }

    public int getRemoteASN() {
        return remoteASN;
    }

    public void setRemoteASN(int remoteASN) {
        this.remoteASN = remoteASN;
    }

    public int getFlows() {
        return flows;
    }

    public void setFlows(int flows) {
        this.flows = flows;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "date='" + date + '\'' +
                ", localIP=" + localIP +
                ", remoteASN=" + remoteASN +
                ", flows=" + flows +
                '}';
    }
}

