package com.shashwat.electronicstorebackend.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
	
	String setDefaultImage(String sex);
	
	String uploadImage(MultipartFile file, String path, String id, String entityName) throws IOException;
	
	InputStream getImageResource(String path, String name) throws FileNotFoundException;

}
