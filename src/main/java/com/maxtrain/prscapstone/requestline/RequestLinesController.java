package com.maxtrain.prscapstone.requestline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/requestlines")
public class RequestLinesController {

	@Autowired
	private RequestLineRepository reqlineRepo;
}
