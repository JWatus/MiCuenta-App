package pl.sii.eu.micuenta.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.sii.eu.micuenta.model.CreditCard;
import pl.sii.eu.micuenta.model.Debt;
import pl.sii.eu.micuenta.model.Debtor;
import pl.sii.eu.micuenta.model.Payment;
import pl.sii.eu.micuenta.repository.mappers.DebtorRowMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class AccountsRepositoryNativeImpl implements AccountsRepository {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private NamedParameterJdbcTemplate jdbcTemplate;

    public AccountsRepositoryNativeImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Debtor findFirstBySsn(String ssn) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("ssn", ssn);
        String query = "SELECT ID, FIRST_NAME, LAST_NAME, SSN FROM DEBTOR WHERE SSN = :ssn";
        Debtor debtor = (Debtor) jdbcTemplate.queryForObject(query, paramSource, new DebtorRowMapper());
        debtor.setDebts(this.findDebtById(debtor.getId()));
        return debtor;
    }

    public Optional<Debtor> findFirstBySsnAndFirstNameAndLastName(String ssn, String firstName, String lastName) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("ssn", ssn);
        paramSource.addValue("firstName", firstName);
        paramSource.addValue("lastName", lastName);
        String query = "SELECT ID, FIRST_NAME, LAST_NAME, SSN FROM DEBTOR " +
                "WHERE SSN = :ssn AND FIRST_NAME = :firstName AND LAST_NAME = :lastName";

        Debtor debtor = (Debtor) jdbcTemplate.queryForObject(query, paramSource, new DebtorRowMapper());
        debtor.setDebts(this.findDebtById(debtor.getId()));
        return Optional.ofNullable(debtor);
    }

    private Set<Debt> findDebtById(long id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);
        String query = "SELECT ID, DEBT_AMOUNT, DEBT_NAME, REPAYMENT_DATE, UUID FROM DEBT WHERE DEBTOR_ID = :id";
        Set<Debt> debts = new HashSet<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, paramSource);
        for (Map row : rows) {
            Debt debt = new Debt();
            debt.setId((long) row.get("ID"));
            debt.setDebtAmount(new BigDecimal(row.get("DEBT_AMOUNT").toString()).setScale(2, RoundingMode.HALF_EVEN));
            debt.setDebtName((String) row.get("DEBT_NAME"));
            debt.setRepaymentDate(LocalDate.parse(row.get("REPAYMENT_DATE").toString(), DATE_FORMATTER));
            debt.setUuid((String) row.get("UUID"));
            debt.setPayments(this.findPaymentsByDebtId(debt.getId()));
            debts.add(debt);
        }
        return debts;
    }

    private Set<Payment> findPaymentsByDebtId(long id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);

        String query = "SELECT t1.ID, t1.CLIENT_ID, t1.PAYMENT_AMOUNT, t1.PAYMENT_DATE, t2.* " +
                "FROM PAYMENT t1 JOIN CREDIT_CARD t2 on (t1.CREDIT_CARD_ID=t2.ID)  " +
                "WHERE DEBT_ID = :id";
        Set<Payment> payments = new HashSet<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, paramSource);
        for (Map row : rows) {
            Payment payment = new Payment();
            payment.setId((long) row.get("ID"));
            payment.setClientId((String) row.get("CLIENT_ID"));
            payment.setPaymentAmount(new BigDecimal(row.get("PAYMENT_AMOUNT").toString()).setScale(2, RoundingMode.HALF_EVEN));
            payment.setPaymentDate(LocalDate.parse(row.get("PAYMENT_DATE").toString(), DATE_FORMATTER));
            CreditCard creditCard = new CreditCard();
            creditCard.setId((long) row.get("ID"));
            creditCard.setCcNumber((String) row.get("CC_NUMBER"));
            creditCard.setCvv((String) row.get("CVV"));
            creditCard.setExpDate(LocalDate.parse(row.get("EXP_DATE").toString(), DATE_FORMATTER));
            creditCard.setFirstName((String) row.get("FIRST_NAME"));
            creditCard.setLastName((String) row.get("LAST_NAME"));
            creditCard.setIssuingNetwork((String) row.get("ISSUING_NETWORK"));
            payment.setCreditCard(creditCard);
            payments.add(payment);
        }
        return payments;
    }

    @Override
    public List<Debtor> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Debtor> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Debtor> findAllById(Iterable<Long> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Debtor> List<S> saveAll(Iterable<S> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Debtor> S saveAndFlush(S s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteInBatch(Iterable<Debtor> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Debtor getOne(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Debtor> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Debtor> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Debtor> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Debtor> S save(S s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Debtor> findById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existsById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Debtor debtor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends Debtor> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Debtor> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Debtor> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Debtor> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Debtor> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }
}
