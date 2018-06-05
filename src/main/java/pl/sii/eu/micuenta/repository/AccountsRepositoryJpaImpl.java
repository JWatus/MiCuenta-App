package pl.sii.eu.micuenta.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import pl.sii.eu.micuenta.model.model_entity.DebtorEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class AccountsRepositoryJpaImpl implements AccountsRepository {
    @PersistenceContext
    private EntityManager em;

    public DebtorEntity findFirstBySsn(String ssn) {
        return em.createNamedQuery("findFirstBySsn", DebtorEntity.class)
                .setParameter("ssn", ssn)
                .getSingleResult();
    }

    public Optional<DebtorEntity> findFirstBySsnAndFirstNameAndLastName(String ssn, String firstName, String lastName) {
        return em.createNamedQuery("findFirstBySsnFirstNameLastName", DebtorEntity.class)
                .setParameter("ssn", ssn)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getResultList()
                .stream()
                .findFirst();
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
    public void delete(DebtorEntity debtor) {
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
