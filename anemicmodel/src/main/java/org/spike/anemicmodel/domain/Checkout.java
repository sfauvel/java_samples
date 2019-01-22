package org.spike.anemicmodel.domain;

import org.spike.anemicmodel.domain.Order;
import org.spike.anemicmodel.domain.Shipment;

public class Checkout {

    private Shipment shipment;
    private Order order;

    public Checkout(Order order, Shipment shipment) {
        this.order = order;
        this.shipment = shipment;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
