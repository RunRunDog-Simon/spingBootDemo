package com.gtalent.demo.responses;

import com.gtalent.demo.model.Product;

public class ProductResponse {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private boolean status;
    private SupplierResponse supplierResponse; //後來增加

    public SupplierResponse getSupplierResponse() {
        return supplierResponse;
    }

    public void setSupplierResponse(SupplierResponse supplierResponse) {
        this.supplierResponse = supplierResponse;
    }

    public ProductResponse(){
    }
    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.status = product.getStatus();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
