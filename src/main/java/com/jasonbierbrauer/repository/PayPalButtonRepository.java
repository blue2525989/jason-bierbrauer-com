package com.jasonbierbrauer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jasonbierbrauer.model.PayPalButton;


public interface PayPalButtonRepository extends JpaRepository<PayPalButton, Long> {
	
	List<PayPalButton> findById(Long count);
}
