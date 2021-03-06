package com.gridone.scraping.model;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.gridone.scraping.type.EnumActive;
import com.gridone.scraping.type.EnumUserRole;

public class UserModel {
	private Integer id;
	
	/*@NotNull
	@NotBlank
	@Email(message="이메일 형식을 확인해주세요.")
	private String email;
	
	@NotNull
	@NotBlank
	@Pattern(regexp="^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$", message="비밀번호는 8~16 자리까지 특수문자를 포함해야 합니다.")
	private String password;
	
	@NotNull
	@NotBlank
	private Integer positionId;
	
	@NotNull
	@NotBlank
	private Integer departmentId;
	
	@NotNull
	@NotBlank
	@Size(min=1, max=30, message="이름은 1 ~ 30자까지 입력 가능합니다.")
	private String name;
	
	@NotNull
	@NotBlank
	@Pattern(regexp="^[0-9]{9,20}$", message="휴대폰 번호는 9~20자리까지 숫자만 입력 가능합니다.")
	private String phone;*/
	
	@NotBlank
	@Length(min=8)
	@Length(max=30)
	@Email(message="이메일 형식을 확인해주세요.")
	private String email;
	
	@NotBlank
	@Length(min=8)
	@Length(max=16)
	@Pattern(regexp="^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$", message="비밀번호는 8~16 자리까지 특수문자를 포함해야 합니다.")
	private String password;

	@NotBlank
	@Length(min=1)
	@Length(max=30)
	private String name;

	@Length(min=9)
	@Length(max=20)
	@Pattern(regexp="^[0-9]{9,20}$", message="휴대폰 번호는 9~20자리까지 숫자만 입력 가능합니다.")
	private String phone;
	private EnumActive active;
	private EnumUserRole role;
	private Integer passwordFailCnt;
	private Date regDt;
	private Date changeDt;
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
	public EnumActive getActive() {
		return active;
	}
	public void setActive(EnumActive active) {
		this.active = active;
	}
	public EnumUserRole getRole() {
		return role;
	}
	public void setRole(EnumUserRole role) {
		this.role = role;
	}
	public Integer getPasswordFailCnt() {
		return passwordFailCnt;
	}
	public void setPasswordFailCnt(Integer passwordFailCnt) {
		this.passwordFailCnt = passwordFailCnt;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	public Date getChangeDt() {
		return changeDt;
	}
	public void setChangeDt(Date changeDt) {
		this.changeDt = changeDt;
	}
	@Override
	public String toString() {
		return "UserModel [id=" + id + ", email=" + email + ", password=" + password + ", name=" + name + ", phone="
				+ phone + ", active=" + active + ", role=" + role + ", passwordFailCnt=" + passwordFailCnt + ", regDt="
				+ regDt + ", changeDt=" + changeDt + "]";
	}
	
	
	
}//.class
