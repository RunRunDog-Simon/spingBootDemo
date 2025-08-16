package com.gtalent.demo.responses;

import com.gtalent.demo.model.Product;

public class CreateProductRequest {

    private String name;
    private double price;
    private int quantity;
    private boolean status;
    private int supplierId;

    public CreateProductRequest() {
    }

    public CreateProductRequest(String name, double price, int quantity, boolean status, int supplierId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.supplierId = supplierId;
    }

    public CreateProductRequest(Product product) {
    }
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
