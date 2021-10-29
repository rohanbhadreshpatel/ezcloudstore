package com.cloud.ezcloudstore.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cloud.ezcloudstore.model.Files;


@Repository
public interface FileRepository extends JpaRepository<Files,Integer> {
	
	@Query("SELECT file FROM Files file WHERE LOWER(file.emailid) = LOWER(:emailid)")
    public ArrayList<Files> retrieveUserFiles(@Param("emailid") String emailid);
	
	@Query("SELECT file FROM Files file")
    public ArrayList<Files> retrieveAllFiles();

}
