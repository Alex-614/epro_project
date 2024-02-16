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

import de.thbingen.epro.project.okrservice.entities.Unit;

public class UnitIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        ReturningWork<UnitId> maxReturningWork = new ReturningWork<UnitId>() {
            @Override
            public UnitId execute(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                try {
                    BusinessUnitId businessUnitId = ((Unit) obj).getBusinessUnit().getId();
                    preparedStatement = connection.prepareStatement("SELECT MAX(id) AS Id FROM tbl_unit WHERE company_id = " + businessUnitId.getCompanyId()
                                                                                                            + " AND businessunit_id = " + businessUnitId.getId());
                    resultSet = preparedStatement.executeQuery();
                    Long max = 1L;
                    if (resultSet.next()) {
                        max += resultSet.getLong(1);
                    }
                    return new UnitId(max, businessUnitId);
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
        UnitId result = session.doReturningWork(maxReturningWork);
        return result;
    }




}
