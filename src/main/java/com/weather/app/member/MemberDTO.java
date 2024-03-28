package com.weather.app.member;

import org.apache.ibatis.annotations.Mapper;

import lombok.Getter;
import lombok.Setter;

@Mapper
@Getter
@Setter
public class MemberDTO {

	private String numOfRows;
	private String pageNo;
	private String totalCount;
	private String resultCode;
	private String resultMsg;
	private String dataType;
	private String baseDate;
	private String baseTime;
	private String fcstDate;
	private String fcstTime;
	private String category;
	private String fcstValue;
	private String nx;
	private String ny;

}
