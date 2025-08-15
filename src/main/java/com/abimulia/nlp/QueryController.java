/**
 * QueryController.java
 * 15-Aug-2025
 */
package com.abimulia.nlp;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abimulia.nlp.core.QueryOrchestrator;
import com.abimulia.nlp.core.dto.QueryRequest;
import com.abimulia.nlp.core.dto.QueryResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author abimu
 *
 * @version 1.0 (15-Aug-2025)
 * @since 15-Aug-2025 9:16:49 PM
 * 
 * 
 * Copyright(c) 2025 Abi Mulia
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class QueryController {
	


	private final QueryOrchestrator orchestrator;
	
	/**
	 * @param orchestrator
	 */
	public QueryController(QueryOrchestrator orchestrator) {
		this.orchestrator = orchestrator;
	}
	
	@GetMapping("/query")
	public QueryResponse query(@RequestParam String question,Integer maxRows) {
		log.debug("query : {}", question);
		return orchestrator.handle(question,maxRows);
	}
}
