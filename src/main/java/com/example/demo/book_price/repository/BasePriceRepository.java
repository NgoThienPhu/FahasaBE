package com.example.demo.book_price.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.book_price.entity.BasePrice;

@Repository
public interface BasePriceRepository extends JpaRepository<BasePrice, String> {

	@Query("""
				SELECT bp FROM BasePrice bp
				WHERE bp.book.id = :bookId
				  AND (:fromDate IS NULL OR bp.effectiveFrom >= :fromDate)
				  AND (:toDate IS NULL OR bp.effectiveFrom <= :toDate)
				ORDER BY bp.effectiveFrom DESC
			""")
	List<BasePrice> findByBookId(String bookId, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);

	@Query("""
				SELECT bp FROM BasePrice bp
				WHERE bp.book.id = :bookId
				  AND (bp.effectiveTo IS NULL OR bp.effectiveTo >= :currentDate)
				ORDER BY bp.effectiveFrom DESC
			""")
	Optional<BasePrice> findCurrentPrice(String bookId, LocalDateTime currentDate);

	@Query("""
				SELECT CASE WHEN COUNT(bp) > 0 THEN true ELSE false END
				FROM BasePrice bp
				WHERE bp.book.id = :bookId
				  AND (
						(bp.effectiveFrom <= :effectiveTo AND (bp.effectiveTo IS NULL OR bp.effectiveTo >= :effectiveFrom))
					)
			""")
	boolean existsOverlappingActivePrice(String bookId, LocalDateTime effectiveFrom, LocalDateTime effectiveTo);

}
