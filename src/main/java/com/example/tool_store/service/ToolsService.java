package com.example.tool_store.service;

import com.example.tool_store.model.CheckoutDetails;
import com.example.tool_store.model.Tool;
import com.example.tool_store.model.ToolPricing;
import com.example.tool_store.repository.ToolPricingRepository;
import com.example.tool_store.repository.ToolsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToolsService {

    @Autowired
    private ToolsRepository toolsRepository;

    @Autowired
    private ToolPricingRepository toolPricingRepository;

    public Tool getToolByCode(String code) {
        return toolsRepository.getToolByCode(code);
    }

    public ToolPricing getToolPricingByToolType(String toolType) {
        return toolPricingRepository.getToolPricingByToolType(toolType);
    }

    // From checkout details use the information provided to retrieve the Tool, and ToolPricing and then
    // use those to create a new Agreement
    public Agreement getAgreementOnCheckout(CheckoutDetails checkoutDetails){
        Tool tool = toolsRepository.getToolByCode(checkoutDetails.getToolCode());
        ToolPricing toolPricing = toolPricingRepository.getToolPricingByToolType(tool.getToolType());
        return new Agreement(checkoutDetails, tool, toolPricing);
    }

}
