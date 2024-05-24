# Tool Store #

My approach to solving this problem:

When I first read through the requirements of this coding exercise my mind immediately went to microservice APIs. I have spent much of my professional career working in microservices and APIs using java and spring boot, and so it seemed like a good place to start as my approach for solving the ask of this coding exercise.

I wanted to demonstrate an approach I could see being implemented in a large scale production environment. The benefit of creating an API is that it is widely used across the technology industry as a way to build web-applications and offer ease of scalability. With this approach it allows the user or developer to get tools, compute the necessary agreement information as it relates to the tools at the tool store, and offers potential such as adding new tools or new tool pricing options.

### Step 1: Create a Database for Tool Data ###

In the real world a microservice such as this one would rely on maintaining data in a dynamic way, typically in a database. I decided to approach this solution in the same way, by creating a free AWS RDS called `tools-store-db`. I then connected to this database using PgAdmin4 and created two tables to store our data, `tools` and `tools_pricing`, and added our default data.

```sql
INSERT INTO tools (tool_code, tool_type, brand) VALUES
('CHNS', 'Chainsaw', 'Stihl'),
('LADW', 'Ladder', 'Werner'),
('JAKD', 'Jackhammer', 'DeWalt'),
('JAKR', 'Jackhammer', 'Ridgid');
```

| tool_code | tool_type  | brand  |
| :-------- | ---------- | :----- |
| CHNS      | Chainsaw   | Stihl  |
| LADW      | Ladder     | Werner |
| JAKD      | Jackhammer | DeWalt |
| JAKR      | Jackhammer | Ridgid |

```sql
INSERT INTO tools_pricing (tool_type, daily_charge, weekday_charge, weekend_charge, holiday_charge) VALUES
('Ladder', 1.99, true, true, false),
('Chainsaw', 1.49, true, false, true),
('Jackhammer', 2.99, true, false, false);
```

| tool_type  | daily_charge | weekday_charge | weekend_charge | holiday_charge |
| ---------- | ------------ | -------------- | -------------- | -------------- |
| Ladder     | 1.99         | true           | true           | false          |
| Chainsaw   | 1.49         | true           | false          | true           |
| Jackhammer | 2.99         | true           | false          | false          |

### Step 2: Setup my Java Springboot Project ###

