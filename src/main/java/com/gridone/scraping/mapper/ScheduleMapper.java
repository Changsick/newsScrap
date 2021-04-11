package com.gridone.scraping.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gridone.scraping.model.ScheduleModel;


@Mapper
public interface ScheduleMapper {

	int insertMailSchedule(ScheduleModel mailSchedule);

	int insertTextminingSchedule(ScheduleModel textminingSchedule);

	int updateSchedule(ScheduleModel schedule);

	List<ScheduleModel> getCurrNextTime(String date);

}
