package reflection;

import java.sql.SQLException;

public class ExceptionClass {
    public ExceptionClass() throws SQLException {
        throw new SQLException();
    }
}
