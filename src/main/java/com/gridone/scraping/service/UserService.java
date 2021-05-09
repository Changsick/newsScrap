package com.gridone.scraping.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gridone.scraping.mapper.NewsMonitoringMapper;
import com.gridone.scraping.mapper.UserMapper;
import com.gridone.scraping.model.Keyword;
import com.gridone.scraping.model.LoginUserDetails;
import com.gridone.scraping.model.ResultList;
import com.gridone.scraping.model.ScheduleModel;
import com.gridone.scraping.model.SearchBase;
import com.gridone.scraping.model.UserModel;
import com.gridone.scraping.type.EnumActive;
import com.gridone.scraping.type.EnumScheduleType;
import com.gridone.scraping.type.EnumUserRole;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	KeywordService keywordService;
	
	@Autowired
	NewsMonitoringMapper newsMonitoringMapper;
	
	private static String DEFAULTCRON = "0 0 9 ? * FRI *";
	private static String MININGCRON = "0 0 0 ? * FRI *";

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserModel user = userMapper.selectByLogin(username);
		if(user == null) {
			throw new UsernameNotFoundException("login fail");
		}else if(!user.getActive().equals(EnumActive.ACTIVE)) {
			throw new UsernameNotFoundException("not ativated user");
		}
		return new LoginUserDetails(user);
//		return new User(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
	}
	
	public Map<String, Object> join(UserModel um) {
		Map<String, Object> result = new HashMap<String, Object>();
		int state_ok = -1;
		try {
			um.setPassword(passwordEncoder.encode(um.getPassword()));
			um.setRole(EnumUserRole.USER);
			um.setActive(EnumActive.ACTIVE);
			um.setPasswordFailCnt(0);
			userMapper.insertUser(um);
			
			ScheduleModel textminingSchedule = new ScheduleModel();
			textminingSchedule.setCron(DEFAULTCRON);
			textminingSchedule.setUserId(um.getId());
			textminingSchedule.setType(EnumScheduleType.TEXTMINING);
			textminingSchedule.setNextTime(scheduleService.checkNextTime(DEFAULTCRON));
			scheduleService.insertTextminingSchedule (textminingSchedule);
			
			state_ok = 0;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "register falsed");
		}
		result.put("state_ok", state_ok);
		
		return result;
	}

	public UserModel selectUser(String email) {
		return userMapper.selectByLogin(email);
	}
	
	public UserModel selectUserById(Integer id) {
		return userMapper.selectUserById(id);
	}
	
	public List<UserModel> getAllAdmins() {
		return userMapper.getAllAdmins();
	}

	public List<UserModel> getUsers() {
		return userMapper.getUsers();
	}

	public Map<String, Object> myInfo() {
		Map<String, Object> result = new HashMap<String, Object>();
		int state_ok = -1;
		try {
			LoginUserDetails user = (LoginUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			result.put("data", selectUser(user.getEmail()));
			state_ok = 200;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", e.getMessage());
		}
		result.put("state_ok", state_ok);
		return result;
	}

	public Map<String, Object> saveMyInfo(UserModel um) {
		Map<String, Object> result = new HashMap<String, Object>();
		int state_ok = -1;
		try {
			UserModel user = selectUser(um.getEmail());
			user.setName(um.getName());
			user.setPhone(um.getPhone());
			
			userMapper.updateUser(user);
			state_ok = 200;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", e.getMessage());
		}
		result.put("state_ok", state_ok);
		return result;
	}

	public Map<String, Object> getUsersInfo(SearchBase search) {
		Map<String, Object> result = new HashMap<String, Object>();
		int state_ok = -1;
		try {
			LoginUserDetails user = (LoginUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			search.setUserId(user.getId());
			ResultList resultList = new ResultList(search);
			resultList.setResultList(userMapper.getUsersInfo(search));
			resultList.setTotalRecordCount(userMapper.getUsersInfoCount(search));
			result.put("data", resultList);
			state_ok = 200;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", e.getMessage());
		}
		result.put("state_ok", state_ok);
		return result;
	}

	public Map<String, Object> editUser(UserModel um) {
		Map<String, Object> result = new HashMap<String, Object>();
		int state_ok = -1;
		try {
			UserModel user = selectUser(um.getEmail());
			
			ScheduleModel adminSchedule = new ScheduleModel(); // only admin
			adminSchedule.setUserId(user.getId());
			adminSchedule.setType(EnumScheduleType.MONITORING);
			if(um.getRole().equals(EnumUserRole.ADMIN)) {
				ScheduleModel userSchedule = scheduleService.getUserTypeSchedule(adminSchedule);
				if(userSchedule == null) {
					adminSchedule.setCron(MININGCRON);
					adminSchedule.setNextTime(scheduleService.checkNextTime(MININGCRON)); //scheduleService.checkNextTime(DEFAULTCRON)
					scheduleService.insertMailSchedule(adminSchedule);					
				}
			}else {
				scheduleService.deleteMailScheduleUser(adminSchedule);
				List<Keyword> keyList = keywordService.selectByLogin(user.getId());
				for (Keyword k : keyList) {
					newsMonitoringMapper.deleteByKeywordId(k);
				}
			}
			
			user.setActive(um.getActive());
			user.setRole(um.getRole());
			userMapper.updateUser(user);
			state_ok = 200;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", e.getMessage());
		}
		result.put("state_ok", state_ok);
		return result;
	}
}
