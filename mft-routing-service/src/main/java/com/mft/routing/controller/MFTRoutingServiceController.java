/**
 * 
 */
package com.mft.routing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mft.routing.dto.SFTPGo_Upload_DTO;
import com.mft.routing.request.model.SFTPGoTokenRequest;
import com.mft.routing.response.SFTPGoTokenResponse;
import com.mft.routing.service.IMFTRoutingService;

/**
 * 
 */
@RestController
@RequestMapping("/mft/routing")
public class MFTRoutingServiceController {
	Logger logger = LoggerFactory.getLogger(MFTRoutingServiceController.class);

	@Autowired
	IMFTRoutingService imft_Routing_Service;

	@GetMapping("/greet")
	public @ResponseBody String welcome() {
		logger.info("Inside MFTRoutingServiceController >> welcome");
		return "welcome";
	}
	
	@PostMapping("/token")
	public @ResponseBody SFTPGoTokenResponse getToken(@RequestBody(required=true) SFTPGoTokenRequest tokenRequest) {
		logger.info("Inside MFTRoutingServiceController >> getToken");
		
		SFTPGoTokenResponse response = imft_Routing_Service.getTokenDetails(tokenRequest);
		return null;
	}

	@PostMapping("/uploadFiles")
	public @ResponseBody SFTPGo_Upload_DTO uploadFiles(@RequestParam("path") String path,
			@RequestParam("mkdir_parents") boolean mkdir_parents, @RequestBody MultipartFile filenames) {
		logger.info("Inside MFTRoutingServiceController >> uploadFiles");
		imft_Routing_Service.uploadFiles(path, mkdir_parents, filenames);
		return null;
	}
}
