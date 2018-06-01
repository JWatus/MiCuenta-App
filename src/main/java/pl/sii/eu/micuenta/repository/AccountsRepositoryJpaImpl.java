package pl.sii.eu.micuenta.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import pl.sii.eu.micuenta.model.Debtor;

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

    public Debtor findFirstBySsn(String ssn) {
        return em.createNamedQuery("findFirstBySsn", Debtor.class)
                .setParameter("ssn", ssn)
                .getSingleResult();
    }

    public Optional<Debtor> findFirstBySsnAndFirstNameAndLastName(String ssn, String firstName, String lastName) {
        return em.createNamedQuery("findFirstBySsnFirstNameLastName", Debtor.class)
                .setParameter("ssn", ssn)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getResultList()
                .stream()
                .findFirst();
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
