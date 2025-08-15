/**
 * SqlExecutor.java
 * 15-Aug-2025
 */
package com.abimulia.nlp.core;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.abimulia.nlp.core.dto.SqlPlan;

import lombok.extern.slf4j.Slf4j;

/**
 * @author abimu
 *
 * @version 1.0 (15-Aug-2025)
 * @since 15-Aug-2025 8:59:31 PM
 * 
 * 
 * Copyright(c) 2025 Abi Mulia
 */

@Component
@Slf4j
public class SqlExecutor {
	

	private final NamedParameterJdbcTemplate jdbc;
	private static final Pattern SELECT_ONLY = Pattern.compile("(?i)^\\s*select\\b[\\s\\S]*"); // longgar, hanya cek mulai dari SELECT

	
	/**
	 * @param jdbc
	 */
	public SqlExecutor(NamedParameterJdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	public List<Map<String,Object>> execute(SqlPlan plan, int maxRows) {
	    String sql = plan.sql();
	    log.debug("SQL : {}", sql);
	    if (sql == null || !SELECT_ONLY.matcher(sql).find())
	      throw new IllegalArgumentException("Only SELECT is allowed");
	    String safeSql = Pattern.compile("\\blimit\\b", Pattern.CASE_INSENSITIVE)
	    	    .matcher(sql)
	    	    .find() 
	    	    ? sql 
	    	    : sql + " limit " + Math.max(1, maxRows);
	    log.debug("safeSql : {}",safeSql);
	    try {
	      var params = new MapSqlParameterSource();
	      if (plan.params() != null) plan.params().forEach(params::addValue);
	      log.info("Executing SQL: {} | params={}", safeSql, plan.params());
	      return jdbc.queryForList(safeSql, params);
	    } catch (DataAccessException ex) {
	      log.warn("SQL error: {}", ex.getMessage());
	      throw ex;
	    }
	  }
	
	

}
