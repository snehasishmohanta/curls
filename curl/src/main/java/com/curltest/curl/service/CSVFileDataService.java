package com.curltest.curl.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
@Scope("singleton")
public class CSVFileDataService {
	
	@Autowired
	ICSVFileDataRepository csvFileDataRepository;
	
	
	public String saveToDB(String fileName, String[] lines, Model model){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		List<CSVFileData> listOfData = new ArrayList<>();

		for(int i =1 ; i< lines.length; i++) {

			CSVFileData data = new CSVFileData();
			try {
				data.setDate(format.parse(lines[i].split(",")[0]));
			} catch (ParseException e) {
				model.addAttribute("message", "Invalid Date Format, Please check the file once.");
				return "home";
			}
			data.setNumber(Long.parseLong(lines[i].split(",")[1]));
			data.setFileName(fileName);

			listOfData.add(data);

		}
		csvFileDataRepository.saveAll(listOfData);
		
		model.addAttribute("successMessage", "Successfully uploaded!!! ");
		return "home";
	}
	
	public String processTheFlieForGraphicalRepresentaion(String fileName) {
		
		//Getting CSV Data Based on FileName
		List<CSVFileData> listOfDataForThePerticularFileName = csvFileDataRepository.findByFileName(fileName);

		
		final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

		final Map<String, List<Long>> requireDetails = new TreeMap<>();

		for (int i = 0; i < listOfDataForThePerticularFileName.size(); i++) {
			List<Long> listOfNumber = new ArrayList<>();

			final String dateMMYYYY = yearFormat.format(listOfDataForThePerticularFileName.get(i).getDate());

			if (requireDetails.get(dateMMYYYY) == null) {

				listOfNumber.add(listOfDataForThePerticularFileName.get(i).getNumber());
				requireDetails.put(dateMMYYYY, listOfNumber);
			} else {
				listOfNumber = requireDetails.get(dateMMYYYY);
				listOfNumber.add(listOfDataForThePerticularFileName.get(i).getNumber());
			}
		}
		
				
		
		final DecimalFormat decimalRoundUp = new DecimalFormat("##.00");

		final StringBuilder dataForGraph = new StringBuilder("[");
		dataForGraph.append("['Year', 'Forecast'],");

		for (final Map.Entry<String, List<Long>> entry : requireDetails.entrySet()) {

			final String year = entry.getKey();
			final List<Long> listOfForeCast = entry.getValue();
			final Double avgForeCastForTheYear = listOfForeCast.stream().mapToDouble(value -> value).average().orElse(0.0);

			dataForGraph.append("['" + year + "'," + decimalRoundUp.format(avgForeCastForTheYear) + "],");
		}

		dataForGraph.append("]");

		
		return dataForGraph.toString();
	}

	public List<String> getAvailableFiles() {
		List<String> fileList = csvFileDataRepository.findDistinctFileName();
		
		return fileList;
	}
	

}
