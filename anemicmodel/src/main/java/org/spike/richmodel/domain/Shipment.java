package org.spike.richmodel.domain;

import java.time.LocalDate;
import java.util.List;

public class Shipment {
    private Stock stock;
    private Factory factory;
    private Warehouse warehouse;
    private List<Product> products;
    private LocalDate deliveryDate;

    public Shipment(Stock stock, Factory factory) {
        this.stock = stock;
        this.factory = factory;
    }

    /**
     * Fields are private and this method is the only way to initiate them.
     * It's imposssible to have an incoherent Shipment.
     * When shipement is created, deliveryDate and warehouse are set and caller have no way to forget it.
     * @param products
     */
    public void create(List<Product> products) {
        this.products = products;
        deliveryDate = LocalDate.now().plusDays(2);
        warehouse =  factory.getWarehouse("DHL");

        stock.decrease(products);
    }

}
