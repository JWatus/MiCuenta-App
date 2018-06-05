package pl.sii.eu.micuenta.repository.mappers;

import org.springframework.jdbc.core.RowMapper;
import pl.sii.eu.micuenta.model.model_entity.DebtorEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DebtorEntityRowMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        DebtorEntity debtorEntity = new DebtorEntity();
        debtorEntity.setId(rs.getLong("ID"));
        debtorEntity.setFirstName(rs.getString("FIRST_NAME"));
        debtorEntity.setLastName(rs.getString("LAST_NAME"));
        debtorEntity.setSsn(rs.getString("SSN"));
        return debtorEntity;
    }
}

