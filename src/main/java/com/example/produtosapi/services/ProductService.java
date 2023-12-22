package com.example.produtosapi.services;

import com.example.produtosapi.dtos.ProductRecordDto;
import com.example.produtosapi.exceptions.EntityNotFoundException;
import com.example.produtosapi.models.ProductModel;
import com.example.produtosapi.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    public List<ProductModel> list(){
        return repository.findAll();
    }

    public ProductModel findById(UUID id) throws EntityNotFoundException {
        Optional<ProductModel> productO = repository.findById(id);
        if(productO.isEmpty()){
            throw new EntityNotFoundException("Product");
        }
        return productO.get();
    }

    public ProductModel create(ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return repository.save(productModel);
    }

    public ProductModel save(ProductModel productModel) {
        return repository.save(productModel);
    }

    public ProductModel update(ProductRecordDto productRecordDto, ProductModel productModel) {
        BeanUtils.copyProperties(productRecordDto, productModel);
        return this.save(productModel);
    }

    public void delete(ProductModel productModel) {
        repository.delete(productModel);
    }
}
