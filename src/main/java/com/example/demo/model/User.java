package com.example.demo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID")
    private int id;
    @Column(name="NAME")
    private String name;
    @Column(name="SURNAME")
    private String surname;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<CreditCard> creditCardNumber;

    @Transient
    private Credentials credentials;

    public User() {
    }

    public User(String name, String surname, Set<CreditCard> creditCardNumber, Credentials credentials) {
        this.name = name;
        this.surname = surname;
        this.credentials = credentials;
        this.creditCardNumber = creditCardNumber;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getId() {
        return id;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public Set<CreditCard> getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCreditCardNumber(Set<CreditCard> creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
