package org.spike.anemicmodel.service;

import org.spike.anemicmodel.domain.Order;
import org.spike.anemicmodel.domain.Shipment;

import java.time.LocalDate;

public class CheckoutService {

    private OrderService orderService;

    public CheckoutService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void complete(Order order) {
        orderService.validateOrder(order);

        Shipment shipment = new Shipment();
        shipment.setProducts(order.getProducts());

        shipment.setDeliveryDate(LocalDate.now().plusDays(2));

        //stock.decrease(products);
    }
}
