package com.example.demo.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class CreditCard {

    @Column(name="CREDIT_CARD_NUMBER")
    private String cardNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    public CreditCard() {
    }

    public CreditCard(String cardNumber, User user) {
        this.cardNumber = cardNumber;
        this.user = user;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public User getUser() {
        return user;
    }
}
