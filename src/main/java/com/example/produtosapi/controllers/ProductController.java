package com.example.produtosapi.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.produtosapi.exceptions.EntityNotFoundException;
import com.example.produtosapi.services.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.produtosapi.dtos.ProductRecordDto;
import com.example.produtosapi.models.ProductModel;
import com.example.produtosapi.repositories.ProductRepository;

import jakarta.validation.Valid;

@RestController()
public class ProductController {

  @Autowired
  ProductService service;

  @GetMapping("/products")
  public ResponseEntity<List<ProductModel>> listProducts() {
    List<ProductModel> productsList = service.list();
    if(!productsList.isEmpty()){
      for(ProductModel product : productsList) {
        UUID id = product.getIdProduct();
        product.add(linkTo(methodOn(ProductController.class).findOneProduct(id)).withSelfRel());
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body(productsList);
  }

  @GetMapping("/products/{id}")
  public ResponseEntity<Object> findOneProduct(@PathVariable(value = "id") UUID id) {

    ProductModel product;
    try {
      product = service.findById(id);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    product.add(linkTo(methodOn(ProductController.class).listProducts()).withRel("Products List"));
    return ResponseEntity.status(HttpStatus.OK).body(product);
  }

  @PostMapping("/products")
  public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(productRecordDto));
  }

  @PutMapping("/products/{id}")
  public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
      @RequestBody @Valid ProductRecordDto productRecordDto) {
    ProductModel productModel;
    try {
      productModel = service.findById(id);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    return ResponseEntity.status(HttpStatus.OK).body(service.update(productRecordDto, productModel));
  }

  @DeleteMapping("/products/{id}")
  public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
    ProductModel productModel;
    try {
      productModel = service.findById(id);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    service.delete(productModel);

    return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
  }
}
