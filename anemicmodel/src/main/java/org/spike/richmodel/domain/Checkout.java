package org.spike.richmodel.domain;

public class Checkout {

    private Shipment shipment;
    private Order order;

    public Checkout(Order order, Shipment shipment) {
        this.order = order;
        this.shipment = shipment;
    }

    public void complete(Order order) {
        order.validate();
        shipment.create(order.getProducts());
    }
}
