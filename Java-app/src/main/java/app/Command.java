
package app;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Command {
    void act(ResultSet rs) throws SQLException;
}
