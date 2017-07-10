package com.jasonbierbrauer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jasonbierbrauer.model.InventoryItem;


public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
	
	List<InventoryItem> findById(Long count);

	List<InventoryItem> findByType(String type);
}
