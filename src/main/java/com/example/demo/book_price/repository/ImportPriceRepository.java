package com.example.demo.book_price.repository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.book_price.entity.ImportPrice;

@Repository
public interface ImportPriceRepository extends JpaRepository<ImportPrice, String> {

	@Query("""
			SELECT p FROM ImportPrice p
			WHERE p.book.id = :bookId
			AND p.effectiveFrom <= CURRENT_TIMESTAMP
			AND (p.effectiveTo IS NULL OR p.effectiveTo >= CURRENT_TIMESTAMP)
			ORDER BY p.effectiveFrom ASC
			""")
	List<ImportPrice> findCurrentPrice(@Param("bookId") String bookId, Pageable pageable);
	
	@Query("""
			SELECT COUNT(p) > 0 FROM ImportPrice p
			WHERE p.book.id = :bookId
			AND p.effectiveFrom >= :from
			""")
	boolean isOverlap(@Param("bookId") String bookId, @Param("from") LocalDateTime from);
	
	@Query("""
			SELECT p FROM ImportPrice p
			WHERE p.book.id = :bookId
			AND p.effectiveFrom < :from
			ORDER BY p.effectiveFrom DESC
			""")
	ImportPrice findPreviousPrice(@Param("bookId") String bookId, @Param("from") LocalDateTime from);
	
	@Query("""
			SELECT p FROM ImportPrice p
			WHERE p.book.id = :bookId
			AND p.effectiveFrom > :from
			ORDER BY p.effectiveFrom DESC
			""")
	ImportPrice findNextPrice(@Param("bookId") String bookId, @Param("from") LocalDateTime from);


}
