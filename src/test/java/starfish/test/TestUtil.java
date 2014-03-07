package starfish.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import starfish.helper.ConnectionActivityNoResult;
import starfish.helper.JdbcUtil;
import starfish.type.TableMetadata;

public class TestUtil {

    public static final TableMetadata meta = TableMetadata.create("session", "skey", "value", "version", "updated");

    public static Properties loadProperties() {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("database.properties");
        final Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load test properties", e);
        }
        return properties;
    }

    public static DataSource makeTestDataSource() {
        final Properties properties = loadProperties();
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(properties.getProperty("driver.classname"));
        ds.setUrl(properties.getProperty("jdbc.url"));
        ds.setUsername(properties.getProperty("jdbc.username"));
        ds.setPassword(properties.getProperty("jdbc.password"));
        ds.setValidationQuery(properties.getProperty("validation.query"));
        return ds;
    }

    public static boolean isMysqlTestEnabled() {
        final Properties properties = loadProperties();
        final String status = properties.getProperty("test.mysql.support");
        return status != null && Boolean.parseBoolean(status);
    }

    public static void createTable(DataSource ds) {
        JdbcUtil.withConnectionNoResult(ds, new ConnectionActivityNoResult() {
            public void execute(Connection conn) {
                JdbcUtil.update(conn, loadProperties().getProperty("create.table.ddl"), null);
            }
        });
    }

    public static void dropTable(DataSource ds) {
        JdbcUtil.withConnectionNoResult(ds, new ConnectionActivityNoResult() {
            public void execute(Connection conn) {
                JdbcUtil.update(conn, loadProperties().getProperty("drop.table.ddl"), null);
            }
        });
    }

}
