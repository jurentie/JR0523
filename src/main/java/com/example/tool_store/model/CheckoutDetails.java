package com.example.tool_store.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutDetails {

    @NotBlank(message = "Tool Code is Mandatory")
    @Size(min = 4, max = 4, message = "Tool Code should be 4 Characters. [CHNS, JAKD, JAKR, LADW]")
    private String toolCode;

    @NotNull(message = "Rental Days is Mandatory")
    @Min(value = 1, message = "Rental Days must be 1 or greater")
    private Integer rentalDays;

    @NotNull(message = "Discount is Mandatory")
    @Min(value = 0, message = "Discount should be a whole number between 0 - 100")
    @Max(value = 100, message = "Discount should be a whole number between 0 - 100")
    @Digits(integer = 2, fraction = 0, message = "Discount should be a whole number between 0 - 100")
    private Double discount;

    @NotBlank(message = "Checkout Date is Mandatory")
    @Pattern(regexp = "^(0?[1-9]|1[0-2])/(0?[1-9]|[12][0-9]|3[01])/(\\d{2})$", message = "Date must be in format mm/dd/yy, m/dd/yy, m/d/yy, mm/d/yy")
    private String checkoutDate;

    // create a user friendly readable string output of the checkout details
    @Override
    public String toString(){
        return "Tool Code: " + toolCode + ", Rental Days: " + rentalDays + ", Discount: " + discount + ", Checkout Date: " + checkoutDate;
    }

}
