package com.gridone.scraping.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gridone.scraping.model.UserModel;


@Mapper
public interface UserMapper {

	UserModel selectByLogin(String email);

	void insertUser(UserModel um);

	List<UserModel> getAllAdmins();

	List<UserModel> getUsers();


}
