package dto;

import java.util.ArrayList;

public class OrderDTO {
    private ArrayList<OrderDetailDTO> orderDetail;
    private String orderId;
    private String custId;
    private String date;
    private double cost;
    private int discount;

    public OrderDTO(ArrayList<OrderDetailDTO> orderDetail, String orderId, String custId, String date, double cost, int discount) {
        this.orderDetail = orderDetail;
        this.orderId = orderId;
        this.custId = custId;
        this.date = date;
        this.cost = cost;
        this.discount = discount;
    }

    public ArrayList<OrderDetailDTO> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(ArrayList<OrderDetailDTO> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
