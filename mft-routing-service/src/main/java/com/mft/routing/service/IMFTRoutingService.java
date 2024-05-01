/**
 * 
 */
package com.mft.routing.service;

import org.springframework.web.multipart.MultipartFile;

import com.mft.routing.dto.SFTPGoUploadDTO;
import com.mft.routing.request.model.SFTPGoTokenRequest;
import com.mft.routing.response.SFTPGoTokenResponse;

/**
 * 
 */
public interface IMFTRoutingService {

	SFTPGoUploadDTO uploadFiles(String url, String path, Boolean mk_dir, MultipartFile file);

	SFTPGoTokenResponse getTokenDetails(SFTPGoTokenRequest tokenRequest);

	String getToolDetails(String userName);

	SFTPGoUploadDTO saveFile(String uploadAPI, String path, boolean mkdir_parents, MultipartFile filenames);

}
