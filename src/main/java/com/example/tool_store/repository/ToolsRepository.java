package com.example.tool_store.repository;

import com.example.tool_store.model.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolsRepository extends JpaRepository<Tool, Long> {

    @Query(value = "SELECT * FROM tools WHERE tool_code = ?1", nativeQuery = true)
    Tool getToolByCode(String code);
}
