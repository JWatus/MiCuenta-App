package pl.sii.eu.micuenta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sii.eu.micuenta.model.model_entity.DebtorEntity;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<DebtorEntity, Long> {

    DebtorEntity findFirstBySsn(String ssn);

    Optional<DebtorEntity> findFirstBySsnAndFirstNameAndLastName(String ssn, String firstName, String lastName);
}