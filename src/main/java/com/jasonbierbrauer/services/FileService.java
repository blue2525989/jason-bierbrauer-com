package com.jasonbierbrauer.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.jasonbierbrauer.controller.PermissionController;
import com.jasonbierbrauer.model.Image;
import com.jasonbierbrauer.model.ImageTypes;
import com.jasonbierbrauer.repository.ImageRepository;
import com.jasonbierbrauer.repository.ImageTypeRepository;

@Controller
public class FileService extends PermissionController {
	
	private ImageRepository imgRepo;
	private AmazonS3Client s3client;
	private ImageTypeRepository imgTypeRepo;
	
	@Autowired
	public FileService(ImageRepository imgRepo,	AmazonS3Client s3client,
			ImageTypeRepository imgTypeRepo) {
		this.imgRepo = imgRepo;
		this.s3client = s3client;
		this.imgTypeRepo = imgTypeRepo;
	}

	// uses javaScript to upload, mainly just redirects to saved
	//@RequestMapping("/upload-image")
	public String upload(@RequestParam MultipartFile file, HttpSession session,
			MultipartRequest event, Model model, @RequestParam String type) {
		/*
		 * Obtain the Content length of the Input stream for S3 header
		 */
		byte[] contentBytes = null;
		try {
		    InputStream is = event.getFile(file.getName()).getInputStream();
		    contentBytes = IOUtils.toByteArray(is);
		} catch (IOException e) {
		    System.err.printf("Failed while reading bytes from %s", e.getMessage());
		} 

		Long contentLength = Long.valueOf(contentBytes.length);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentLength);
		metadata.setContentType(file.getContentType());
		

		/*
		 * Reobtain the tmp uploaded file as input stream
		 */
		InputStream inputStream = null;
		try {
			inputStream = event.getFile(file.getName()).getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * Put the object in S3
		 */
		try {

		    s3client.putObject(new PutObjectRequest("" + type,
		    		file.getOriginalFilename(), inputStream, metadata).withCannedAcl(CannedAccessControlList.PublicReadWrite));

		} catch (AmazonServiceException ase) {
		    System.out.println("Error Message:    " + ase.getMessage());
		    System.out.println("HTTP Status Code: " + ase.getStatusCode());
		    System.out.println("AWS Error Code:   " + ase.getErrorCode());
		    System.out.println("Error Type:       " + ase.getErrorType());
		    System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
		    System.out.println("Error Message: " + ace.getMessage());
		} finally {
		    if (inputStream != null) {
		        try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
		
		Image img = new Image();
		img.setName(file.getOriginalFilename());
		img.setUrl("" + type + "/" + file.getOriginalFilename());
		img.setType(type);
		imgRepo.save(img);
		
		// admin user
		if (hasAdminRole()) {
			session.setAttribute("adminrole", hasAdminRole());
		}
		// regular user
		else if (hasUserRole()) {
			session.setAttribute("userrole", hasUserRole());
		}
		addTypesForMenu(model);
		String saved = "Image " + file.getOriginalFilename() + " has been saved.";
		model.addAttribute("saved", saved);
		addFragments(model);
		return "admin/saved";
	}
	
	@RequestMapping("/upload-page")
	public String uploadImagePage(HttpSession session, Model model) {
		addFragments(model);
		addTypesForMenu(model);
		List<ImageTypes> fullList = imgTypeRepo.findAll();
		if (fullList != null) {
			model.addAttribute("fullList", fullList);
		}
		// admin user
		if (hasAdminRole()) {
			session.setAttribute("adminrole", hasAdminRole());
		}
		// regular user
		else if (hasUserRole()) {
			session.setAttribute("userrole", hasUserRole());
		}
		return "admin/upload-images";
	}
	
	//@RequestMapping("/new-image-type")
	public String addNewImgType(@RequestParam String name, @RequestParam String type, Model model) {
		addFragments(model);
		addTypesForMenu(model);
		ImageTypes img = new ImageTypes();
		img.setName(name);
		img.setType(type);
		imgTypeRepo.save(img);
		String saved = "The image type " + name + " has been saved.";
		model.addAttribute("saved", saved);
		return "admin/saved";
	}
	
	// delete image
	//@GetMapping(path="/delete-image")
	public String deleteImages(Long ID, Model model) {
		addFragments(model);
		addTypesForMenu(model);
		// need to add aws code to delete from bucket
		String file = imgRepo.findOne(ID).getName();
		String type = imgRepo.findOne(ID).getType();
		imgRepo.delete(ID);
		s3client.deleteObject(new DeleteObjectRequest("" + type, file));
		String saved = "The Image with Name " + file + " has been deleted.";
		model.addAttribute("saved", saved);
		return "admin/saved";
	}
	
	// delete image type
	//@GetMapping(path="/delete-image-type")
	public String deleteImageType(Long ID, Model model) {
		addFragments(model);
		addTypesForMenu(model);
		// need to add aws code to delete from bucket
		String file = imgTypeRepo.findOne(ID).getName();
		imgRepo.delete(ID);
		String saved = "The Image Type with Name " + file + " has been deleted.";
		model.addAttribute("saved", saved);
		return "admin/saved";
	}
		
	// list all images
	@RequestMapping("/list-images")
	public String listImages(Model model, @RequestParam String type) {
		addFragments(model);
		addTypesForMenu(model);
		List<Image> imageList = imgRepo.findAll();
		List<Image> realList = new ArrayList<Image>();
		for (int i = imageList.size()-1; i >= 0; i--) {
			if (imageList.get(i).getType().equalsIgnoreCase(type)) {
				realList.add(imageList.get(i));
			}
		}
		if (realList != null) {
			model.addAttribute("listMain", realList);
		}	
		return "admin/list-all-images";
	}
	
	// list all image types
	@RequestMapping("/list-images-type")
	public String listImageType(Model model) {
		addFragments(model);
		addTypesForMenu(model);
		List<ImageTypes> imageList = imgTypeRepo.findAll();
		if (imageList != null) {
			model.addAttribute("listMain", imageList);
		}	
		return "admin/list-all-image-types";
	}
}


