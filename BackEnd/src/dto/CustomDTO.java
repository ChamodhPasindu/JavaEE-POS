package dto;

public class CustomDTO {
    private String orderId;
    private String custName;
    private String date;
    private int discount;
    private double cost;

    public CustomDTO(String orderId, String custName, String date, int discount, double cost) {
        this.orderId = orderId;
        this.custName = custName;
        this.date = date;
        this.discount = discount;
        this.cost = cost;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
