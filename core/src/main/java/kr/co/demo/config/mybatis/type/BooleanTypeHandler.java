package kr.co.demo.config.mybatis.type;

import org.apache.ibatis.type.*;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Alias("booleanTypeHandler")
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(Boolean.class)
public class BooleanTypeHandler extends BaseTypeHandler<Boolean> {

    private static final String YES = "Y";
    private static final String NO = "N";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    Boolean parameter, JdbcType jdbcType) throws SQLException {
        if(parameter == null) {
            ps.setString(i, null);
        }else {
            boolean b = parameter.booleanValue();
            ps.setString(i, b ? YES : NO);
        }
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return convertStringToBooelan(rs.getString(columnName));
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return convertStringToBooelan(rs.getString(columnIndex));
    }

    @Override
    public Boolean getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return convertStringToBooelan(cs.getString(columnIndex));
    }

    private Boolean convertStringToBooelan(String strValue) throws SQLException {
        if (YES.equalsIgnoreCase(strValue)) {
            return new Boolean(true);
        } else if (NO.equalsIgnoreCase(strValue)) {
            return new Boolean(false);
        } else {
            return null;
        }
    }

}
