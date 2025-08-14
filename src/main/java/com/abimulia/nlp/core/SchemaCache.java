/**
 * SchemaCache.java
 * 14-Aug-2025
 */
package com.abimulia.nlp.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author abimu
 *
 * @version 1.0 (14-Aug-2025)
 * @since 14-Aug-2025 10:43:23 PM
 * 
 * 
 * Copyright(c) 2025 Abi Mulia
 */
@Component
@Slf4j
public class SchemaCache {
	private final JdbcTemplate jdbc;
	private volatile String schemaSummary = "";
	
	public SchemaCache(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		refresh();
	}
	
	public String schemaSummary() {
		return schemaSummary;
	}
	
	@Scheduled(fixedDelay = 15 * 60 * 1000L, initialDelay = 30_000L)
	public void refresh() {
		log.info("Refreshing schema cache");
		String sql = """
				select table_name, column_name, data_type
				from information_schema.columns
				where table_schema = 'public'
				order by table_name, ordinal_position
				""";
		List<Map<String,Object>> rows = jdbc.queryForList(sql);
		var byTable = rows.stream()
				.collect(Collectors.groupingBy(r -> (String) r.get("table_name")));
		
		String summary = byTable.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.map(e -> e.getKey() + "(" + e.getValue().stream()
						.map(c -> c.get("column_name") + ":" + c.get("data_type"))
						.collect(Collectors.joining(", ")) + ")")
				.collect(Collectors.joining(""));
		log.debug("Schema summary: {}",summary);
		this.schemaSummary = summary;
		log.info("Schema cache loaded ({} tables)", byTable.size());
				
				
		
	}

}
