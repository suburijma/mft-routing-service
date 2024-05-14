package com.mft.utility;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class DownloadUtility {
	Logger logger = LoggerFactory.getLogger(DownloadUtility.class);

	// code request code here
	public String doGetRequest(String headerAuth) throws IOException {
		logger.info("Inside DownloadUtility :::");
		logger.info("Changes for testing :::");
		OkHttpClient client1 = new OkHttpClient();
		MediaType mediaType = MediaType.parse("text/plain");
		RequestBody body = RequestBody.create(mediaType, "");
		Request request = new Request.Builder().url("http://10.28.79.7/api/v2/user/files/upload?path=image6.png")
				.method("GET", body).addHeader("Accept", "*/*, application/json")
				.addHeader("Authorization", "Bearer 123").build();

		logger.info("Before ---------------- DownloadUtility :::");
		Response response = client1.newCall(request).execute();
		logger.info("After ---------------- DownloadUtility :::");
		return response.body().string();
	}

}
