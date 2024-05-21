/**
 * 
 */
package com.mft.routing.request.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SFTPGoUploadRequest {

	private boolean mkdir_parents;
	private String path;
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isMkdir_parents() {
		return mkdir_parents;
	}

	public void setMkdir_parents(boolean mkdir_parents) {
		this.mkdir_parents = mkdir_parents;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
