package com.example.demo.controller;

import com.example.demo.model.Credentials;
import com.example.demo.model.CreditCard;
import com.example.demo.model.User;
import com.example.demo.repository.AccountsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/micuenta")
public class AccountController {

    private Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    AccountsRepository accountsRepository;

    @GET
    @Path("/welcome")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sayHello(@QueryParam("name") String name,
                             @QueryParam("surname") String surname) {

        LOG.info("Welcome " + name + " " + surname + ". Enjoy MiCuenta app.");

        return Response.ok("Hello " + name + " " + surname + ". Enjoy MiCuenta app.").build();
    }

    @POST
    @Path("/add")
    @Produces({MediaType.TEXT_PLAIN})
    public Response add(@FormParam("username") String username,
                          @FormParam("password") String password,
                          @FormParam("name") String name,
                          @FormParam("surname") String surname,
                          @FormParam("creditCardNumber") String creditCardNumber
                          ) throws URISyntaxException {

        User user = new User();
        Credentials credentials = new Credentials();

        Set<CreditCard> creditCards = new LinkedHashSet<>();
        CreditCard newCreditCard = new CreditCard(creditCardNumber, user);
        creditCards.add(newCreditCard);;

        user.setName(name);
        user.setSurname(surname);
        user.setCreditCardNumber(creditCards);
        credentials.setUsername(username);
        credentials.setPassword(password);

        accountsRepository.save(user);

        LOG.info("User created");
        return Response.ok("Created user " + username + " with ID " + user.getId()).build();
    }

    @POST
    @Path("/login")
    @Produces({MediaType.TEXT_PLAIN})
    public Response login(@FormParam("username") String username,
                          @FormParam("pass") String password) throws URISyntaxException {

        LOG.info("Login attempt : [" + username + "]");

        Optional<User> userByUserName = accountsRepository.findAll()
                .stream()
                .filter(u -> u.getCredentials().getUsername().equals(username))
                .findFirst();

        if (userByUserName.isPresent()) {
            if (userByUserName.get().getCredentials().getPassword().equals(password)) {
                LOG.info("Authorization passed");
                return Response.ok("Authorization passed for " + username + " with ID " + userByUserName.get().getId()).build();
            } else {
                LOG.info("Wrong password for user " + userByUserName.get().getCredentials().getPassword() + ":" + password);
                return Response.temporaryRedirect(new URI("/micuenta/welcome/")).build();
            }
        } else {
            LOG.info("There is not such user in MiCuenta system");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}








