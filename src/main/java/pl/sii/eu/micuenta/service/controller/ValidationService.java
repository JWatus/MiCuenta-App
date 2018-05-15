package pl.sii.eu.micuenta.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ValidationService {

    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);

    boolean notValidPaymentAmount(BigDecimal paymentAmount, String ssn) {
        if (paymentAmount.compareTo(BigDecimal.ZERO) > 0) {
            logger.info("Received payment: {} for user with ssn {}.",
                    paymentAmount.setScale(2, RoundingMode.HALF_EVEN), ssn);
            return false;
        } else {
            logger.info("Not valid payment amount.");
            return true;
        }
    }
}