package com.mft.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.mft.routing.service.impl.MFTRoutingServiceImpl;
 
public class FileUploadUtil {
    public static File saveFile(String fileName, MultipartFile multipartFile)
            throws IOException {
    	Logger logger = LoggerFactory.getLogger(MFTRoutingServiceImpl.class);
    	
    	logger.info("Inside FileUploadUtil >> saveFile");
		logger.info("==============***************************************====================");
        Path uploadPath = Paths.get("Files-Upload");
          
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
         
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(multipartFile.getOriginalFilename());
            logger.info("Filename is "+fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            
            File files = new File("/Files-Upload/"+multipartFile.getOriginalFilename());
            System.out.println(files.getAbsolutePath() + ":::::" +files.getName());
            return files;
        } catch (IOException ioe) {       
            throw new IOException("Could not save file: " + fileName, ioe);
        }       
    }
}