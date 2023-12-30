package com.eureka.zuul.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Configuration  {
	
	public static Map<String, LtMastUsers> inactiveUserMap = new HashMap<String, LtMastUsers>();
	
	
}
