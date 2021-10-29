package com.cloud.ezcloudstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.servlet.http.Part;
import java.io.InputStream;

import com.cloud.ezcloudstore.model.Files;

//import javax.servlet.annotation.MultipartConfig;

import com.cloud.ezcloudstore.model.User;
import com.cloud.ezcloudstore.service.AWSService;
import com.cloud.ezcloudstore.service.EzCloudStoreService;


@Controller
public class EzCloudStoreController {
	
	@Autowired
	private EzCloudStoreService ezcloudstoreservice;
	
	@Autowired
	private AWSService awsservice;
	
	@Autowired
	EzCloudStoreController(AWSService awsservice) {
		this.awsservice = awsservice;
	}
	
	@GetMapping("/")
	public String start() {
		return "register";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}
	
	@GetMapping("/adminlogin")
	public String adminLogin() {
		return "adminlogin";
	}
	
	@GetMapping("/admindashboard")
	public String adminDashboard() {
		return "admindashboard";
	}
	
	@GetMapping("/googleLogin")
	public String googleLogin() {
		return "googleLogin";
	}
	
	
	@PostMapping("/registerUser")
	public String registerUser(@RequestParam("firstname") String firstname,@RequestParam("lastname") String lastname, @RequestParam("emailid") String emailid,
			@RequestParam("password") String password, HttpSession session) {

		User user = new User();
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setEmailid(emailid);
		user.setPassword(password);
		if(!ezcloudstoreservice.checkUser(emailid)) {
			if (ezcloudstoreservice.registerUser(user)) {
				session.setAttribute("status", "Congratulations, your account has been successfully created. Please log in..!");
				return "redirect:/login";
			}
			session.setAttribute("status", "User already exists!");
			return "redirect:/register";
		}
		session.setAttribute("status", "User already exists!");
		return "redirect:/register";

	}
	
	@PostMapping("/loginUser")
	public String loginUser(@RequestParam("emailid") String emailid,
			@RequestParam("password") String password, HttpSession session) {
		User user = ezcloudstoreservice.getUser(emailid, password);
		
		if (user == null) {
			session.setAttribute("status", "Invalid username or Password");
			return "redirect:/login";			
		}
		ArrayList<Files> files = ezcloudstoreservice.retrieveUserFiles(emailid);
		String name = user.getFirstname() + " " + user.getLastname();
		session.setAttribute("name", name);	
		session.setAttribute("emailid", emailid);
		if(files != null) {
		session.setAttribute("files",files);
		return "redirect:/dashboard";
	    }
		else
		{
			session.setAttribute("files", null);
			return "redirect:/dashboard";
		 }
	}
	
	@PostMapping("/loginWithGoogle")
	public String loginWithGoogle(@RequestParam("emailid") String emailid, @RequestParam("googleusername") String googleusername, HttpSession session) {
		ArrayList<Files> files = ezcloudstoreservice.retrieveUserFiles(emailid);
		session.setAttribute("name", googleusername);	
		session.setAttribute("emailid", emailid);
		if(files != null) {
		session.setAttribute("files",files);
		return "redirect:/dashboard";
	    }
		else
		{
			session.setAttribute("files", null);
			return "redirect:/dashboard";
		 }		
		
	}
	
