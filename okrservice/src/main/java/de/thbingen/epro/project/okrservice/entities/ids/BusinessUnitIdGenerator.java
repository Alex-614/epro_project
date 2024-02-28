package de.thbingen.epro.project.okrservice.entities.ids;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.jdbc.ReturningWork;

import de.thbingen.epro.project.okrservice.entities.BusinessUnit;


/**
 * Custom Id generator, to properly generate partially unique identifiers for BusinessUnits
 */
public class BusinessUnitIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        ReturningWork<BusinessUnitId> maxReturningWork = new ReturningWork<BusinessUnitId>() {
            @Override
            public BusinessUnitId execute(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                try {
                    Long companyId = ((BusinessUnit) obj).getCompany().getId();
                    preparedStatement = connection.prepareStatement("SELECT MAX(id) AS Id FROM tbl_businessunit WHERE company_id = " + companyId);
                    resultSet = preparedStatement.executeQuery();
                    Long max = 1L;
                    if (resultSet.next()) {
                        max += resultSet.getLong(1);
                    }
                    return new BusinessUnitId(max, companyId);
                }catch (SQLException e) {
                    throw e;
                } finally {
                    if(preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if(resultSet != null) {
                        resultSet.close();
                    }
                }
            }
        };
        BusinessUnitId result = session.doReturningWork(maxReturningWork);
        return result;
    }




}
