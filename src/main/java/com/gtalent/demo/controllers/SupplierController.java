package com.gtalent.demo.controllers;

import com.gtalent.demo.model.Supplier;
import com.gtalent.demo.responses.*;
import com.gtalent.demo.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return ResponseEntity.ok(suppliers.stream().map(supplier -> {
            SupplierResponse supplierResponse = new SupplierResponse(supplier);
            List<ProductResponse> products = supplier.getProducts().stream().
                    map(ProductResponse::new).toList();
            supplierResponse.setListOfProducts(products);
            return supplierResponse;
        }).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getSuppliersById(@PathVariable int id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isPresent()) {
            SupplierResponse response = new SupplierResponse(supplier.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> UpdateSupplierById(@PathVariable int id, @RequestBody UpdateSupplierRequest request) {
        //解題思路: 1. 先找到user 2. 拿出user 3. set user(UpdateUser裡只允set username)
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isPresent()) {
            Supplier updateSupplier = supplier.get();
            System.out.println("Before Update:" + updateSupplier);

            updateSupplier.setName(request.getName());
            updateSupplier.setPhone(request.getPhone());
            updateSupplier.setEmail(request.getEmail());
            System.out.println("Before Save:" + updateSupplier);
            updateSupplier = supplierRepository.save(updateSupplier);
            SupplierResponse response = new SupplierResponse(updateSupplier);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(@RequestBody CreateSupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        System.out.println("Before Save:" + supplier);
        Supplier savedSupplier = supplierRepository.save(supplier);
        SupplierResponse response = new SupplierResponse(savedSupplier);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierById(@PathVariable int id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isPresent()) {
            supplierRepository.delete(supplier.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}