package com.curltest.curl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curltest.curl.service.CSVFileDataService;
import com.curltest.curl.service.FileName;

@Controller
public class FileListController {

	@Autowired
	CSVFileDataService csvFileDataService;


	@RequestMapping(value = "/available")
	public String getAvailableFile(Model model) {

		List<String> fileList = csvFileDataService.getAvailableFiles();

		if(fileList.isEmpty()) {

			model.addAttribute("message", "Don't have any file to represent graph.");
			return "home";
		}


		model.addAttribute("fileList", fileList);
		model.addAttribute("fileName", new FileName());
		return "available";
	}

	@RequestMapping(value = "/showgraph")
	public String showGraphForFile(@ModelAttribute FileName fileName, Model model) {

		String dataForGraph= csvFileDataService.processTheFlieForGraphicalRepresentaion(fileName.getName());
		
		if(dataForGraph.equalsIgnoreCase("[['Year', 'Forecast'],]")) {
			model.addAttribute("message", "You need to upload the File first");
			return "home";
		}

		model.addAttribute("filteredData", dataForGraph);
		return "showgraph";
	}


}
