package com.jasonbierbrauer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jasonbierbrauer.model.GalleryCardContent;


public interface GalleryCardContentRepository extends JpaRepository<GalleryCardContent, Long> {
	
	List<GalleryCardContent> findById(Long count);

}
