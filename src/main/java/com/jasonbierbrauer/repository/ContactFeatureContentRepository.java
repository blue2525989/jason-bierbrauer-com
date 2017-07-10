package com.jasonbierbrauer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jasonbierbrauer.model.ContactFeatureContent;

public interface ContactFeatureContentRepository extends JpaRepository<ContactFeatureContent, Long> {
	
	List<ContactFeatureContent> findById(Long count);

}
