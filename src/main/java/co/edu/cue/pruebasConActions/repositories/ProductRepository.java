package co.edu.cue.pruebasConActions.repositories;

import co.edu.cue.pruebasConActions.domain.entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Products, Long> {
}