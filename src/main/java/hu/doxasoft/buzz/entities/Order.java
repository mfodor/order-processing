package hu.doxasoft.buzz.entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id")
    private int orderId;

    @NotNull
    @Column(name = "buyer_name")
    private String buyerName;

    @Email
    @NotNull
    @Column(name = "buyer_email")
    private String buyerEmail;

    @NotNull
    @Column(name = "order_date")
    private String orderDate;

    @Column(name = "order_total_value")
    private double orderTotalValue;

    @NotNull
    @Column(name = "address")
    private String address;

    @Column(name = "postcode")
    private int postcode;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
    }

    public Order(
            int orderId,
            @NotNull String buyerName,
            @Email @NotNull String buyerEmail,
            String orderDate,
            @NotNull String address,
            int postcode
    ) {
        this.orderId = orderId;
        this.buyerName = buyerName;
        this.buyerEmail = buyerEmail;
        setOrderDate(orderDate);
        this.address = address;
        this.postcode = postcode;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(@NotNull String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(@NotNull @Email String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        if (orderDate == null || orderDate.equals("")) {
            orderDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        }
        this.orderDate = orderDate;
    }

    public double getOrderTotalValue() {
        return orderTotalValue;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(@NotNull String address) {
        this.address = address;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(@NotNull List<OrderItem> items) {
        this.items = items;
        updateOrderTotalValue();
    }

    public void addItem(OrderItem item) {
        this.addItem(item, false);
    }

    public void addItem(OrderItem item, boolean skipBidirectionalCall) {
        if (items.contains(item)) {
            items.set(items.indexOf(item), item);
        } else {
            this.items.add(item);
        }
        if (!skipBidirectionalCall) {
            item.setOrder(this, true);
        }
        updateOrderTotalValue();
    }

    public void updateOrderTotalValue() {
        double sum = 0.0;
        for (OrderItem item : items) {
            sum += item.getTotalItemPrice();
        }
        this.orderTotalValue = Math.round(sum * 100) / 100.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getOrderId() == order.getOrderId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId());
    }
}
