/**
 * JsonUtils.java
 * 15-Aug-2025
 */
package com.abimulia.nlp.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author abimu
 *
 * @version 1.0 (15-Aug-2025)
 * @since 15-Aug-2025 7:55:09 PM
 * 
 * 
 * Copyright(c) 2025 Abi Mulia
 */
public class JsonUtils {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public static <T> T read(String json, Class<T> type) {
		try {
			return objectMapper.readValue(json, type);
		} catch (Exception e) {
			throw new RuntimeException("Invalid JSON from LLM",e);
		}
	}

}
