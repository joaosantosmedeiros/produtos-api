package com.example.produtosapi.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
  ProductRepository productRepository;

  @GetMapping("/products")
  public ResponseEntity<List<ProductModel>> listProducts() {
    List<ProductModel> productsList = productRepository.findAll();
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
    Optional<ProductModel> productO = productRepository.findById(id);
    if (productO.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found");
    }
    var product = productO.get();
    product.add(linkTo(methodOn(ProductController.class).listProducts()).withRel("Products List"));
    return ResponseEntity.status(HttpStatus.OK).body(product);
  }

  @PostMapping("/products")
  public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
    var productModel = new ProductModel();
    BeanUtils.copyProperties(productRecordDto, productModel);
    return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
  }

  @PutMapping("/products/{id}")
  public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
      @RequestBody @Valid ProductRecordDto productRecordDto) {
    Optional<ProductModel> productO = productRepository.findById(id);
    if (productO.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found.");
    }

    var productModel = productO.get();
    BeanUtils.copyProperties(productRecordDto, productModel);

    return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
  }

  @DeleteMapping("/products/{id}")
  public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
    Optional<ProductModel> productO = productRepository.findById(id);
    if(productO.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found.");
    }

    productRepository.delete(productO.get());

    return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
  }
}
