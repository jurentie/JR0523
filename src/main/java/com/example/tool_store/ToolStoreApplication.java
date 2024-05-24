package com.example.tool_store;

import com.example.tool_store.controller.ToolsController;
import com.example.tool_store.model.CheckoutDetails;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ToolStoreApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(ToolStoreApplication.class, args);
		programStart(applicationContext);
	}


	public static void programStart(ApplicationContext applicationContext){

		//Manual run of tools controller to demonstrate the API behavior
		ToolsController controller = applicationContext.getBean(ToolsController.class);
		CheckoutDetails checkoutDetails = new CheckoutDetails();
		checkoutDetails.setToolCode("LADW");
		checkoutDetails.setCheckoutDate("7/2/20");
		checkoutDetails.setRentalDays(5);
		checkoutDetails.setDiscount(10.0);
		String agreement = controller.getAgreement(checkoutDetails);

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Sample Output from the API:");
		System.out.println("-----------------------------------------------------------------------------------------");
		System.out.println("Create checkout details:");
		System.out.println(checkoutDetails);
		System.out.println();
		System.out.println("Agreement:");
		System.out.println(agreement);
	}

}
