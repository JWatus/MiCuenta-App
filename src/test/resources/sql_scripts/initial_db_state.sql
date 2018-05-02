DELETE FROM CREDIT_CARD;
DELETE FROM PAYMENT;
DELETE FROM DEBT;
DELETE FROM DEBTOR;

INSERT INTO CREDIT_CARD VALUES('98978872537125', '109', 'Jakub', 'Watus');
INSERT INTO CREDIT_CARD VALUES('23457590909018', '235', 'Jakub', 'Watus');

   CreditCard creditCardOne = new CreditCard("98978872537125", "109", "Jakub", "Watus");
        CreditCard creditCardTwo = new CreditCard("23457590909018", "235", "Jakub", "Watus");

        Payment paymentOne = new Payment(LocalDate.now(), BigDecimal.valueOf(500.00), creditCardOne, "Alivio");
        Payment paymentTwo = new Payment(LocalDate.now(), BigDecimal.valueOf(700.00), creditCardTwo, "Alivio");
        Payment paymentThree = new Payment(LocalDate.now(), BigDecimal.valueOf(700.00), creditCardTwo, "Alivio");
        Payment paymentFour = new Payment(LocalDate.now(), BigDecimal.valueOf(700.00), creditCardTwo, "Alivio");

        Set<Payment> setOfPaymentsOne = new HashSet<>();
        setOfPaymentsOne.add(paymentOne);
        setOfPaymentsOne.add(paymentTwo);
        Set<Payment> setOfPaymentsTwo = new HashSet<>();
        setOfPaymentsTwo.add(paymentThree);
        setOfPaymentsTwo.add(paymentFour);

        Debt debtOne = new Debt(BigDecimal.valueOf(50000.00), LocalDate.now(), setOfPaymentsOne, "111222333444", "speedLoan");
        Debt debtTwo = new Debt(BigDecimal.valueOf(60000.00), LocalDate.now(), setOfPaymentsTwo, "999888777666", "fastLoan");

        Set<Debt> setOfDebts = new HashSet<>();
        setOfDebts.add(debtOne);
        setOfDebts.add(debtTwo);

        return new Debtor("Jakub", "Watus", "980-122-111", setOfDebts);