/**
 * QueryResponse.java
 * 14-Aug-2025
 */
package com.abimulia.nlp.core.dto;

import java.util.List;
import java.util.Map;

/**
 * @author abimu
 *
 * @version 1.0 (14-Aug-2025)
 * @since 14-Aug-2025 10:28:09 PM
 * 
 * 
 *        Copyright(c) 2025 Abi Mulia
 */
public record QueryResponse(String question, String explanation, String sql, List<Map<String, Object>> rows,
		int rowCount) {

}
