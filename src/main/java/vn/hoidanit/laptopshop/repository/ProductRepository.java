package vn.hoidanit.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.laptopshop.domain.Product;

@Repository
public interface  ProductRepository extends JpaRepository<Product, Long>{

    // Product save(Product product);

    // List<Product> findAll();

    // void deleteById(long id);
    
    // Product findById(long id);
}
