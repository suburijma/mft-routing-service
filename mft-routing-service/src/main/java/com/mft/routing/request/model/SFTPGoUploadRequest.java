/**
 * 
 */
package com.mft.routing.request.model;

/**
 * 
 */
public class SFTPGoUploadRequest {

	private boolean mkdir_parents;
	private String path;

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
