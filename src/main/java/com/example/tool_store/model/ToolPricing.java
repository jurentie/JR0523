package com.example.tool_store.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tools_pricing")
public class ToolPricing {

    @Id
    private String toolType;
    private Double dailyCharge;
    private Boolean weekdayCharge;
    private Boolean weekendCharge;
    private Boolean holidayCharge;
}
