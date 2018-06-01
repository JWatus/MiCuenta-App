package pl.sii.eu.micuenta.repository.mappers;

import pl.sii.eu.micuenta.model.Debtor;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DebtorRowMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
        Debtor debtor = new Debtor();
        debtor.setId(rs.getLong("ID"));
        debtor.setFirstName(rs.getString("FIRST_NAME"));
        debtor.setLastName(rs.getString("LAST_NAME"));
        debtor.setSsn(rs.getString("SSN"));
        return debtor;
    }
}

