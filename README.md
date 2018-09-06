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
at initialization. Data of this debtor You can find in DataCreator class.

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

4. In the end client sends <i>PaymentConfirmation</i> with CreditCard which will be used to pay. In response
API will send status 200 if list of payments for chosen debts has been updated, 400 if payment amount was not
valid or 404 if debt with chosen id does not exist.

If You want to reset database after initialization and making operations of it, just open link
 [http://localhost:7000/reset](http://localhost:7000/reset).



