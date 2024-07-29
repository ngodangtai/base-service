package com.company.module.controller.external;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actuator")
public class MyHealthController {

	@RequestMapping(value = "/health", method = RequestMethod.GET)
	public ResponseEntity<?> health() {
		return ResponseEntity.ok(true);
	}
}
