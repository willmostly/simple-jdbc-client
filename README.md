# simple-jdbc-client

Occasionally one needs to check if a jdbc driver or connection string works independently of a client tool like SAS, Tableau, etc. This is a bare bones program that loads a driver and runs a query. For example:

```
git clone git@github.com:willmostly/simple-jdbc-client.git 
wget https://repo1.maven.org/maven2/io/trino/trino-jdbc/380/trino-jdbc-380.jar
javac SimpleJdbcClient.java
java -cp .:trino-jdbc-380.jar SimpleJdbcClient 'io.trino.jdbc.TrinoDriver' 'jdbc:trino://localhost:8080?user=will' 'SELECT 1'

#Output:
#Query finishes in 1309 ms
#_col0,
#1,
```

This should work with any (most?) JDBC drivers, not just the Trino driver.
