package pl.sii.eu.micuenta.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.sii.eu.micuenta.model.model_entity.CreditCardEntity;
import pl.sii.eu.micuenta.model.model_entity.DebtEntity;
import pl.sii.eu.micuenta.model.model_entity.DebtorEntity;
import pl.sii.eu.micuenta.model.model_entity.PaymentEntity;
import pl.sii.eu.micuenta.repository.mappers.DebtorEntityRowMapper;

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

    public DebtorEntity findFirstBySsn(String ssn) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("ssn", ssn);
        String query = "SELECT ID, FIRST_NAME, LAST_NAME, SSN FROM DEBTOR_ENTITY WHERE SSN = :ssn";
        DebtorEntity debtorEntity = (DebtorEntity) jdbcTemplate.queryForObject(query, paramSource, new DebtorEntityRowMapper());
        debtorEntity.setDebtEntities(this.findDebtEntityById(debtorEntity.getId()));
        return debtorEntity;
    }

    public Optional<DebtorEntity> findFirstBySsnAndFirstNameAndLastName(String ssn, String firstName, String lastName) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("ssn", ssn);
        paramSource.addValue("firstName", firstName);
        paramSource.addValue("lastName", lastName);
        String query = "SELECT ID, FIRST_NAME, LAST_NAME, SSN FROM DEBTOR_ENTITY " +
                "WHERE SSN = :ssn AND FIRST_NAME = :firstName AND LAST_NAME = :lastName";

        DebtorEntity debtorEntity = (DebtorEntity) jdbcTemplate.queryForObject(query, paramSource, new DebtorEntityRowMapper());
        debtorEntity.setDebtEntities(this.findDebtEntityById(debtorEntity.getId()));
        return Optional.ofNullable(debtorEntity);
    }

    private Set<DebtEntity> findDebtEntityById(long id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);
        String query = "SELECT ID, DEBT_AMOUNT, DEBT_NAME, REPAYMENT_DATE, UUID FROM DEBT_ENTITY WHERE DEBTOR_ENTITY_ID = :id";
        Set<DebtEntity> debts = new HashSet<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, paramSource);
        for (Map row : rows) {
            DebtEntity debtEntity = new DebtEntity();
            debtEntity.setId((long) row.get("ID"));
            debtEntity.setDebtAmount(new BigDecimal(row.get("DEBT_AMOUNT").toString()).setScale(2, RoundingMode.HALF_EVEN));
            debtEntity.setDebtName((String) row.get("DEBT_NAME"));
            debtEntity.setRepaymentDate(LocalDate.parse(row.get("REPAYMENT_DATE").toString(), DATE_FORMATTER));
            debtEntity.setUuid((String) row.get("UUID"));
            debtEntity.setPaymentEntities(this.findPaymentEntitiesByDebtEntityId(debtEntity.getId()));
            debts.add(debtEntity);
        }
        return debts;
    }

    private Set<PaymentEntity> findPaymentEntitiesByDebtEntityId(long id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);

        String query = "SELECT t1.ID, t1.CLIENT_ID, t1.PAYMENT_AMOUNT, t1.PAYMENT_DATE, t2.* " +
                "FROM PAYMENT_ENTITY t1 JOIN CREDIT_CARD_ENTITY t2 on (t1.CREDIT_CARD_ENTITY_ID=t2.ID)  " +
                "WHERE DEBT_ENTITY_ID = :id";
        Set<PaymentEntity> payments = new HashSet<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, paramSource);
        for (Map row : rows) {
            PaymentEntity paymentEntity = new PaymentEntity();
            paymentEntity.setId((long) row.get("ID"));
            paymentEntity.setClientId((String) row.get("CLIENT_ID"));
            paymentEntity.setPaymentAmount(new BigDecimal(row.get("PAYMENT_AMOUNT").toString()).setScale(2, RoundingMode.HALF_EVEN));
            paymentEntity.setPaymentDate(LocalDate.parse(row.get("PAYMENT_DATE").toString(), DATE_FORMATTER));
            CreditCardEntity creditCardEntity = new CreditCardEntity();
            creditCardEntity.setId((long) row.get("ID"));
            creditCardEntity.setCcNumber((String) row.get("CC_NUMBER"));
            creditCardEntity.setCvv((String) row.get("CVV"));
            creditCardEntity.setExpDate(LocalDate.parse(row.get("EXP_DATE").toString(), DATE_FORMATTER));
            creditCardEntity.setFirstName((String) row.get("FIRST_NAME"));
            creditCardEntity.setLastName((String) row.get("LAST_NAME"));
            creditCardEntity.setIssuingNetwork((String) row.get("ISSUING_NETWORK"));
            paymentEntity.setCreditCardEntity(creditCardEntity);
            payments.add(paymentEntity);
        }
        return payments;
    }

    @Override
    public List<DebtorEntity> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DebtorEntity> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DebtorEntity> findAllById(Iterable<Long> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends DebtorEntity> List<S> saveAll(Iterable<S> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends DebtorEntity> S saveAndFlush(S s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteInBatch(Iterable<DebtorEntity> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DebtorEntity getOne(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends DebtorEntity> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends DebtorEntity> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<DebtorEntity> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends DebtorEntity> S save(S s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<DebtorEntity> findById(Long aLong) {
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
    public void delete(DebtorEntity debtorEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends DebtorEntity> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends DebtorEntity> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends DebtorEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends DebtorEntity> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends DebtorEntity> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }
}
