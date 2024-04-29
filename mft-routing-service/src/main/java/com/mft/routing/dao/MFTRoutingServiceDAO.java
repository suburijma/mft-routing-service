package com.mft.routing.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.mft.routing.service.impl.MFTRoutingServiceImpl;

@Repository
public class MFTRoutingServiceDAO {
	Logger logger = LoggerFactory.getLogger(MFTRoutingServiceDAO.class);
	
	@Autowired
	private Environment env;

	public String getToolInfo(String userName) {
		logger.info("Inside MFTRoutingServiceImpl >> getTokenDetails");
		logger.info(env.getProperty(userName));
		
		String toolName = env.getProperty(userName);
		return toolName;
	}

}
