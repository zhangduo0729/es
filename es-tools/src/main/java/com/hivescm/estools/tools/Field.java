package com.hivescm.estools.tools;

import java.util.HashMap;
import java.util.Map;

public class Field {

	/**
	 * 定义 fields 属性下的映射内容
	 */
	Map<String, Object> keyword = new HashMap<>();

	public static Field make() {
		return new Field();
	}

	public Field setType(DataType type) {
		keyword.put("type", type.name().toLowerCase());
		return this;
	}

	/**
	 * 忽略字符长度ignore_above以外的字符，不被索引
	 * @param ignore_above
	 * @return
	 */
	public Field setIgnoreAbove(int ignore_above) {
		keyword.put("ignore_above", ignore_above);
		return this;
	}

	/**
	 * 超过256个字符的文本，将会被忽略，不被索引
	 * @return
	 */
	public Field setIgnoreAbove() {
		keyword.put("ignore_above", 256);
		return this;
	}

	public Map<String, Object> getKeyword() {
		return keyword;
	}

	public void setKeyword(Map<String, Object> keyword) {
		this.keyword = keyword;
	}
}