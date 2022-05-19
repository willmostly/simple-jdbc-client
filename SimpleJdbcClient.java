import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;

import java.sql.Types;
import java.util.Properties;

class SimpleJdbcClient
{
    public static void connectPresto(String jdbcDriverClass, String connectionString, String query)
    {
        //Connect to Presto server using Presto JDBC
        Properties properties = new Properties();
        Connection conn = null;
        Statement stmt = null;
        try {
            //Register JDBC driver
            Class.forName(jdbcDriverClass);
            //Open a connection
            conn = DriverManager.getConnection(connectionString, properties);
            //Execute a query
            stmt = conn.createStatement();

            long startMs = System.currentTimeMillis();
            ResultSet res = stmt.executeQuery(query);

            //Extract data from result set
            long endMs = System.currentTimeMillis();
            System.out.println("Query finishes in " + (endMs - startMs) + " ms");

            while (res.next()) {
                int columnCount = res.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = res.getMetaData().getColumnName(i);
                    System.out.print(columnName + ",");
                }
                System.out.println("");
                for (int i = 1; i <= columnCount; i++) {
                    String column;
                    if (res.getMetaData().getColumnType(i) == Types.DATE) {
                        column = res.getDate(i).toString();
                    } else {
                        column = res.getString(i);
                    }
                    System.out.print(column + ",");
                }
                System.out.println("");
            }
            //Clean-up environment
            res.close();
            stmt.close();
            conn.close();
        }
        catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        finally {
            //finally block used to close resources
            try {
				if (stmt != null) { stmt.close(); }
            }
            catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            try {
				if (conn != null) { conn.close(); }
            }
            catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void main(String[] argv)
    {
        if (argv.length == 0 || argv[0].compareTo("-h") == 0 || argv[0].compareTo("--help") == 0 || argv.length != 3) {
            System.err.println("Usage: java SimpleJdbcClient <jdbcDriverClass> '<connectionString>' '<query>'");
            return;
        }
        connectPresto(argv[0], argv[1], argv[2]);
    }
}
