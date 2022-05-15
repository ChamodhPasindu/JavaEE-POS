package entity;

public class Order {
    private String orderId;
    private String date;
    private String custId;
    private double cost;
    private int discount;

    public Order(String orderId, String date, String custId, double cost, int discount) {
        this.orderId = orderId;
        this.date = date;
        this.custId = custId;
        this.cost = cost;
        this.discount = discount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
