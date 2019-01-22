package org.spike.anemicmodel.domain;

import java.time.LocalDate;
import java.util.List;

/**
 * Anemic object.
 */
public class Shipment {
    private List<Product> products;
    private LocalDate deliveryDate;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
