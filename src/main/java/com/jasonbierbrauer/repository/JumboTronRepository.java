package com.jasonbierbrauer.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.jasonbierbrauer.model.JumbotronContent;


public interface JumboTronRepository extends JpaRepository<JumbotronContent, Long> {
	
	JumbotronContent findById(Long id);
	
}
