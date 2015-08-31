package com.sc.util;


public class Constants {
	
	public static final String separator = "/";
	public static String CurrentDir = Constants.class.getResource("/").getPath();

	public static final String DefaultConfigFile = CurrentDir + separator + "default.properties";

	public static final String LocationCollectionName = "location";
	public static final String OrderCollectionName = "order";


}
