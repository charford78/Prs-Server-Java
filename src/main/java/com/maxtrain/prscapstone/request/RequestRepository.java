package com.maxtrain.prscapstone.request;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Integer>{

	Optional<Request> findByUserId(int userId);
	List<Request> findByUserIdNot(int userId);
}
