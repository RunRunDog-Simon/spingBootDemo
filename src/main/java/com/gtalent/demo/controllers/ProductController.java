package com.gtalent.demo.controllers;

import com.gtalent.demo.model.Product;
import com.gtalent.demo.model.Supplier;
import com.gtalent.demo.responses.*;
import com.gtalent.demo.repositories.ProductRepository;
import com.gtalent.demo.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
public class ProductController {
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productRepository.findAll();
//        return ResponseEntity.ok(products);
//        return ResponseEntity.ok(products.stream().map(ProductResponse::new).toList());  原本的
        return ResponseEntity.ok(products.stream().map(product->{
            // 1. Product -> ProductResponse
            ProductResponse response = new ProductResponse(product);
            // 2. 把supplier 填充至 SupplierResponse
            response.setSupplierResponse(new SupplierResponse(product.getSupplier()));
            return response;
        }).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductsById(@PathVariable int id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
//            ProductResponse response = new GetUserResponse(product.get());
            ProductResponse response = new ProductResponse(product.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> UpdateProductById(@PathVariable int id, @RequestBody UpdateProductRequest request) {
        //解題思路: 1. 先找到user 2. 拿出user 3. set user(UpdateUser裡只允set username)
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product updateProduct = product.get();
            System.out.println("Before Update:" + updateProduct);

            updateProduct.setName(request.getName());
            updateProduct.setPrice(request.getPrice());
            updateProduct.setQuantity(request.getQuantity());
            System.out.println("Before Save:" + updateProduct);
            updateProduct = productRepository.save(updateProduct);
            ProductResponse response = new ProductResponse(updateProduct);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        Optional<Supplier> supplier = supplierRepository.findById(request.getSupplierId());
        if (supplier.isPresent()) {
            Product product = new Product();
            product.setName(request.getName());
            product.setPrice(request.getPrice());
            product.setQuantity(request.getQuantity());
            product.setStatus(request.isStatus());
            product.setSupplier(supplier.get());
            Product saveProduct = productRepository.save(product);
            ProductResponse response = new ProductResponse(saveProduct);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductsById(@PathVariable int id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
