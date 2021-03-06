package com.gridone.scraping.model;

import java.util.Date;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import com.gridone.scraping.type.EnumActive;
import com.gridone.scraping.type.EnumUserRole;

public class LoginUserDetails extends User {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String email;
	private String password;
	private String name;
	private String phone;
	private EnumUserRole role;
	private Date regDt;
	private EnumActive active;
	private Integer passwordFailCnt;
	private Date changeDt;
	
	public LoginUserDetails(UserModel user) {
		super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
		id = user.getId();
		email = user.getEmail();
		password = user.getPassword();
		name = user.getName();
		phone = user.getPhone();
		role = user.getRole();
		regDt = user.getRegDt();
		active = user.getActive();
		passwordFailCnt = user.getPasswordFailCnt();
		changeDt = user.getChangeDt();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public EnumUserRole getRole() {
		return role;
	}

	public void setRole(EnumUserRole role) {
		this.role = role;
	}

	public Date getRegDt() {
		return regDt;
	}

	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}

	public EnumActive getActive() {
		return active;
	}

	public void setActive(EnumActive active) {
		this.active = active;
	}

	public Integer getPasswordFailCnt() {
		return passwordFailCnt;
	}

	public void setPasswordFailCnt(Integer passwordFailCnt) {
		this.passwordFailCnt = passwordFailCnt;
	}

	public Date getChangeDt() {
		return changeDt;
	}

	public void setChangeDt(Date changeDt) {
		this.changeDt = changeDt;
	}

	
}
