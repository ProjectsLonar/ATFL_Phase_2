package com.eureka.zuul.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.eureka.zuul.SpringZuulApplication;
import com.eureka.zuul.common.LtVersion;

public class CheckVersionAPI {

	public static boolean checkAPIVersion(HttpServletRequest request, String XAPIVersion) {

		Map<String, LtVersion> apiversionMap = new HashMap<String, LtVersion>();

		apiversionMap = SpringZuulApplication.apiversionMap;

		String reuestURI = request.getRequestURI();

		for (Map.Entry<String, LtVersion> entry : SpringZuulApplication.apiversionMap.entrySet()) {

			if (reuestURI.toUpperCase().contains(entry.getKey().toUpperCase())) {
				
				LtVersion ltVersion=new LtVersion();
				
				ltVersion = entry.getValue();
				System.out.println(ltVersion);
				List<String> versionList = Arrays.asList(ltVersion.getSupported_version().split(",", -1));
				System.out.println(XAPIVersion);
				System.out.println(versionList);
				if (versionList.contains(XAPIVersion)) {
					System.out.println("API CHECK==TRUE");
					return true;
				} else {
					System.out.println("API CHECK==FALSE");
					return false;
				}
			}

		}

//		Set<String> paramNames = request.getParameterMap().keySet();
//
//		for (String name : paramNames) {
//			String value = request.getParameter(name);
//			System.err.println(value);
//		}

		return false;

	}

}
