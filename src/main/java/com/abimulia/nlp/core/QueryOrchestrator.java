/**
 * QueryOrchestrator.java
 * 15-Aug-2025
 */
package com.abimulia.nlp.core;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abimulia.nlp.core.dto.QueryResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author abimu
 *
 * @version 1.0 (15-Aug-2025)
 * @since 15-Aug-2025 9:08:00 PM
 * 
 * 
 * Copyright(c) 2025 Abi Mulia
 */

@Service
@Slf4j
public class QueryOrchestrator {
	

	private final SqlPlanner planner;
	private final SqlExecutor executor;
	
	/**
	 * @param planner
	 * @param executor
	 */
	public QueryOrchestrator(SqlPlanner planner, SqlExecutor executor) {
		this.planner = planner;
		this.executor = executor;
	}
	
	public QueryResponse handle(String question, Integer maxRows) {
		log.debug("question : {}",question);
		log.debug("maxRows: {}",maxRows);
		var plan = planner.plan(question);
		log.debug("SqlPlan : {}",plan);
		int limit = (maxRows == null? 200 : Math.min(1000, Math.max(1, maxRows)));
		List<Map<String, Object>> rows = executor.execute(plan, limit);
		return new QueryResponse(question, plan.explanation(), plan.sql(), rows, rows.size());
	}

}
