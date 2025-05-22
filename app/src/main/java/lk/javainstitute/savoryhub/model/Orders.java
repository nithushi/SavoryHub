package lk.javainstitute.savoryhub.model;

public class Orders {

    private int OrderId;
    private double ItemTotal;
    private double Delivery;
    private double Total;
    private String User;
    private String Date;
    private int Status_Id;

    public Orders(int orderId, double itemTotal, double delivery, double total, String user, String date, int status_Id) {
        OrderId = orderId;
        ItemTotal = itemTotal;
        Delivery = delivery;
        Total = total;
        User = user;
        Date = date;
        Status_Id = status_Id;
    }
//    public Orders(int orderId, double itemTotal, double delivery, double total, String user, String date) {
//        OrderId = orderId;
//        ItemTotal = itemTotal;
//        Delivery = delivery;
//        Total = total;
//        User = user;
//        Date = date;
//    }

    public Orders() {
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public double getItemTotal() {
        return ItemTotal;
    }

    public void setItemTotal(double itemTotal) {
        ItemTotal = itemTotal;
    }

    public double getDelivery() {
        return Delivery;
    }

    public void setDelivery(double delivery) {
        Delivery = delivery;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getStatus_Id() {
        return Status_Id;
    }

    public void setStatus_Id(int status_Id) {
        Status_Id = status_Id;
    }


}
