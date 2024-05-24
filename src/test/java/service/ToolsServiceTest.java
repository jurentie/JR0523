package service;


import com.example.tool_store.model.CheckoutDetails;
import com.example.tool_store.model.ToolPricing;
import com.example.tool_store.model.Tool;
import com.example.tool_store.repository.ToolPricingRepository;
import com.example.tool_store.repository.ToolsRepository;
import com.example.tool_store.service.Agreement;
import com.example.tool_store.service.ToolsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ToolsService.class)
public class ToolsServiceTest {

    @Autowired
    ToolsService toolsService;

    @MockBean
    ToolsRepository toolsRepository;

    @MockBean
    ToolPricingRepository toolPricingRepository;

    private String loadResourceAsString(String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }

    private String normalizeLineSeparators(String string){
        return string.replaceAll("\\r\\n", "\n");
    }

    // Test #2
    @Test
    public void getAgreementOnCheckout_returnsAgreement_test1() throws Exception{
        String expectedResponse = loadResourceAsString("ExpectedResponse1");

        CheckoutDetails checkoutDetails = new CheckoutDetails();
        checkoutDetails.setToolCode("LADW");
        checkoutDetails.setCheckoutDate("7/2/20");
        checkoutDetails.setDiscount(10.0);
        checkoutDetails.setRentalDays(3);

        Tool tool = new Tool();
        tool.setToolCode("LADW");
        tool.setToolType("Ladder");
        tool.setBrand("Werner");

        ToolPricing toolPricing = new ToolPricing();
        toolPricing.setToolType("Ladder");
        toolPricing.setDailyCharge(1.99);
        toolPricing.setWeekdayCharge(true);
        toolPricing.setWeekendCharge(true);
        toolPricing.setHolidayCharge(false);

        when(toolsRepository.getToolByCode("LADW")).thenReturn(tool);
        when(toolPricingRepository.getToolPricingByToolType("Ladder")).thenReturn(toolPricing);

        Agreement result = toolsService.getAgreementOnCheckout(checkoutDetails);

        assertEquals(normalizeLineSeparators(expectedResponse), normalizeLineSeparators(result.toString()));
    }

    // Test #3
    @Test
    public void getAgreementOnCheckout_returnsAgreement_test2() throws Exception{
        String expectedResponse = loadResourceAsString("ExpectedResponse2");

        CheckoutDetails checkoutDetails = new CheckoutDetails();
        checkoutDetails.setToolCode("CHNS");
        checkoutDetails.setCheckoutDate("7/2/15");
        checkoutDetails.setDiscount(25.0);
        checkoutDetails.setRentalDays(5);

        Tool tool = new Tool();
        tool.setToolCode("CHNS");
        tool.setToolType("Chainsaw");
        tool.setBrand("Stihl");

        ToolPricing toolPricing = new ToolPricing();
        toolPricing.setToolType("Chainsaw");
        toolPricing.setDailyCharge(1.49);
        toolPricing.setWeekdayCharge(true);
        toolPricing.setWeekendCharge(false);
        toolPricing.setHolidayCharge(true);

        when(toolsRepository.getToolByCode("CHNS")).thenReturn(tool);
        when(toolPricingRepository.getToolPricingByToolType("Chainsaw")).thenReturn(toolPricing);

        Agreement result = toolsService.getAgreementOnCheckout(checkoutDetails);

        assertEquals(normalizeLineSeparators(expectedResponse), normalizeLineSeparators(result.toString()));
    }

    // Test #4
    @Test
    public void getAgreementOnCheckout_returnsAgreement_test3() throws Exception{
        String expectedResponse = loadResourceAsString("ExpectedResponse3");

        CheckoutDetails checkoutDetails = new CheckoutDetails();
        checkoutDetails.setToolCode("JAKD");
        checkoutDetails.setCheckoutDate("9/3/15");
        checkoutDetails.setDiscount(0.0);
        checkoutDetails.setRentalDays(6);

        Tool tool = new Tool();
        tool.setToolCode("JAKD");
        tool.setToolType("Jackhammer");
        tool.setBrand("DeWalt");

        ToolPricing toolPricing = new ToolPricing();
        toolPricing.setToolType("Jackhammer");
        toolPricing.setDailyCharge(2.99);
        toolPricing.setWeekdayCharge(true);
        toolPricing.setWeekendCharge(false);
        toolPricing.setHolidayCharge(false);

        when(toolsRepository.getToolByCode("JAKD")).thenReturn(tool);
        when(toolPricingRepository.getToolPricingByToolType("Jackhammer")).thenReturn(toolPricing);

        Agreement result = toolsService.getAgreementOnCheckout(checkoutDetails);

        assertEquals(normalizeLineSeparators(expectedResponse), normalizeLineSeparators(result.toString()));
    }

    // Test #5
    @Test
    public void getAgreementOnCheckout_returnsAgreement_test4() throws Exception{
        String expectedResponse = loadResourceAsString("ExpectedResponse4");

        CheckoutDetails checkoutDetails = new CheckoutDetails();
        checkoutDetails.setToolCode("JAKR");
        checkoutDetails.setCheckoutDate("7/2/15");
        checkoutDetails.setDiscount(0.0);
        checkoutDetails.setRentalDays(9);

        Tool tool = new Tool();
        tool.setToolCode("JAKR");
        tool.setToolType("Jackhammer");
        tool.setBrand("Ridgid");

        ToolPricing toolPricing = new ToolPricing();
        toolPricing.setToolType("Jackhammer");
        toolPricing.setDailyCharge(2.99);
        toolPricing.setWeekdayCharge(true);
        toolPricing.setWeekendCharge(false);
        toolPricing.setHolidayCharge(false);

        when(toolsRepository.getToolByCode("JAKR")).thenReturn(tool);
        when(toolPricingRepository.getToolPricingByToolType("Jackhammer")).thenReturn(toolPricing);

        Agreement result = toolsService.getAgreementOnCheckout(checkoutDetails);

        assertEquals(normalizeLineSeparators(expectedResponse), normalizeLineSeparators(result.toString()));
    }

    // Test #6
    @Test
    public void getAgreementOnCheckout_returnsAgreement_test5() throws Exception{
        String expectedResponse = loadResourceAsString("ExpectedResponse5");

        CheckoutDetails checkoutDetails = new CheckoutDetails();
        checkoutDetails.setToolCode("JAKR");
        checkoutDetails.setCheckoutDate("7/2/20");
        checkoutDetails.setDiscount(50.0);
        checkoutDetails.setRentalDays(4);

        Tool tool = new Tool();
        tool.setToolCode("JAKR");
        tool.setToolType("Jackhammer");
        tool.setBrand("Ridgid");

        ToolPricing toolPricing = new ToolPricing();
        toolPricing.setToolType("Jackhammer");
        toolPricing.setDailyCharge(2.99);
        toolPricing.setWeekdayCharge(true);
        toolPricing.setWeekendCharge(false);
        toolPricing.setHolidayCharge(false);

        when(toolsRepository.getToolByCode("JAKR")).thenReturn(tool);
        when(toolPricingRepository.getToolPricingByToolType("Jackhammer")).thenReturn(toolPricing);

        Agreement result = toolsService.getAgreementOnCheckout(checkoutDetails);

        assertEquals(normalizeLineSeparators(expectedResponse), normalizeLineSeparators(result.toString()));
    }

}
