package tleaf.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import tleaf.Tweet;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataConfig {

    @Profile("orm-hibernate")
    @Bean
    //public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
    public SessionFactory sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
        sfb.setDataSource(dataSource);
        //sfb.setPackagesToScan(new String[]{"tleaf"}); // searching for @Entity ; @MappedSuperclass
        sfb.setAnnotatedClasses(new Class<?>[]{tleaf.Profile.class, tleaf.Tweet.class});// file list
        //sfb.setMappingResources(new String[]{"Profile.hbm.xml"}); // file list with Hibernate mappings
        Properties props = new Properties();
        props.setProperty("dialect", "org.hibernate.dialect.H2Dialect");
        sfb.setHibernateProperties(props);
        //return sfb;
        return (SessionFactory) sfb.getObject();
    }

    //translate FROM @Repo Hibernate-exceptions TO Spring=exceptions
    @Profile("orm-hibernate")
    @Bean
    public BeanPostProcessor persistenceTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Profile("jdbc")
    @Bean
    public JdbcOperations jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Profile({"jdbc","orm-hibernate"})
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

}
