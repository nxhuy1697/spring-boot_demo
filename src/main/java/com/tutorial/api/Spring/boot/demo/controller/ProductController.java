package com.tutorial.api.Spring.boot.demo.controller;

import com.tutorial.api.Spring.boot.demo.models.Product;
import com.tutorial.api.Spring.boot.demo.models.ResposeObject;
import com.tutorial.api.Spring.boot.demo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/Products")
public class ProductController {
    //DI = Dependency Injection
    @Autowired
    private ProductRepository repository;

    //this request is: http://localhost:8080/api/v1/Products
    @GetMapping("")
    List<Product> getAllProducts(){
        return repository.findAll();

    }
    //U must have this to Database, now we have H2 DB = In-memory Database
    //U can also send request using Postman

    //Get product detail
    @GetMapping("/{id}")
    //let's return an obj with : data, message, status
    ResponseEntity <ResposeObject> findById(@PathVariable Long id) {
        Optional<Product> foundProduct = repository.findById(id);
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResposeObject("ok", "Query product successfully", foundProduct)):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResposeObject("failed","Can not find product with id " + id, ""));
    }
    //Insert Product with Post method
    //Postman: Raw, JSON
    @PostMapping("/insert")
    ResponseEntity<ResposeObject> insertProduct(@RequestBody Product newProduct){
        //2 product must not have the same name!
        List <Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        if(foundProducts.size() > 0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResposeObject("Failed", "This name is already taken","")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResposeObject("ok", "Insert product successfully", repository.save(newProduct))
        );
    }
    //update, upsert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResposeObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id){
        Product updateProduct = repository.findById(id)
                .map(product -> {
                    product.setProductName(newProduct.getProductName());
                    product.setYears(newProduct.getYears());
                    product.setPrice(newProduct.getPrice());
                    product.setUrl(newProduct.getUrl());
                    return repository.save(product);
                }). orElseGet(()-> {
                    newProduct.setId(id);
                    return repository.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResposeObject("ok", "Updated product successfully", updateProduct)
        );
    }
    // Delete a product = > DELETE Method
    @DeleteMapping("/{id}")
    ResponseEntity<ResposeObject> deleteProduct(@PathVariable Long id) {
        boolean exsists = repository.existsById(id);
        if (exsists){
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResposeObject("ok", "Delete product successfully", "")
            );
        };
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResposeObject("Failed", "Can not find the product to delete", "")
        );

    }
}