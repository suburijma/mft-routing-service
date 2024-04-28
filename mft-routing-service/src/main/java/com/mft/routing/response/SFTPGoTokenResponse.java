/**
 * 
 */
package com.mft.routing.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SFTPGoTokenResponse {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("expires_at")
	private String expiresAt;

	@JsonProperty("message")
	private String message;

	@JsonProperty("error")
	private String error;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
