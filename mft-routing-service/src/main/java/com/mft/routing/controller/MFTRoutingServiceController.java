/**
 * 
 */
package com.mft.routing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mft.enums.MFTRoutingServiceEnum;
import com.mft.routing.dto.SFTPGoUploadDTO;
import com.mft.routing.request.model.SFTPGoTokenRequest;
import com.mft.routing.response.SFTPGoTokenResponse;
import com.mft.routing.service.IMFTRoutingService;
import com.mft.utility.ToolAPIDetail;

/**
 * 
 */
@RestController
@RequestMapping("/mft/routing")
public class MFTRoutingServiceController {
	Logger logger = LoggerFactory.getLogger(MFTRoutingServiceController.class);

	@Autowired
	IMFTRoutingService imftRoutingService;

	@Autowired
	ToolAPIDetail toolAPIDetail;

	@Autowired
	private Environment env;

	@GetMapping("/greet")
	public @ResponseBody String welcome() {
		logger.info("Inside MFTRoutingServiceController >> welcome");
		logger.info(env.getProperty("user1"));
		return "welcome";
	}

	@PostMapping("/token")
	public @ResponseBody SFTPGoTokenResponse getToken(@RequestBody(required = true) SFTPGoTokenRequest tokenRequest) {
		logger.info("Inside MFTRoutingServiceController >> getToken");

		SFTPGoTokenResponse response = imftRoutingService.getTokenDetails(tokenRequest);
		return response;
	}

	@PostMapping("/uploadFile")
	public @ResponseBody SFTPGoUploadDTO uploadFiles(@RequestParam("userName") String userName,
			@RequestParam("path") String path, @RequestParam("mkdir_parents") boolean mkdir_parents,
			@RequestPart MultipartFile filenames) {
		logger.info("Inside MFTRoutingServiceController >> uploadFiles");

		String toolName = env.getProperty(userName);
		String uploadURL = toolAPIDetail.getToolAPI(toolName, MFTRoutingServiceEnum._upload.name());
		// String uploadAPI = env.getProperty(toolName + ".upload");
		logger.info("Upload url >>>>> " + env.getProperty(toolName + ".upload"));

		SFTPGoUploadDTO respone = imftRoutingService.uploadFiles(uploadURL, path, mkdir_parents, filenames);
		return null;
	}

	@PostMapping("/saveFile")
	public @ResponseBody SFTPGoUploadDTO saveFile(@RequestParam("userName") String userName,
			@RequestParam("path") String path, @RequestParam("mkdir_parents") boolean mkdir_parents,
			@RequestPart MultipartFile filenames) {
		logger.info("Inside MFTRoutingServiceController >> uploadFiles");

		String toolName = env.getProperty(userName);
		String uploadURL = toolAPIDetail.getToolAPI(toolName, MFTRoutingServiceEnum._upload.name());
		// String uploadAPI = env.getProperty(toolName + ".upload");
		logger.info("Upload url >>>>> " + env.getProperty(toolName + ".upload"));

		SFTPGoUploadDTO respone = imftRoutingService.saveFile(uploadURL, path, mkdir_parents, filenames);
		return null;
	}

	@PostMapping("/downloadFile")
	public @ResponseBody SFTPGoUploadDTO downloadFile(@RequestParam("userName") String userName,
			@RequestParam(required = false) String inline, @RequestParam("path") String path) {
		logger.info("Inside MFTRoutingServiceController >> downloadFile");

		String toolName = env.getProperty(userName);
		String downloadURL = env.getProperty(toolName + ".download");
		logger.info("Upload url >>>>> " + env.getProperty(toolName + ".download"));

		imftRoutingService.downloadFile(downloadURL, path, userName, inline);
		return null;
	}

}
