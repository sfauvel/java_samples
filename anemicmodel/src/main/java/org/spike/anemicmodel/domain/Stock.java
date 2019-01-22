package org.spike.anemicmodel.domain;

import org.spike.anemicmodel.domain.Product;

import java.util.List;

public class Stock {
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
