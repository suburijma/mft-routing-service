/**
 * 
 */
package com.mft.routing.service.impl;

import java.nio.charset.Charset;
import java.util.Collections;

import javax.xml.ws.soap.MTOM;

import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.mft.routing.dao.MFTRoutingServiceDAO;
import com.mft.routing.dto.SFTPGo_Upload_DTO;
import com.mft.routing.request.model.SFTPGoTokenRequest;
import com.mft.routing.response.SFTPGoTokenResponse;
import com.mft.routing.service.IMFTRoutingService;

/**
 * 
 */
@Service
public class MFTRoutingServiceImpl implements IMFTRoutingService {
	Logger logger = LoggerFactory.getLogger(MFTRoutingServiceImpl.class);
	
	@Value( "${sftpgourl}" )
	private String sftpgourl;
	
	@Autowired
	MFTRoutingServiceDAO mftRoutingServiceDAO;

	@Override
	public SFTPGo_Upload_DTO uploadFiles(String url,
			String path, Boolean mk_dir, MultipartFile file) {
		logger.info("Inside MFT_Routing_ServiceImpl >> uploadFiles");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.setContentType(MediaType.APPLICATION_JSON);

		MultiValueMap<String, Object> filenames = new LinkedMultiValueMap<>();
		filenames.add("file", file);
		//TODO : SFTPGoTokenResponse getToken();

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(filenames, headers);
		String serverUrl = url+"path=test1.txt&mkdir_parents=false";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<SFTPGo_Upload_DTO> response = restTemplate.postForEntity(serverUrl, requestEntity, SFTPGo_Upload_DTO.class);

		logger.info("Checking the response"+response.getBody().toString());
		return response.getBody();
	}

	@Override
	public SFTPGoTokenResponse getTokenDetails(SFTPGoTokenRequest tokenRequest) {
		logger.info("Inside MFTRoutingServiceImpl >> getTokenDetails");
		logger.info("==============***************************************====================");
		logger.info("==============STARTED TOKEN SERVICE====================");
		try {
			String auth = tokenRequest.getUserName() + ":" + tokenRequest.getPassword();
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
			String authHeader = "Basic " + new String(encodedAuth);
			logger.info("authHeader : " + authHeader);
			
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
			header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			header.set("Authorization", authHeader);
			
			HttpEntity<?> req = new HttpEntity<>(header);
			
			ResponseEntity<SFTPGoTokenResponse> response = new RestTemplate().exchange("http://10.28.79.7/api/v2/user/token", HttpMethod.GET, req, SFTPGoTokenResponse.class);
			logger.info("==============***************************************====================");
			logger.info(response.getBody().getAccessToken());
			logger.info("==============End TOKEN SERVICE====================");
			return response.getBody();
		}catch(Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String getToolDetails(String userName) {
		logger.info("Inside MFTRoutingServiceImpl >> getToolDetails");
		logger.info("==============***************************************====================");
		logger.info("==============Get Tool Details====================");
		
		String toolName = mftRoutingServiceDAO.getToolInfo(userName);
		return toolName;
	}

}
