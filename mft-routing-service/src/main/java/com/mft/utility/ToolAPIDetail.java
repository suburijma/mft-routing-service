package com.mft.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ToolAPIDetail {
	Logger logger = LoggerFactory.getLogger(ToolAPIDetail.class);

	@Autowired
	private Environment env;
	
	public String getToolAPI(String toolName, String operation) {
		logger.info("Inside ToolAPIDetail >>>>>getToolAPI ");
		String toolIP = env.getProperty(toolName+"_ip");
		logger.info("toolIP >>> " + toolIP);
		String apiVersion = env.getProperty(toolName+"_apiVersion");
		logger.info("apiVersion >>> " + apiVersion);
		String opertn = env.getProperty(toolName+operation);
		logger.info("opertn >>> " + opertn);
		String url = toolIP+apiVersion+opertn;
		logger.info("url >>> " + url);
		return url;
	}

}
