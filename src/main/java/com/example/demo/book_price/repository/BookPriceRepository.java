package com.example.demo.book_price.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.book_price.entity.base.BookPrice;

@Repository
public interface BookPriceRepository extends JpaRepository<BookPrice, String> {

	@Query(value = """
			    SELECT bp
				FROM BookPrice bp
				WHERE bp.book.id = :bookId
				  AND bp.effectiveFrom <= :currentDate
				  AND (bp.effectiveTo IS NULL OR bp.effectiveTo > :currentDate)
				ORDER BY
				  CASE TYPE(bp)
				    WHEN BasePrice THEN 3
				  END
			    LIMIT 1
			""")
	Optional<BookPrice> findCurrentPrice(String bookId, LocalDateTime currentDate);

	@Query("""
				SELECT bp
				FROM BookPrice bp
				WHERE bp.book.id IN :bookIds
				  AND bp.effectiveFrom <= :currentDate
				  AND (bp.effectiveTo IS NULL OR bp.effectiveTo > :currentDate)
				ORDER BY
				  CASE TYPE(bp)
				    WHEN BasePrice THEN 3
				  END
			""")
	List<BookPrice> findCurrentPrices(@Param("bookIds") List<String> bookIds, LocalDateTime currentDate);

}
