package com.jasonbierbrauer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jasonbierbrauer.model.Fragment;

public interface FragmentRepository extends JpaRepository<Fragment, Long> {
	
	List<Fragment> findById(Long count);

}
