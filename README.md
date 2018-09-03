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

