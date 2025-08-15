/**
 * SqlPlanner.java
 * 15-Aug-2025
 */
package com.abimulia.nlp.core;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import com.abimulia.nlp.core.dto.SqlPlan;
import com.abimulia.nlp.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author abimu
 *
 * @version 1.0 (15-Aug-2025)
 * @since 15-Aug-2025 5:45:58 PM
 * 
 * 
 * Copyright(c) 2025 Abi Mulia
 * 
 */

@Service
@Slf4j
public class SqlPlanner {
	
	private final ChatClient chatClient;
	private final SchemaCache schema;
	/**
	 * @param chatClient
	 * @param schema
	 */
	public SqlPlanner(ChatClient.Builder builder, SchemaCache schema) {
		log.debug("SqlPlanner() : {}",  schema);
		this.chatClient = builder.defaultSystem(SYSTEM).build();
		this.schema = schema;
	}
	
	private static final String SYSTEM = """
			You are a senior data analyst. Convert the user question into a **single, safe PostgreSQL SELECT**.
			Rules:
			- Use only the provided schema and exact table/column names.
			- Never modify data; **SELECT only**. No DDL/DML.
			- Prefer ANSI SQL. Parameterize literals using :named parameters when appropriate.
			- If the question isn't answerable from the schema, explain why and propose the closest alternative.
			- Always output SQL with numeric LIMIT values, not parameters.
			
			Output strictly as JSON with fields: sql(String), params (object), explanation(String).
			""";
	
	private static final String USER_TMPL = """
			Schema:
			{schema}
			
			Question: {question}
			Return JSON only
			""";
	
	public SqlPlan plan(String question) {

		
		// 1. Buat prompt langsung jadi String
	    var prompt = new PromptTemplate(USER_TMPL)
	            .create(Map.of(
	                "schema", schema.schemaSummary(),
	                "question", question
	            )).getUserMessages()
	            .get(0)
	            .getText();
	    
	    log.debug("prompt: {}", prompt);
	    // 2. Kirim prompt ke LLM
	    String jsonOutput = chatClient.prompt()
	            .user(prompt)
	            .call()
	            .content();
	    
	    log.debug("LLM raw response: {}", jsonOutput);
	    String cleanJson = jsonOutput
	            .replaceAll("(?s)```json\\s*(.*?)\\s*```", "$1") // ambil isi di dalam blok ```json
	            .trim();

	    log.debug("Cleaned JSON: {}", cleanJson);
	    
	    // 3. Parsing JSON ke SqlPlan
	    return JsonUtils.read(cleanJson, SqlPlan.class);

	
	}
	
	

}
