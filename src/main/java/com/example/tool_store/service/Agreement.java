package com.example.tool_store.service;

import com.example.tool_store.model.CheckoutDetails;
import com.example.tool_store.model.Tool;
import com.example.tool_store.model.ToolPricing;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@Setter
public class Agreement {

    private String toolCode;
    private String toolType;
    private String toolBrand;
    private Integer rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private Double dailyRentalCharge;
    private Integer chargeDays;
    private Double preDiscountCharge;
    private Double discountPercent;
    private Double discountAmount;
    private Double finalCharge;

    public Agreement(CheckoutDetails checkoutDetails, Tool tool, ToolPricing toolPricing){
        this.toolCode = checkoutDetails.getToolCode();
        this.toolType = tool.getToolType();
        this.toolBrand = tool.getBrand();
        this.rentalDays = checkoutDetails.getRentalDays();
        this.checkoutDate = parseDateFromString(checkoutDetails.getCheckoutDate());
        this.dueDate = checkoutDate.plusDays(rentalDays);
        this.dailyRentalCharge = toolPricing.getDailyCharge();
        this.discountPercent = checkoutDetails.getDiscount() / 100;
        calculateChargeDays(toolPricing);
        this.preDiscountCharge = halfRoundUp(dailyRentalCharge * chargeDays);
        this.discountAmount = halfRoundUp(preDiscountCharge * discountPercent);
        this.finalCharge = preDiscountCharge - discountAmount;
    }

    public Agreement(){}

    // Get a LocalDate from the string input of a date
    private LocalDate parseDateFromString(String dateString){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("[MM][M]/[dd][d]/yy");
        return LocalDate.parse(dateString, dtf);
    }

    // Figure out how many days to charge the customer between the start and end date
    // We do not charge for the day the tool was rented on, but we do charge for the day it was returned.
    private void calculateChargeDays(ToolPricing toolPricing){
        LocalDate currDate = checkoutDate;
        Integer daysToCharge = 0;
        for(int i = 0; i < rentalDays; i++){
            currDate = currDate.plusDays(1);
            if (chargeOnDay(currDate, toolPricing))
                daysToCharge++;
        }
        this.chargeDays = daysToCharge;
    }

    // Helper method to determine if we should charge on a given day
    public Boolean chargeOnDay(LocalDate date, ToolPricing toolPricing){
        String day = date.getDayOfWeek().toString();

        Boolean dayOfWeekCharges = toolPricing.getWeekdayCharge();
        Boolean weekendCharges = toolPricing.getWeekendCharge();
        Boolean holidayCharges = toolPricing.getHolidayCharge();

        // If it's the fourth of july see if we charge for holidays
        if(formatDate(date).contains("07/04")){
            if(holidayCharges){
                return true;
            }
            return false;
        }
        // If it is labor day see if we charge for holidays
        else if((date.getMonth().toString().equals("SEPTEMBER") &&
                date.getDayOfMonth() <= 7 &&
                date.getDayOfWeek().toString().equals("MONDAY"))){
            if(holidayCharges){
                return true;
            }
            return false;
        }
        // If it is a mon - fri and we charge for days of the week return true
        else if((day.equals("MONDAY") ||
                day.equals("TUESDAY") ||
                day.equals("WEDNESDAY") ||
                day.equals("THURSDAY") ||
                day.equals("FRIDAY")) && dayOfWeekCharges
        ) {
            return true;
        }
        // If it is a weekend and we charge on weekends return true
        else if((day.equals("SATURDAY") || day.equals("SUNDAY")) && weekendCharges){
            return true;
        }

        return false;
    }

    // Use BigDecimal Half Round Up to round to cents
    public Double halfRoundUp(Double value){
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // Format date values
    public String formatDate(LocalDate date){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
        return date.format(dtf);
    }

    // Format currency values
    public String formatCurrency(Double amount){
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        return currencyFormatter.format(amount);
    }

    // Format percent values
    public String formatPercent(Double percent){
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        return percentFormat.format(percent);
    }

    // Custom toString to print the agreement per the instruction in the exercise
    @Override
    public String toString(){
        return  "Tool Code: " + toolCode + "\n" +
                "Tool Type: " + toolType + "\n" +
                "Tool Brand: " + toolBrand + "\n" +
                "Rental Days: " + rentalDays + "\n" +
                "Checkout Date: " + formatDate(checkoutDate) + "\n" +
                "Due Date: " + formatDate(dueDate) + "\n" +
                "Daily Rental Charge: " + formatCurrency(dailyRentalCharge) + "\n" +
                "Charge Days: " + chargeDays + "\n" +
                "Pre Discount Charge: " + formatCurrency(preDiscountCharge) + "\n" +
                "Discount Percent: " + formatPercent(discountPercent) + "\n" +
                "Discount Amount: " + formatCurrency(discountAmount) + "\n" +
                "Final Charge: " + formatCurrency(finalCharge);
    }

}
