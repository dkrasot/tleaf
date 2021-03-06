package tleaf.config;

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
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.rmi.RmiServiceExporter;
import tleaf.service.TleafService;

import javax.sql.DataSource;

@Configuration
public class DataConfig {

    @Bean
    public JdbcOperations jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Profile("development")
    @Bean(destroyMethod = "shutdown")
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                        //.addScript("schema.sql")
                .addScript("classpath:schema.sql")
                .build();
    }

    @Profile("qa")
    @Bean
    //Unable to get driver instance for jdbcUrl=jdbc:h2:~/test  ...  No suitable driver
    public DataSource dataSourceHikari() {
        HikariDataSource ds = new HikariDataSource();
        //ds.setJdbcUrl("jdbc:mysql://localhost:8080/tleaf");
        ds.setJdbcUrl("jdbc:h2:~/test");
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


    // JNDI to Remote server  MySQL; Oracle; MongoDB ?
    @Profile("production")
    @Bean
    public DataSource dataSourceJNDI() {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("jdbc/TleafDS");
        jndiObjectFactoryBean.setResourceRef(true);
        jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
        return (DataSource) jndiObjectFactoryBean.getObject();
    }

    @Profile("CommonsCP")
    @Bean
    public DataSource dataSourceCOMMONS() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:tcp://localhost/~/tleaf");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setInitialSize(5);
        ds.setMaxActive(10);
        return ds;
    }


    // RMI + service-tier
    //RmiServiceExporter wraps Service to class-adapter, which would be linked to RMI registry
    //@Bean
    public RmiServiceExporter rmiServiceExporter(TleafService tleafService) {
        RmiServiceExporter rmiExporter = new RmiServiceExporter();
        rmiExporter.setService(tleafService);
        rmiExporter.setServiceName("TleafService");
        rmiExporter.setServiceInterface(TleafService.class);
        //rmiExporter.setRegistryHost("rmi.tleaf.com");
        rmiExporter.setRegistryHost("rmi://localhost/TleafService");
        //rmiExporter.setRegistryPort(1199);//by default is D1099
        return rmiExporter;
    }

    //// ON THEN CLIENT SIDE: DAO config @Bean RmiProxyFactoryBean
//    @Bean
//    public RmiProxyFactoryBean tleafService1() {
//        RmiProxyFactoryBean rmiProxy = new RmiProxyFactoryBean();
//        rmiProxy.setServiceUrl("rmi://localhost/TleafService");
//        rmiProxy.setServiceInterface(TleafService.class);
//        return rmiProxy;
//    }
}
