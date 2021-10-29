package com.maxtrain.prscapstone.requestline;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLineRepository extends JpaRepository<RequestLine, Integer>{
	
	List<RequestLine> findRequestLineByRequestId(int requestId);
}
