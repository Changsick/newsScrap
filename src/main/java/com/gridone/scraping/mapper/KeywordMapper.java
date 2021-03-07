package com.gridone.scraping.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gridone.scraping.model.Keyword;
import com.gridone.scraping.model.LoginUserDetails;
import com.gridone.scraping.model.SearchBase;

@Mapper
public interface KeywordMapper {

	List<Keyword> selectSearchList(SearchBase searchBase);

	Integer selectSearchListCount(SearchBase searchBase);

	List<Keyword> selectAll();

	Keyword selectById(Integer id);

	Integer addEnterprise(Keyword param);

	Integer modifyEnterprise(Keyword param);

	Integer deleteEnterprise(Keyword param);

	List<Keyword> selectByLogin(Integer id);

	Integer deleteEnterpriseByLogin(Integer id);

	List<Keyword> selectAdmin(LoginUserDetails user);

}
