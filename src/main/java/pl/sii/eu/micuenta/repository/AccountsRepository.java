package pl.sii.eu.micuenta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sii.eu.micuenta.model.Debtor;

@Repository
public interface AccountsRepository extends JpaRepository<Debtor, Long> {

    Debtor findFirstBySsn(String ssn);

}