package com.example.tool_store.controller;

import com.example.tool_store.model.CheckoutDetails;
import com.example.tool_store.model.Tool;
import com.example.tool_store.model.ToolPricing;
import com.example.tool_store.service.ToolsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tool-store")
public class ToolsController {

    @Autowired
    private ToolsService toolsService;

    // get a string representation of the agreement
    @GetMapping(value = "/checkout", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getAgreement(@Valid @RequestBody CheckoutDetails checkoutDetails){
        return toolsService.getAgreementOnCheckout(checkoutDetails).toString();
    }

    // get a single tool from the tool code
    @GetMapping("/tool/{toolCode}")
    public Tool getTool(@PathVariable String toolCode){
        return toolsService.getToolByCode(toolCode);
    }

    // get tool pricing from tool type
    @GetMapping("/tool/pricing/{toolType}")
    public ToolPricing getToolPricingByToolType(@PathVariable String toolType){
        return toolsService.getToolPricingByToolType(toolType);
    }
}
