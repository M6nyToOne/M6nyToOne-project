package sparta.m6nytooneproject.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.m6nytooneproject.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productrepository;
}
