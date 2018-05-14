package pl.sii.eu.micuenta.service.controller;

import org.springframework.stereotype.Service;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.Payment;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DebtCalculatorService {

    BigDecimal getDebtLeftToPaid(Debt chosenDebt) {
        BigDecimal sumOfPayments = getSumOfPayments(chosenDebt);
        BigDecimal debtAmount = chosenDebt.getDebtAmount();
        return debtAmount.subtract(sumOfPayments);
    }

    List<Debt> getListOfOldestDebts(Debtor debtor) {
        return debtor
                .getSetOfDebts()
                .stream()
                .filter(d -> d.getDebtAmount().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(Debt::getRepaymentDate))
                .collect(Collectors.toList());
    }

    BigDecimal getSumOfDebts(Debtor debtor) {
        return debtor
                .getSetOfDebts()
                .stream()
                .map(Debt::getDebtAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    BigDecimal getSumOfPayments(Debt oldestDebt) {
        return oldestDebt
                .getSetOfPayments()
                .stream()
                .map(Payment::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}