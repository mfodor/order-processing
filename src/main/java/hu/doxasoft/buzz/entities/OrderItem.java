package hu.doxasoft.buzz.entities;

import hu.doxasoft.buzz.enums.OrderItemStatus;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @Column(name = "id")
    private int orderItemId;

    @ManyToOne
    private Order order;

    @Column(name = "sale_price")
    @Min(value = 1, message = "The sale price should be at least 1.0!")
    private double salePrice;

    @Column(name = "shipping_price")
    private double shippingPrice;

    @Column(name = "total_price")
    private double totalItemPrice;

    @NotNull
    @Column(name = "sku")
    private String SKU;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;

    public OrderItem() {
    }

    public OrderItem(
            int orderItemId,
            double salePrice,
            double shippingPrice,
            @NotNull String SKU,
            @NotNull OrderItemStatus status
    ) throws Exception {
        this.orderItemId = orderItemId;
        setSalePrice(salePrice);
        this.shippingPrice = shippingPrice;
        this.SKU = SKU;
        this.status = status;
        updateTotalItemPrice();
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.setOrder(order, false);
    }

    public void setOrder(Order order, boolean skipBidirectionalCall) {
        this.order = order;
        if (order != null && !skipBidirectionalCall) {
            order.addItem(this, true);
        }
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) throws Exception {
        if (salePrice < 1.0) {
            throw new Exception("The sale price should be at least 1.0!");
        }
        this.salePrice = salePrice;
        updateTotalItemPrice();
    }

    public double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(double shippingPrice) {
        this.shippingPrice = shippingPrice;
        updateTotalItemPrice();
    }

    public double getTotalItemPrice() {
        return totalItemPrice;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(@NotNull String SKU) {
        this.SKU = SKU;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull OrderItemStatus status) {
        this.status = status;
    }

    private void updateTotalItemPrice() {
        this.totalItemPrice = salePrice + shippingPrice;
        if (order != null) {
            order.updateOrderTotalValue();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;
        OrderItem orderItem = (OrderItem) o;
        return getOrderItemId() == orderItem.getOrderItemId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderItemId());
    }
}
