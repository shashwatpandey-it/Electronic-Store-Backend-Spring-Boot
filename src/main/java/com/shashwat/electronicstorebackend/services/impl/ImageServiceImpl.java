package com.shashwat.electronicstorebackend.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shashwat.electronicstorebackend.exceptions.UnsupportedExtensionException;
import com.shashwat.electronicstorebackend.repositories.CategoryRepository;
import com.shashwat.electronicstorebackend.repositories.ProductRepository;
import com.shashwat.electronicstorebackend.repositories.UserRepository;
import com.shashwat.electronicstorebackend.services.ImageService;
import com.shashwat.electronicstorebackend.services.ProductService;



@Service
public class ImageServiceImpl implements ImageService {
	
	private Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ProductRepository productRepository;

	@Transactional
	@Override
	public String uploadImage(MultipartFile file, String path, String id, String entityName) throws IOException {
		// TODO Auto-generated method stub
		String originalFileName = file.getOriginalFilename();
		logger.info("---- * original file name : {} *----", originalFileName);
		String generatedRandomUUID = UUID.randomUUID().toString();
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String savingFileName = generatedRandomUUID + extension;
		String fullPathWithFileName = path + savingFileName;
		
		if((extension.equals(".png")) || (extension.equals(".jpg")) || (extension.equals(".jpeg"))) {
			File folder = new File(path); // representing the given path by a file instance
			// if folder not present then creating the folder
			if(!folder.exists()) {
				logger.info("---- * folder/file created * ----");
				folder.mkdirs();
			}
			// upload the file(image file) to the desired folder
			Files.copy(file.getInputStream(), Path.of(fullPathWithFileName));
			switch (entityName) {
			case "user": {
				userRepository.updateUserSetImageNameForId(savingFileName, id);
				break;	
			}
			case "category": {
				categoryRepository.updateCategorySetCoverImageNameForId(savingFileName, id);
				break;
			}
			case "product": {
				productRepository.updateProductSetImageNameForId(savingFileName, id);
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value");
			}
			
			return savingFileName;
			
		}
		else {
			throw new UnsupportedExtensionException("File extension : " + extension + " not supported");
		}
		
	}

	@Override
	public InputStream getImageResource(String path, String name) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String fullPath = path+name;
		InputStream inputStream = new FileInputStream(fullPath);
		return inputStream;
	}

	@Override
	public String setDefaultImage(String sex) {
		// TODO Auto-generated method stub
		if(sex.equalsIgnoreCase("male")) return "male-user.jpg";
		else return "female-user.png";
	}

}
