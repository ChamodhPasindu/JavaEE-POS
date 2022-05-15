package entity;

public class Customer {
    private String custId;
    private String custName;
    private double CustSalary;
    private String address;

    public Customer(String custId, String custName, double custSalary, String address) {
        this.custId = custId;
        this.custName = custName;
        CustSalary = custSalary;
        this.address = address;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public double getCustSalary() {
        return CustSalary;
    }

    public void setCustSalary(double custSalary) {
        CustSalary = custSalary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
