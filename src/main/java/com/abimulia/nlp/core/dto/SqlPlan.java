/**
 * SqlPlan.java
 * 14-Aug-2025
 */
package com.abimulia.nlp.core.dto;

import java.util.Map;

/**
 * @author abimu
 *
 * @version 1.0 (14-Aug-2025)
 * @since 14-Aug-2025 10:34:59 PM
 * 
 * 
 * Copyright(c) 2025 Abi Mulia
 */
public record SqlPlan(String sql, Map<String,Object> params, String explanation) {

}
