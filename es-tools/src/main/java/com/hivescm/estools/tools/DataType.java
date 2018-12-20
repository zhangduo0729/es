package com.hivescm.estools.tools;

/**
 * elasticsearch 数据类型
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html
 * 
 * @author ygm
 */
public enum DataType {

	TEXT, KEYWORD,

	//https://blog.csdn.net/weizg/article/details/79269028
	DATE, IP, BOOLEAN,

	//number类型
	LONG, INTEGER, SHORT, BYTE, DOUBLE, FLOAT,

	//nested object mapping
	NESTED;
}