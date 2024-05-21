/**
 * 
 */
package com.mft.routing.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
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

import com.mft.enums.MFTRoutingServiceEnum;
import com.mft.routing.dao.MFTRoutingServiceDAO;
import com.mft.routing.dto.SFTPGoUploadDTO;
import com.mft.routing.request.model.SFTPGoTokenRequest;
import com.mft.routing.response.SFTPGoTokenResponse;
import com.mft.routing.service.IMFTRoutingService;
import com.mft.utility.FileUploadUtil;
import com.mft.utility.ToolAPIDetail;

/**
 * 
 */
@Service
public class MFTRoutingServiceImpl implements IMFTRoutingService {
	Logger logger = LoggerFactory.getLogger(MFTRoutingServiceImpl.class);

	@Value("${sftpgourl}")
	private String sftpgourl;

	@Autowired
	private Environment env;

	@Autowired
	MFTRoutingServiceDAO mftRoutingServiceDAO;

	@Override
	public SFTPGoUploadDTO uploadFiles(String uploadAPI, String path, boolean mkdir_parents, MultipartFile file,
			String userName) {
		logger.info("Inside MFT_Routing_ServiceImpl >> saveFile");
		logger.info("uploadAPI " + uploadAPI);

		ToolAPIDetail apiDetail = new ToolAPIDetail();
		String endPoint = apiDetail.getToolAPI(env.getProperty(userName), MFTRoutingServiceEnum._upload.name(), env);

		endPoint = endPoint + "path=" + file.getOriginalFilename() + "&mkdir_parents=false";
		logger.info(endPoint);

		// TODO : based on data will rewrite the code
		if (userName.equalsIgnoreCase("mft-axway-2")) {
			String header = getAXTokenDetails(null, file);
			/// logger.info("header " + header.getMessage());
			return null;
		}

		SFTPGoTokenRequest tokenRequest = new SFTPGoTokenRequest();
		tokenRequest.setUserName("user1");
		tokenRequest.setPassword("Rijma@856411");

		SFTPGoTokenResponse response = getTokenDetails(tokenRequest);
		String authHeader = "Bearer " + response.getAccessToken();
		logger.info("authHeader : " + authHeader);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("Authorization", authHeader);
		logger.info("Inside MFT_Routing_ServiceImpl >> header");

		// changes now
		FileUploadUtil fileUpload = new FileUploadUtil();
		File savedFile;
		ResponseEntity<SFTPGoUploadDTO> responseEntity;
		try {
			savedFile = fileUpload.saveFile(file.getName(), file);
			logger.info("savedFile : " + savedFile.getAbsolutePath());

			MultiValueMap<String, Object> data = new LinkedMultiValueMap<String, Object>();
			ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
				@Override
				public String getFilename() {
					return file.getName();
				}
			};
			data.add("file", resource);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
					data, headers);
			RestTemplate restTemplate = new RestTemplate();

