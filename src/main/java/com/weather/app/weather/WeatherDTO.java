package com.weather.app.weather;

import org.apache.ibatis.annotations.Mapper;

import lombok.Getter;
import lombok.Setter;

@Mapper
@Getter
@Setter
public class WeatherDTO {

	private String obsrValue;
	private String baseDate;
	private String nx;
	private String ny;
	private String category;
	private String baseTime;


}
