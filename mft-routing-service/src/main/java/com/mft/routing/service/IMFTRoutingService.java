/**
 * 
 */
package com.mft.routing.service;

import org.springframework.web.multipart.MultipartFile;

import com.mft.routing.dto.SFTPGo_Upload_DTO;
import com.mft.routing.request.model.SFTPGoTokenRequest;
import com.mft.routing.response.SFTPGoTokenResponse;

/**
 * 
 */
public interface IMFTRoutingService {

	SFTPGo_Upload_DTO uploadFiles(String url, String path, Boolean mk_dir, MultipartFile file);

	SFTPGoTokenResponse getTokenDetails(SFTPGoTokenRequest tokenRequest);

	String getToolDetails(String userName);

}
