package com.example.tool_store.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutDetails {

    private String toolCode;
    private Integer rentalDays;
    private Double discount;
    private String checkoutDate;

    // create a user friendly readable string output of the checkout details
    @Override
    public String toString(){
        return "Tool Code: " + toolCode + ", Rental Days: " + rentalDays + ", Discount: " + discount + ", Checkout Date: " + checkoutDate;
    }

}
