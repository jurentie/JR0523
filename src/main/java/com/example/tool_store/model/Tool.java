package com.example.tool_store.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tools")
public class Tool {

    @Id
    private String toolCode;
    private String toolType;
    private String brand;

}
