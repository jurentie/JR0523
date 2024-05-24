package com.example.tool_store.repository;

import com.example.tool_store.model.ToolPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolPricingRepository extends JpaRepository<ToolPricing, Long> {

    @Query(value = "SELECT * FROM tools_pricing WHERE tool_type = ?1", nativeQuery = true)
    ToolPricing getToolPricingByToolType(String toolType);
}
