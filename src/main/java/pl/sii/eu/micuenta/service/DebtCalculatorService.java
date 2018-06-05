package pl.sii.eu.micuenta.service;

import org.springframework.stereotype.Service;
import pl.sii.eu.micuenta.model.model_entity.DebtEntity;
import pl.sii.eu.micuenta.model.model_entity.DebtorEntity;
import pl.sii.eu.micuenta.model.model_entity.PaymentEntity;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
class DebtCalculatorService {

    BigDecimal getDebtLeftToPaid(DebtEntity chosenDebtEntity) {
        BigDecimal sumOfPayments = getSumOfPayments(chosenDebtEntity);
        BigDecimal debtAmount = chosenDebtEntity.getDebtAmount();
        return debtAmount.subtract(sumOfPayments);
    }

    List<DebtEntity> getListOfOldestDebts(DebtorEntity debtorEntity) {
        return debtorEntity
                .getDebtEntities()
                .stream()
                .filter(d -> d.getDebtAmount().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(DebtEntity::getRepaymentDate))
                .collect(Collectors.toList());
    }

    BigDecimal getSumOfDebts(DebtorEntity debtorEntity) {
        return debtorEntity
                .getDebtEntities()
                .stream()
                .map(DebtEntity::getDebtAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    BigDecimal getSumOfPayments(DebtEntity oldestDebtEntity) {
        return oldestDebtEntity
                .getPaymentEntities()
                .stream()
                .map(PaymentEntity::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}