For efficiency in getting this java application up and running, I chose to utilize [Spring Initializr](https://start.spring.io/).  When considering which Java version to use I chose to go with Java 17 as this seems to be the most commonly used, up to date version of Java being implemented in large scale production. I wanted to consider how I intended to build this project, and I chose to go with Gradle as this is something I have the most experience with in a professional context. I initially decided to start only with the spring boot starter web dependencies and to add dependencies as needed. After selecting these default configurations I exported my new java spring boot application to my local machine. I ran a quick hello world test to ensure the microservice API was ready to go and then I was ready to get to work.

After testing the service I needed to add configuration in order to connect to `tool-store-db` being hosted on AWS. First I had to add the necessary dependencies to my `build.gradle` file including `org.springframework.boot:spring-boot-starter-data-jpa` and `org.postgresql:postgresql` and then configure my `application.properties`. In a production environment it would be best to set these properties using environment variables instead of hardcoding and pushing to a public repository, but for the sake of this exercise I was going to let that slide

I ran into some struggles getting the project running after adding the database configuration but luckily found a solution [here](https://github.com/spring-projects/spring-lifecycle-smoke-tests/tree/main/data/data-jpa) which helped me get my properties in the correct state.

Now it was time to start implementing some of the endpoints I would need for this API. As I began creating my first entity class to hold the data of a Tool object, I decided to add lombok to my dependencies to avoid having to manually write out all of my getters and setters, also in the interest of saving time. After implementing the necessary classes `Tool`, `ToolsRepository`, `ToolsService`, and `ToolsController`, I ran an intial test by hitting the following endpoint in Postman to get the following output:

```
GET http://localhost:8080/api/tool-store/tool/:toolCode
---
{
    "toolCode": "CHNS",
    "toolType": "Chainsaw",
    "brand": "Stihl"
}
```

Now I was ready to fully implement this microservice to achieve the goal of the coding exercise.

### Step 3: Implement the Required Functionality of Tools Service ###

My next step was to implement all of the same necessary files to create an endpoint to get tool pricing by tool type. Now that I had access to both the tool by tool code and the tool pricing by tool type I was ready to start adding some logic for handling the agreement upon rental, and this is when I got into the bulk of the code necessary to achieve the requirements of this coding exercise. 

*It's worth noting that in a production environment it would be pertinent to add some kind of authorization to this API, but for the sake of this exercise I chose not to do so.*

I decided the best approach to this was to try and practice the idea of encapsulation by creating an Agreement class on it's own that will handle all the logic necessary to initialize the agreement with the correct data. I decided that I would initialize all the data I had available to me in the constructor of the agreement class and then create helper methods to parse and calculate the relevant properties that needed some additional logic.

The first hurdle to cross was a robust way of handling dates. I am quite familiar with java's `LocalDate` object first introduced in Java version 1.8. I used a `DateTimeFormatter` to parse a date passed into the API as a string in any of the following formats (mm/dd/yy, m/dd/yy, mm/d/yy, m/d/yy) into a `LocalDate` object.  Then, checking back with the coding exercise instructions I created a few helper methods to format the date, as well as percentage, and currency values in the format asked for by utilizing `DateTimeFormatter` and `NumberFormat`.

The next task I sought to tackle was the issue of half rounding up any of the currency values that I was working with. I scoured the internet to find various solutions to this problem and came across `BigDecimal` as a solution to this. `BigDecimal` has a method called `setScale` that can take a few arguments including the decimal places we want to round to, and the rounding strategy you want to implement so by passing in any currency values after calculation I was able to ensure that the output would be rounded half up.

```java
    // Use BigDecimal Half Round Up to round to cents
    public Double halfRoundUp(Double value){
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
```

Now that I had all of the formatting and conversions out of the way it was time to address the bulk of the logic that would be needed to complete the agreement, and that involved calculating the chargeable days for any given tool, based on whether or not the tool was specified to be chargeable on weekdays, weekends, or holidays. This method is probably where I spent most of my time and I got it working through a series of `if/else if` conditions that appropriately check if the current date matches any of the chargeable criteria specified in our tool pricing table and then returning a boolean of true or false in each case. After getting that logic sorted I was able to parse through each date from the day after start date (as mentioned in the instructions) up to and including the return date and set chargeable days upon meeting the conditions of `chargeOnDay()` or not. 

*Another thing to note is that I have made sure to create a solution that allows scalability when working with tools and tools pricing, but it would be equally as important to consider implementing a way to allow the same scalability for the holidays that the tool store recognizes*

### Step 3: Input Validation ###

After completing all the logic necessary for the Agreement class I wanted to address the issue of user input validation. For this I added `org.springframework.boot:spring-boot-starter-validation` to my `gradle.build` file to allow me to utilize spring boot validation annotations to check the input of the request body being passed to my `/agreement` API. I decided to add some extra validation on all the passed in variables, but made sure to include the requested check of percent being a whole number between 0 - 100 as well as rental days being 1 or greater. I did not decide to add input validation to my other endpoints, as these were mostly created for testing purposes anyway, and in an interest of saving time.

### Step 4: Testing ###

With my API up and running and working as I expected it to, as validated by running manual tests in postman, I was ready to begin writing my tests for my java microservice API. The exercise specifies that I was to test specific cases using JUnit testing. I am aware that these tests are written using the spring boot starter test, however this library includes JUnit testing and so I believe that these tests are written in a way that demonstrate my skill at writing tests specifically for a spring boot API but also demonstrate my knowledge with JUnit testing as a whole. In some cases I ensured I was testing the output using `assertEquals()` instead of using other validation methods provided from spring boot testing libraries.

I ensure that all six test cases, as well as some additional tests, were provided in my two test classes `ToolsControllerTest` and `ToolsServiceTest`. Since user input validation was being done at the controller level Test #1 is found in `ToolsControllerTest.getAgreement_returnsErrorMessage_discountIsGreaterThan100()` by passing a discount percent greater than 100. The remaining 5 tests can be found in `ToolsServiceTest` as I tested that the agreement was being configured appropriately with all the given test data. 

### Step 5: Print Output to the Console ###

The thing I kept coming back to as I was creating this java service was the final ask of the exercise which specifies:

> Rental Agreement should include a method that can print the above values as text to the console like this: 
>
> Tool code: LADW 
>
>Tool type: Ladder 
>
> … 
> 
> Final charge: $9.99 
>
> with formatting as follows: 
> 
>
>   ● Date mm/dd/yy 
> 	
>   ● Currency $9,999.99 
> 	
>   ● Percent 99%

While I felt that my solution met all of the required specifications I wanted to ensure that my code ultimately does what was being asked and print the output to the console. To achieve this I created an `ApplicationContext` object that would allow me to access the API manually from inside my main java method and then call another method to feed some data into the the controller class and output the agreement to the console. It is my hope that by taking this approach I am not overlooking the basic requirements of this exercise and am instead going above and beyond as I would imagine something like this to be accomplished in an actual business context. 

To better help someone run and test my java solution I am adding some API documentation below.

## API Documentation #

<details>
 <summary><code>GET</code> <code><b>/api/tool-store/checkout</b></code> <code>(returns a text output of the agreement)</code></summary>

##### Parameters

> None

##### Request Body

> Required: true
> 
> content-type: application/json
> 
> Example:
> 
>  ```
> {
>   "toolType": "LADW",
>   "checkoutDate": "7/2/20",
>   "rentalDays": 3,
>   "discount": 10
> }

##### Successful Response

> content-type: text/plain
> 
> Example:
> 
> ```
> Tool Code: LADW
> Tool Type: Ladder
> Tool Brand: Werner
> Rental Days: 3
> Checkout Date: 07/02/20
> Due Date: 07/05/20
> Daily Rental Charge: $1.99
> Charge Days: 2
> Pre Discount Charge: $3.98
> Discount Percent: 10%
> Discount Amount: $0.40
> Final Charge: $3.58

##### Example cURL

> ```javascript
>  curl --location --request GET 'http://localhost:8080/api/tool-store/checkout' \
> --header 'Content-Type: application/json' \
> --data '{
>     "toolCode": "LADW",
>     "checkoutDate": "7/2/20",
>    "rentalDays": 3,
>     "discount": 10
> }'
</details>

<details>
 <summary><code>GET</code> <code><b>/api/tool-store/tool/:toolCode</b></code> <code>(returns a tool by tool code)</code></summary>

##### Parameters

> | name       |  type     | data type | description                                 | 
> |------------|-----------|-----------|---------------------------------------------|
> | `toolCode` |  required | string    | The four character long tool code. ex: CHNS |


##### Request Body

> None

##### Successful Response

> content-type: application/json
> 
> Example:
> 
> ```
> {
>     "toolCode": "CHNS",
>     "toolType": "Chainsaw",
>     "brand": "Stihl"
> }

##### Example cURL

> ```javascript
>  curl --location 'http://localhost:8080/api/tool-store/tool/CHNS'

</details>

<details>
 <summary><code>GET</code> <code><b>/api/tool-store/tool/pricing/:toolType</b></code> <code>(returns a tool pricing by tool type)</code></summary>

##### Parameters

> | name       |  type     | data type | description                   | 
> |------------|-----------|-----------|-------------------------------|
> | `toolType` |  required | string    | The tool type. ex: "Chainsaw" |


##### Request Body

> None

##### Successful Response

> content-type: application/json
> 
> Example:
> 
> ```
> {
>     "toolType": "Chainsaw",
>     "dailyCharge": 1.49,
>     "weekdayCharge": true,
>     "weekendCharge": false,
>     "holidayCharge": true
> }

##### Example cURL

> ```javascript
>  curl --location 'http://localhost:8080/api/tool-store/tool/pricing/Chainsaw'

</details>