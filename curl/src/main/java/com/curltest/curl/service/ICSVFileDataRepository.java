package com.curltest.curl.service;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ICSVFileDataRepository extends CrudRepository<CSVFileData, Integer>{
	
	List<CSVFileData> findByFileName(String fileNmae);
	
	@Query("SELECT DISTINCT data.fileName FROM CSVFileData data")
	List<String> findDistinctFileName();
	
}
