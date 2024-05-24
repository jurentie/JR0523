package com.example.tool_store.controller;

import com.example.tool_store.model.CheckoutDetails;
import com.example.tool_store.model.Tool;
import com.example.tool_store.model.ToolPricing;
import com.example.tool_store.service.Agreement;
import com.example.tool_store.service.ToolsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = ToolsController.class)
@ExtendWith(MockitoExtension.class)
public class ToolsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ToolsService toolsService;

    @Autowired
    private ObjectMapper objectMapper;

    private String loadResourceAsString(String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }

    private String normalizeLineSeparators(String string){
        return string.replaceAll("\\r\\n", "\n");
    }

    @Test
    public void getAgreement_returnsAgreement() throws Exception {
        String expectedResponse = loadResourceAsString("ExpectedResponse1");

        CheckoutDetails checkoutDetails;
        checkoutDetails = new CheckoutDetails();
        checkoutDetails.setToolCode("LADW");
        checkoutDetails.setCheckoutDate("7/2/20");
        checkoutDetails.setDiscount(10.0);
        checkoutDetails.setRentalDays(3);

        Tool tool;
        tool = new Tool();
        tool.setToolCode("LADW");
        tool.setToolType("Ladder");
        tool.setBrand("Werner");

        ToolPricing toolPricing;
        toolPricing = new ToolPricing();
        toolPricing.setToolType("Ladder");
        toolPricing.setDailyCharge(1.99);
        toolPricing.setWeekdayCharge(true);
        toolPricing.setWeekendCharge(true);
        toolPricing.setHolidayCharge(false);

        Agreement agreement = new Agreement(checkoutDetails, tool, toolPricing);

        when(toolsService.getAgreementOnCheckout(any(CheckoutDetails.class))).thenReturn(agreement);

        MvcResult result = mockMvc.perform(get("/api/tool-store/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(checkoutDetails)))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(normalizeLineSeparators(expectedResponse), normalizeLineSeparators(content));
    }

    @Test
    public void getAgreement_returnsErrorMessage_rentalDaysLessThan1() throws Exception{
        String expectedResponse = "{\"rentalDays\":\"Rental Days must be 1 or greater\"}";

        CheckoutDetails badCheckoutDetails = new CheckoutDetails();
        badCheckoutDetails.setToolCode("LADW");
        badCheckoutDetails.setCheckoutDate("7/2/20");
        badCheckoutDetails.setDiscount(10.0);
        badCheckoutDetails.setRentalDays(0);

        MvcResult result = mockMvc.perform(get("/api/tool-store/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badCheckoutDetails)))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(expectedResponse, content);

    }

    @Test
    public void getAgreement_returnsErrorMessage_discountIsGreaterThan100() throws Exception{
        String expectedResponse = "{\"discount\":\"Discount should be a whole number between 0 - 100\"}";

        CheckoutDetails badCheckoutDetails = new CheckoutDetails();
        badCheckoutDetails.setToolCode("JAKR");
        badCheckoutDetails.setCheckoutDate("9/3/15");
        badCheckoutDetails.setDiscount(101.0);
        badCheckoutDetails.setRentalDays(5);

        MvcResult result = mockMvc.perform(get("/api/tool-store/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badCheckoutDetails)))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(expectedResponse, content);

    }

    // Test #1
    @Test
    public void getAgreement_returnsErrorMessage_discountIsLessThan100() throws Exception{
        String expectedResponse = "{\"discount\":\"Discount should be a whole number between 0 - 100\"}";

        CheckoutDetails badCheckoutDetails = new CheckoutDetails();
        badCheckoutDetails.setToolCode("LADW");
        badCheckoutDetails.setCheckoutDate("7/2/20");
        badCheckoutDetails.setDiscount(-1.0);
        badCheckoutDetails.setRentalDays(1);

        MvcResult result = mockMvc.perform(get("/api/tool-store/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badCheckoutDetails)))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(expectedResponse, content);

    }

}
