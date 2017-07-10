package com.jasonbierbrauer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jasonbierbrauer.model.AboutContent;

public interface AboutContentRepository extends JpaRepository<AboutContent, Long> {
	
	List<AboutContent> findById(Long count);

}
