package com.example.demo.book_price.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.book_price.entity.PromotionPrice;

@Repository
public interface PromotionPriceRepository extends JpaRepository<PromotionPrice, String> {
	
	@Query("""
			SELECT p FROM PromotionPrice p
			WHERE p.effectiveFrom <= NOW()
			AND (p.effectiveTo IS NULL OR p.effectiveTo >= NOW())
			""")
	Optional<PromotionPrice> getCurrentPromotionPrice(@Param("productId") String productId);
	
	@Query("""
			SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
			FROM PromotionPrice p
			WHERE (p.effectiveFrom <= :to AND p.effectiveTo >= :from)
			OR (p.effectiveTo IS NULL AND p.effectiveFrom <= :to)
			""")
	boolean existsTimeOverlap(@Param("productId") String productId ,@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

}
