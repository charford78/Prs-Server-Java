package com.maxtrain.prscapstone.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxtrain.prscapstone.user.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestsController {
	
	@Autowired
	private RequestRepository reqRepo;
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Request>> GetAll(){
		var requests = reqRepo.findAll();
		return new ResponseEntity<Iterable<Request>>(requests, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Request> GetbyId(@PathVariable int id){
		
		var request = reqRepo.findById(id);
		if(request.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Request>(request.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Request> Insert(@RequestBody Request request){
		
		if(request == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if(request.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		var newRequest = reqRepo.save(request);
		return new ResponseEntity<Request>(newRequest, HttpStatus.CREATED);		
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity Update(@PathVariable int id, @RequestBody Request request) {
		
		if(request == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if(request.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var oldRequest = reqRepo.findById(request.getId());
		if(oldRequest.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		reqRepo.save(request);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Request> Delete(@PathVariable int id) {
		
		var request = reqRepo.findById(id);
		if(request.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		reqRepo.deleteById(id);
		return new ResponseEntity<Request>(request.get(), HttpStatus.OK);
	}
	
	@GetMapping("user/{userId}")
	public ResponseEntity<Iterable<Request>> GetRequestsInReview(@PathVariable int userId){
		
		var user = userRepo.findById(userId);
		if(user.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		var requests = reqRepo.findByStatusAndUserIdNot("REVIEW", userId);
		return new ResponseEntity<Iterable<Request>>(requests, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("review/{id}")
	public ResponseEntity SetStatusToReview(@PathVariable int id, @RequestBody Request request) {
		
		if(request == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if(request.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var oldRequest = reqRepo.findById(request.getId());
		if(oldRequest.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var newStatus = (request.getTotal() <= 50) ? "APPROVED" : "REVIEW";
		request.setStatus(newStatus);
		return Update(id, request);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("approve/{id}")
	public ResponseEntity SetStatusToApproved(@PathVariable int id, @RequestBody Request request) {
		if(request.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var oldRequest = reqRepo.findById(request.getId());
		if(oldRequest.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		request.setStatus("APPROVED");
		return Update(id, request);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("reject/{id}")
	public ResponseEntity SetStatusToRejected(@PathVariable int id, @RequestBody Request request) {
		if(request.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var oldRequest = reqRepo.findById(request.getId());
		if(oldRequest.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		request.setStatus("REJECTED");
		return Update(id, request);
	}
}
