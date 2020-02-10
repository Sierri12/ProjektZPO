public class Paramater {
    private String name;
    private String date;
    private String index;
    private int quantity;
    private double avg;
    private String unit;
    private double min;
    private double max;
    private double std;

    public Paramater(String name, String date, String index, int quantity, double avg, double std, String unit, double min, double max) {
        this.name = name;
        this.date = date;
        this.index = index;
        this.quantity = quantity;
        this.avg = avg;
        this.unit = unit;
        this.min = min;
        this.max = max;
        this.std = std;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getIndex() {
        return index;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAvg() {
        return avg;
    }

    public String getUnit() {
        return unit;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStd() {
        return std;
    }
}
