package com.curltest.curl.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.curltest.curl.service.CSVFileDataService;
import com.curltest.curl.util.FileProcessorUtil;

@Controller
public class HomeController {

	@Autowired
	FileProcessorUtil fileProcessorUtils;

	@Autowired
	CSVFileDataService csvFileDataService;

	@GetMapping(value= {"/home", "/"})
	public String getHomePage() {
		return "home";
	}
	@RequestMapping(value="/process")
	public String process(@RequestParam("file") MultipartFile file , Model model) throws Exception {

		if(fileProcessorUtils.checkForFileExtenssionType(file, model).equalsIgnoreCase("baddata")) {
			return "home";
		}
		if(fileProcessorUtils.checkForSize(file, model).equalsIgnoreCase("baddata")) {
			return "home";
		}

		String uploadedFile = null;
		try {
			uploadedFile = new String(file.getBytes());
		} catch (IOException e) {
			model.addAttribute("error", "Something went wrong.");
			return "home";
		}
		String [] lines = uploadedFile.split("\n");

		if (fileProcessorUtils.checkForRows(lines,model).equalsIgnoreCase("baddata")) {
			return "home";
		}
		if (fileProcessorUtils.checkForInvalidNumberFormat(lines,model).equalsIgnoreCase("baddata")) {
			return "home";
		}

		String fileName = file.getOriginalFilename();
		// Saving the CSV data into DB
		csvFileDataService.saveToDB(fileName, lines, model);
		
		return "home";

	}
}
