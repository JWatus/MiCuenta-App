## MiCuenta - Debt Management Application :money_with_wings: :credit_card: 

#### This API allows client application to connect and manage user's debts.

### Purpose

'MiCuenta' is an API which allows debtor to connect through the client application and then checkout 
his debts, make a payment plan and actualize payments. 

### Configuration 

This project use the following ports : 

| Server     | Port |
|------------|------|
| localhost  | 7000 |

### Database

This project uses embedded database H2. 

If You want to check database, there is a possibility to do it after running project and loading data.
Then You have to open link:

* [http://localhost:7000/h2](http://localhost:7000/h2)

And fill form with those values:

|   Property   | Value |
|------------|------|
| Driver Class:     | org.h2.Driver |
| JDBC URL:  | jdbc:h2:file:~/test |
| User Name:  | sa |
| Password:   | sa |

After doing this press Connect button.

### Assumptions

This API is intended to be connected with client application and external API with debtor's data. 
At this moment connection with external API is not implemented. For now example debtor is created
at initialization. Data of this debtor You can find in <i>DataCreator</i> class.

### Example client application

Example client application what can be used to check out this API is Alivio and You can find it at following address:
[https://github.com/JWatus/AlivioClient](https://github.com/JWatus/AlivioClient)

### How does it work?

Application operations in sequence are:

1. Through client application user is sending request with <i>Debtor</i> object. Next debtor is validated 
and depending on his presence in database returned is response with status: 200 if debtor is found or 404 
otherwise.

2. Next step is request for balance. Client is sending SSN number and API response with all debts 
off this debtor.

3. In third request client application is sending <i>PaymentDeclaration</i> object. API gets from this 
ID (if user has chosen specific debt to be paid) of chosen debt and amount of money which will be paid.
In response API is sending <i>PaymentPlan</i> based on received amount. This object has list of 
<i>PlannedPayments</i> and appropriate message for user - how repayment system works is described 
in the next paragraph.

4. In the end client sends <i>PaymentConfirmation</i> with <i>CreditCard</i> which will be used to pay. 
In response API will send status 200 if list of payments for chosen debts has been updated, 400 if 
payment amount was not valid or 404 if debt with chosen id does not exist.

If You want to reset database after initialization and making operations of it, just open link
 [http://localhost:7000/reset](http://localhost:7000/reset).

### How repayment system works?

- If user choose specific debt this debt will be paid. If amount of payment money will be greater 
than this debt, except chosen one, debts will be paid respectively from the oldest one as long as payment 
amount will be greater then zero.
- If user does not choose any debt, debts will be paid respectively from the oldest one from the beginning.
- If payment amount will be greater than sum of all debts, all of them will be paid instantly and proper 
message will be send to client.
- If there will be no more debts to be paid, user will get message about no more debts to be paid off.
 
### Connections

Client application can connect with MiCuenta API in one of two ways: through REST 
or in asynchronous way with JMS.

<b>REST</b>

Endpoints for REST communications with API are:
- Debtor validation: <i>/login</i>
- Balance request: <i>/balance/{ssn}</i>
- Sending payment declaration and getting payment plan: <i>/paymentplan</i>
- Sending payment confirmation with credit card: <i>/paymentmethods/creditcard</i>

<b>JMS</b>

For using Java Messaging Service (JMS) You need to deploy ActiveMQ on chosen port and set this connection 
in <i>MessagingConfiguration</i> class.<br>

API listeners are:
- Debtor validation: <i>jms.queue.login</i> queue 
- Balance request: <i>jms.queue.balance</i> queue 
- Sending payment declaration and getting payment plan: <i>jms.queue.paymentplan</i> queue 
- Sending payment confirmation with credit card: <i>jms.queue.paymentsupdate</i> queue 

Responses will be send to <i>jms.queue.x</i> where x is "client" StringProperty 
which should be set at client side and it should be name of client application.

For example in application named Alivio which send TextMessage to API setting property should be:
<i>textMessage.setStringProperty("client", "Alivio");</i>

API in response will set StringProperty for "endpoint". In order of usage those endpoints will be: 
login, balance, paymentplan and paymentsupdate.

### Swagger - API documentations

In this application Swagger was used to create API documentation.  

### Technology used to create and develop this application: 
- Java 8
- Spring Boot
- Spring Web Services
- Java Messaging Service
- Hibernate
- H2
- JUnit
- Swagger

### Enjoy :heavy_exclamation_mark:
