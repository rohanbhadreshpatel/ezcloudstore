package com.cloud.ezcloudstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.cloud.ezcloudstore.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,String>{
	
	@Query("SELECT u FROM User u WHERE LOWER(u.emailid) = LOWER(:emailid)")
    public User getUser(@Param("emailid") String emailid);

}
