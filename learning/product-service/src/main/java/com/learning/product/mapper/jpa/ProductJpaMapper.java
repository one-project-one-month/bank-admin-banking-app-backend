package com.learning.product.mapper.jpa;

import com.learning.product.dto.CreateProductRequest;
import com.learning.product.dto.UpdateProductRequest;
import com.learning.product.dto.ProductResponse;
import com.learning.product.model.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductJpaMapper {
    @Mapping(target = "id", ignore = true)
    Product toEntity(CreateProductRequest request);

    @Mapping(target = "id", ignore = true)
    Product toEntity(UpdateProductRequest request);

    ProductResponse toResponse(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(UpdateProductRequest request, @MappingTarget Product product);
}

