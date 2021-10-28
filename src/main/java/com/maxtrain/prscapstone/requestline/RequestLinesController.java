package com.maxtrain.prscapstone.requestline;

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

import com.maxtrain.prscapstone.request.RequestRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/requestlines")
public class RequestLinesController {

	@Autowired
	private RequestLineRepository reqlineRepo;
	@Autowired
	private RequestRepository reqRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<RequestLine>> GetAll(){
		var requestlines = reqlineRepo.findAll();
		return new ResponseEntity<Iterable<RequestLine>>(requestlines, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<RequestLine> GetbyId(@PathVariable int id){
		var requestline = reqlineRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RequestLine> (requestline.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<RequestLine> Insert(@RequestBody RequestLine requestline) throws Exception{
		
		if(requestline == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if(requestline.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var newRequestline = reqlineRepo.save(requestline);
		RecalculateRequestTotal(requestline.getRequest().getId());
		return new ResponseEntity<RequestLine>(newRequestline, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity Update(@PathVariable int id, @RequestBody RequestLine requestline) throws Exception {
		
		if(requestline.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var oldRequestline = reqlineRepo.findById(requestline.getId());
		if(oldRequestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		reqlineRepo.save(requestline);
		RecalculateRequestTotal(requestline.getRequest().getId());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<RequestLine> Delete(@PathVariable int id) throws Exception {
		
		var requestline = reqlineRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		reqlineRepo.deleteById(id);
		RecalculateRequestTotal(requestline.get().getRequest().getId());
		return new ResponseEntity<RequestLine>(requestline.get(), HttpStatus.OK);
	}
	
	private void RecalculateRequestTotal(int requestId) throws Exception {
		
		var optRequest = reqRepo.findById(requestId);
		if(optRequest.isEmpty()) {
			throw new Exception("Request Id is invalid!");
		}
		var request = optRequest.get();
		var requestLines = reqlineRepo.findRequestlineByRequestId(requestId);
		var total = 0;
		for(var reqLine : requestLines) {
			total += reqLine.getQuantity() * reqLine.getProduct().getPrice();
		}
		request.setTotal(total);
		reqRepo.save(request);
	}
}
