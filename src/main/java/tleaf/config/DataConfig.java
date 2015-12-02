package tleaf.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.sql.DataSource;

@Configuration
public class DataConfig {

    //TODO @Profile 0 create multiple profiles in DataConfig : @Profile - @Dev @Prod @Test annotations
    // https://spring.io/blog/2011/02/14/spring-3-1-m1-introducing-profile/
    //@Profile("dev")
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                //.addScript("schema.sql")
                .addScript("classpath:schema.sql")
                .build();
    }

    @Bean
    public JdbcOperations jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }



    //@Bean
    public HikariDataSource dataSourceHikari() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:mysql://localhost:8080/tleaf");
        //alternative to JDBC URL ( with Spring Boot use JdbcUrl only )
        //ds.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
        ds.setUsername("test");
        ds.setPassword("test");
        ds.addDataSourceProperty("cachePrepStmts","true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit","2048");
        return ds;

        //alternative - from Properties file
//        HikariConfig config = new HikariConfig("some/path/hikari.properties");
//        HikariDataSource ds = new HikariDataSource(config);
//        //in hikari.properties:
//        dataSourceClassName=org.postgresql.ds.PGSimpleDataSource
//        dataSource.user=test
//        dataSource.password=test
//        dataSource.databaseName=mydb
//        dataSource.portNumber=5432
//        dataSource.serverName=localhost
    }


    // Commons DBCP
    //@Profile("test")
    //@Bean
    public BasicDataSource dataSourceCOMMONS() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:tcp://localhost/~/tleaf");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setInitialSize(5);
        ds.setMaxActive(10);
        return ds;
    }

    // JNDI
    //@Profile("prod") //or MySQL; Oracle; MongoDB ?
    //@Bean
    public JndiObjectFactoryBean dataSourceJNDI() {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("jdbc/TleafDS");
        jndiObjectFactoryBean.setResourceRef(true);
        jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
        return jndiObjectFactoryBean;
    }
}