	@PostMapping("/uploadFile")
	public String uploadFile(@RequestPart("filepart") Part filepart, @RequestParam("description") String description,  @RequestParam("name") String name, @RequestParam("emailid") String emailid, HttpSession session) {		
		System.out.println("filepart");
		System.out.println(filepart);
		System.out.println(filepart.getSize());
		Long fileSize = filepart.getSize() / 1024 / 1024;
		ArrayList<Files> userFiles = ezcloudstoreservice.retrieveUserFiles(emailid);
		session.setAttribute("name", name);	
		session.setAttribute("emailid", emailid);
		InputStream inputStream = null;
		if((fileSize <= 10)) {
		String filename;
				
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());	
				filename = getFileName(filepart);
				Long fileinKB =  filepart.getSize() / 1024;
				ArrayList<Files> retrievedFiles = ezcloudstoreservice.retrieveUserFiles(emailid);
				String fileUrl = awsservice.uploadFileToS3(filename, filepart,inputStream, emailid);
				Files files = new Files();
				files.setEmailId(emailid);
				files.setFileName(filename);
				files.setDescription(description);
				files.setFileSize(fileinKB);
				files.setUpdatedTime(timestamp);
				files.setFileUrl(fileUrl);
				Files existingFile = ezcloudstoreservice.checkFile(filename, retrievedFiles);
				System.out.println("existingFile" +existingFile);
				if(existingFile == null) {					
					files.setCreatedTime(timestamp);
				if(ezcloudstoreservice.uploadUserFile(files)) {
					ArrayList<Files> latestFiles = ezcloudstoreservice.retrieveUserFiles(emailid);
					session.setAttribute("files", latestFiles);
					session.setAttribute("message", "Congratulations, File Uploaded Successfully");
					return "redirect:/dashboard";
				}
				session.setAttribute("files", userFiles);
				session.setAttribute("message", "Upload Failed");
				return "redirect:/dashboard";
				} else {
					files.setCreatedTime(existingFile.getCreatedTime());
					files.setFileID(existingFile.getFileID());
					System.out.println("id" +existingFile.getFileID());
					if(ezcloudstoreservice.uploadUserFile(files)) {
						ArrayList<Files> latestFiles = ezcloudstoreservice.retrieveUserFiles(emailid);
						session.setAttribute("files", latestFiles);
						session.setAttribute("message", "Congratulations, File Updated Successfully");
						return "redirect:/dashboard";
					}
					session.setAttribute("files", userFiles);
					session.setAttribute("message", "Upload Failed");
					return "redirect:/dashboard";
				}				 
		}	
		session.setAttribute("files", userFiles);
		session.setAttribute("message", "Upload failed!!! Please upload images less than 10MB.");
		return "redirect:/dashboard";
	}
	
	@GetMapping("/downloadFile")
    private ResponseEntity downloadFile(@RequestParam("fileUrl") String fileUrl, @RequestParam("emailid") String emailid){
    	String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
    	System.out.println("filename");
    	System.out.println(fileName);
    	byte[] data= awsservice.downloadFile(fileName, emailid);
        String contentType = null;
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(data);
	}
	
	@PostMapping("/delete")
	public String deleteFile(@RequestParam(value = "emailid") String emailid, @RequestParam("name") String name,
			@RequestParam("filename") String filename,
	@RequestParam("fileId") Integer fileId, HttpSession session){
		session.setAttribute("name", name);	
		session.setAttribute("emailid", emailid);
		if(ezcloudstoreservice.deleteFile(fileId)) {
			awsservice.deleteFile(filename, emailid);
			ArrayList<Files> latestFiles = ezcloudstoreservice.retrieveUserFiles(emailid);
			session.setAttribute("files", latestFiles);
			session.setAttribute("message", "Congratulations, Deleted File Successfully");
			return "redirect:/dashboard";
		}
		ArrayList<Files> latestFiles = ezcloudstoreservice.retrieveUserFiles(emailid);
		session.setAttribute("files", latestFiles);
		session.setAttribute("message", "Deletion Failed");
		return "redirect:/dashboard";
	}
	
	@PostMapping("/checkadmin")
	public String checkadmin(@RequestParam("emailid") String emailid,
			@RequestParam("password") String password, HttpSession session) {		
		if (!emailid.equals("admin@sjsu.edu") || !password.equals("adminpassword")) {
			session.setAttribute("status", "Invalid username or password");
			return "redirect:/adminlogin";			
		}
		ArrayList<Files> files = ezcloudstoreservice.retrieveAllFiles();
		session.setAttribute("name", "Admin");	
		session.setAttribute("emailid", emailid);
		if(files != null) {
		session.setAttribute("files",files);
		return "redirect:/admindashboard";
	    }
		else
		{
			session.setAttribute("files", null);
			return "redirect:/admindashboard";
		 }
	}
	
	@PostMapping("/admindelete")
	public String adminDeleteFile(@RequestParam(value = "emailid") String emailid,
			@RequestParam("filename") String filename,
	@RequestParam("fileId") Integer fileId, HttpSession session){
		session.setAttribute("name", "Admin");	
		session.setAttribute("emailid", "admin@sjsu.edu");
		if(ezcloudstoreservice.deleteFile(fileId)) {
			awsservice.deleteFile(filename, emailid);
			ArrayList<Files> latestFiles = ezcloudstoreservice.retrieveAllFiles();
			session.setAttribute("files", latestFiles);
			session.setAttribute("message", "Congratulations, Deleted User File Successfully");
			return "redirect:/admindashboard";
		}
		ArrayList<Files> latestFiles = ezcloudstoreservice.retrieveAllFiles();
		session.setAttribute("files", latestFiles);
		session.setAttribute("message", "Deletion failed");
		return "redirect:/admindashboard";
	}
	
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout(ModelMap model, HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
    
	public File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
	
	public String getFileName(final Part part) {
	    for (String content : part.getHeader("content-disposition").split(";")) {
	        if (content.trim().startsWith("filename")) {
	            return content.substring(
	                    content.indexOf('=') + 1).trim().replace("\"", "");
	        }
	    }
	    return null;
	}
		

}
