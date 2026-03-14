package com.example.demo.book_image.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.book_image.entity.BookImage;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, String> {
	
	@Query("""
			SELECT bi FROM BookImage bi
			WHERE bi.book.id = :bookId AND bi.isPrimary = true
			""")
	public Optional<BookImage> findBookPrimaryImage(String bookId);
	
	@Query("""
			SELECT bi FROM BookImage bi
			WHERE bi.book.id IN :bookIds AND bi.isPrimary = true
			""")
	public List<BookImage> findBookPrimaryImages(List<String> bookIds);
	
	@Query("""
			SELECT bi FROM BookImage bi
			WHERE bi.book.id = :bookId AND bi.isPrimary = false
			""")
	public List<BookImage> findBookSecondaryImage(String bookId);

}