			responseEntity = restTemplate.postForEntity(endPoint, requestEntity, SFTPGoUploadDTO.class);
			logger.info("*********************&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&********************************");
			return responseEntity.getBody();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Something Failed@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public SFTPGoTokenResponse getTokenDetails(SFTPGoTokenRequest tokenRequest) {
		logger.info("Inside MFTRoutingServiceImpl >> getTokenDetails");
		logger.info("==============***************************************====================");
		logger.info("==============STARTED TOKEN SERVICE====================");

		try {
			String auth = null;
			String endPoint = null;
			if (tokenRequest != null && !tokenRequest.getUserName().isEmpty()
					&& !tokenRequest.getPassword().isEmpty()) {
				auth = tokenRequest.getUserName() + ":" + tokenRequest.getPassword();
				ToolAPIDetail apiDetail = new ToolAPIDetail();
				endPoint = apiDetail.getToolAPI(env.getProperty(tokenRequest.getUserName()),
						MFTRoutingServiceEnum._token.name(), env);
				logger.info("End Point is :: " + endPoint);
			}

			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
			String authHeader = MFTRoutingServiceEnum.Basic.name() + " " + new String(encodedAuth);
			logger.info("Value of authHeader is :: " + authHeader);

			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
			header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			header.set(MFTRoutingServiceEnum.Authorization.name(), authHeader);

			HttpEntity<?> req = new HttpEntity<>(header);

			ResponseEntity<SFTPGoTokenResponse> response = new RestTemplate()
					.exchange("http://10.28.79.7/api/v2/user/token", HttpMethod.GET, req, SFTPGoTokenResponse.class);

			logger.info("==============***************************************====================");
			logger.info(response.getBody().getAccessToken());
			logger.info("==============End TOKEN SERVICE====================");

			return response.getBody();
		} catch (Exception e) {
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

	@Override
	public SFTPGoUploadDTO saveFile(String uploadAPI, String path, boolean mkdir_parents, MultipartFile file) {
		logger.info("Inside MFT_Routing_ServiceImpl >> saveFile");
		logger.info("uploadAPI " + uploadAPI);

		SFTPGoTokenRequest tokenRequest = new SFTPGoTokenRequest();
		tokenRequest.setUserName("user1");
		tokenRequest.setPassword("Rijma@856411");

		SFTPGoTokenResponse response = getTokenDetails(tokenRequest);
		String authHeader = "Bearer " + response.getAccessToken();
		logger.info("authHeader : " + authHeader);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		// headers.set("Authorization", authHeader);
		logger.info("Inside MFT_Routing_ServiceImpl >> header");

		// changes now
		FileUploadUtil fileUpload = new FileUploadUtil();
		File savedFile;
		ResponseEntity<SFTPGoUploadDTO> responseEntity;
		try {
			savedFile = fileUpload.saveFile(file.getName(), file);
			logger.info("savedFile : " + savedFile.getAbsolutePath());

			FileSystemResource fileSystemResource = new FileSystemResource(savedFile);
			MultiValueMap<String, Object> fileUploadMap = new LinkedMultiValueMap<>();
			fileUploadMap.set("filenames", fileSystemResource);

			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(fileUploadMap, headers);
			RestTemplate restTemplate = new RestTemplate();

			responseEntity = restTemplate.postForEntity(
					"http://10.28.79.7/api/v2/user/files?path=image3.png&mkdir_parents=false", httpEntity,
					SFTPGoUploadDTO.class);
			logger.info("*********************&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&********************************");
			return responseEntity.getBody();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Something Failed@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			e.printStackTrace();
		}
		return null;

		/*
		 * ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
		 * 
		 * @Override public String getFilename() { return file.getOriginalFilename(); }
		 * };
		 * 
		 * final MultiValueMap<String, Object> fileUploadMap = new
		 * LinkedMultiValueMap<>(); fileUploadMap.set("file", fileAsResource);
		 * 
		 * HttpEntity<ByteArrayResource> attachmentPart; ByteArrayResource
		 * fileAsResource1 = new ByteArrayResource(file.getBytes()) {
		 * 
		 * @Override public String getFilename() { return file.getOriginalFilename(); }
		 * }; attachmentPart = new HttpEntity<>(fileAsResource1, headers); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

		/*
		 * HttpEntity<MultiValueMap<String,Object>> requestEntity = new
		 * HttpEntity<>(attachmentPart,headers);
		 * 
		 * RestTemplate restTemplate = new RestTemplate();
		 * ResponseEntity<SFTPGoUploadDTO> responseEntity = restTemplate.postForEntity(
		 * "http://10.28.79.7/api/v2/user/files?path=image3.png&mkdir_parents=false",
		 * httpEntity, SFTPGoUploadDTO.class);
		 */

		/*
		 * multipartRequest.set("file",attachmentPart); FileSystemResource fileSys =
		 * file.getBytes(). String contentType = file.getContentType(); Resource files =
		 * file.getResource();
		 * 
		 * MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
		 * multipartBodyBuilder.part(file.getName(), files, MediaType.APPLICATION_JSON);
		 * logger.info("Inside MFT_Routing_ServiceImpl >> MultipartBodyBuilder");
		 * 
		 * // multipart/form-data request body MultiValueMap<String, HttpEntity<?>>
		 * multipartBody = multipartBodyBuilder.build();
		 * 
		 * // The complete http request body. HttpEntity<MultiValueMap<String,
		 * HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody, headers);
		 * 
		 * RestTemplate restTemplate = new RestTemplate();
		 * ResponseEntity<SFTPGoUploadDTO> responseEntity = restTemplate.postForEntity(
		 * "http://10.28.79.7/api/v2/user/files?path=image3.png&mkdir_parents=false",
		 * httpEntity, SFTPGoUploadDTO.class);
		 */

		// logger.info("Checking the response" + responseEntity.getBody().toString());
		// return null;

	}

	@Override
	public String downloadFile(String downloadURL, String path, String userName, String inline) {

		logger.info("Inside MFT_Routing_ServiceImpl >> downloadFile");
		logger.info("downloadURL :::: " + downloadURL);

		try {
			SFTPGoTokenRequest tokenRequest = new SFTPGoTokenRequest();
			tokenRequest.setUserName("user1");
			tokenRequest.setPassword("Rijma@856411");

			SFTPGoTokenResponse response = getTokenDetails(tokenRequest);
			String authHeader = "Bearer " + response.getAccessToken();
			logger.info("authHeader : " + authHeader);

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
			headers.set("Authorization", authHeader);

			logger.info("Inside MFT_Routing_ServiceImpl >> header");

			// changes now
			String uri = "http://10.28.79.7/api/v2/user/files?path=image6.png";
			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<String> entity = new HttpEntity<String>(headers);

			logger.info("Inside MFT_Routing_ServiceImpl >> Before restTemplate");
			ResponseEntity<byte[]> response1 = restTemplate.exchange(
					"http://10.28.79.7/api/v2/user/files?path=image6.png", HttpMethod.GET, entity, byte[].class, "1");
			logger.info("Inside MFT_Routing_ServiceImpl >> After restTemplate");

			logger.info("URL link = new URL(\"http://10.28.79.7/api/v2/user/files?path=image6.png");
			URL link = new URL("http://10.28.79.7/api/v2/user/files?path=image6.png");
			InputStream in = new BufferedInputStream(link.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response2 = out.toByteArray();
			logger.info("new FileOutputStream");

			FileOutputStream fos = new FileOutputStream("/Documents/temp/" + "image6.png");
			fos.write(response2);
			fos.close();
			logger.info("After new FileOutputStream");

			logger.info("*********************&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&********************************");

			return "Download successfil";

			/*
			 * responseEntity = restTemplate.postForEntity(
			 * "http://10.28.79.7/api/v2/user/files/upload?path=image6.png&mkdir_parents=false",
			 * requestEntity, SFTPGoUploadDTO.class);
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Something Failed@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			e.printStackTrace();
		}
		return null;

	}

	public String getAXTokenDetails(SFTPGoTokenRequest tokenRequest, MultipartFile file) {
		logger.info("Inside MFTRoutingServiceImpl >> getTokenDetails");
		logger.info("==============***************************************====================");
		logger.info("==============STARTED TOKEN SERVICE====================");

		try {

			String auth = "mft-axway-2:test";
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
			String authHeader = "Basic " + new String(encodedAuth);
			logger.info("authHeader : " + authHeader);
			MultiValueMap<String, Object> multipartReq = new LinkedMultiValueMap<>();

			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			HttpEntity<ByteArrayResource> reqPart;

			ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
				@Override
				public String getFilename() {
					return file.getOriginalFilename();
				}
			};

			reqPart = new HttpEntity<>(fileAsResource, header);
			multipartReq.add("file", reqPart);

			HttpHeaders headersJson = new HttpHeaders();
			headersJson.setContentType(MediaType.APPLICATION_JSON);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.set("Referer", "Ian");
			headers.set("Authorization", authHeader);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
					multipartReq, headers);

			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<String> responseEntity = restTemplate.exchange(
					"https://filetransfer-stage.transunion.com/api/v2.0/files/?idp_id=ST_IDP&transferMode=BINARY",
					HttpMethod.POST, requestEntity, String.class);
			logger.info("*********************&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&********************************   :::"
					+ responseEntity.getStatusCodeValue());
			logger.info("*********************&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&*************************LOCATION   :::"
					+ responseEntity.getHeaders().LOCATION);

			/*
			 * FileSystemResource fileSystemResource = new FileSystemResource(savedFile);
			 * MultiValueMap<String, Object> fileUploadMap = new LinkedMultiValueMap<>();
			 * fileUploadMap.set("file", fileSystemResource);
			 * 
			 * HttpEntity<MultiValueMap<String, Object>> httpEntity = new
			 * HttpEntity<>(fileUploadMap, headers);
			 * 
			 * 
			 * ResponseEntity<String> str = restTemplate.postForEntity(
			 * "https://filetransfer-stage.transunion.com/api/v2.0/files/?idp_id=ST_IDP",
			 * httpEntity, String.class); logger.
			 * info("))))))))))))))))00000000000000000000000000((((((((((((((((((((((( "+
			 * str.getStatusCodeValue());
			 */
			return responseEntity.getBody();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Something Failed@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			e.printStackTrace();
		}

		return null;

	}
}
