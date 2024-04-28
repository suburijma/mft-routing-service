/**
 * 
 */
package com.mft.routing.request.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SFTPGoTokenRequest {
	
	@JsonProperty("user_name")
	private String userName;
	
	@JsonProperty("password")
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
