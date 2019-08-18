package com.curltest.curl.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Component
@Scope("singleton")
public class FileProcessorUtil {

	public static final String FILE_TYPE_ACCEPTED = ".csv";
	public static final long MAX_FILE_SIZE_ALLOWED = 20971520;
	private static final long  MEGABYTE = 1024L * 1024L;
	public static final long MAX_ROWS_ALLOWED_FOR_CSV= 20001;
	public static final String INVALID_CHARACTER = "-";

	public String checkForInvalidNumberFormat(String[] lines, Model model) {

		for(int i = 1 ; i < lines.length; i++) {
			if(lines[i].split(",")[1].contains("-")) {
				model.addAttribute("message", "CSV File contains -ve Number of "+lines[i].split(",")[1]+" on Entry of Date - "+ lines[i].split(",")[0]);
				return "baddata";
			}
		}
		return "gooddata";

	}

	public String checkForRows(String [] lines, Model model) {

		if(lines.length < 2) {
			model.addAttribute("message", "CSV File don't have any data to process.");
			return "baddata";
		}

		if(lines.length > MAX_ROWS_ALLOWED_FOR_CSV) {
			model.addAttribute("message", "CSV File must not contains more than "+MAX_ROWS_ALLOWED_FOR_CSV+" rows, But yuur file contains "+ lines.length +" rows.");
			return "baddata";
		}

		return "gooddata";		
	}
	public String checkForSize(MultipartFile file, Model model) {

		long fileSize = file.getSize();

		if(fileSize > MAX_FILE_SIZE_ALLOWED) {
			model.addAttribute("message", "File size must not cross "+MAX_FILE_SIZE_ALLOWED+" MB. But your file size is "+ fileSize/MEGABYTE +" MB");
			return "baddata";
		}else {
			return "gooddata";
		}
	}
	public String checkForFileExtenssionType(MultipartFile file, Model model) {

		String fileName = file.getOriginalFilename().toLowerCase();
		String fileExtensionType = fileName.substring(fileName.indexOf("."), fileName.length());

		if(fileExtensionType.contains(FILE_TYPE_ACCEPTED) == false) {
			model.addAttribute("message", fileExtensionType + " is not Acceptable, Only .csv is acceptable");
			return "baddata";
		}
		return "gooddata";
	}



}
