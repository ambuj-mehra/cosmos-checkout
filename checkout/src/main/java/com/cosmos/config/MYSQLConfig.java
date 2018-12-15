package com.cosmos.config;

import com.mysql.jdbc.Driver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * The type Mysql config.
 *
 * @author ambujmehra
 */
@Configuration
@ComponentScan({"com.cosmos.repository"})
@EnableJpaRepositories(basePackages = {"com.cosmos.repository"}, transactionManagerRef = "securityTransactionManager")
@EnableTransactionManagement
public class MYSQLConfig {

    @Value("${mysql.url}")
    private String mysqlURL;

    @Value("${mysql.isreplicaenabled}")
    private Boolean mysqlReplicaEnabled;

    @Value("${mysql.username}")
    private String mysqlUsername;

    @Value("${mysql.password}")
    private String mysqlPassword;

    @Value("${mysql.minidlethreads}")
    private Integer mysqlMinIdleThreads;

    @Value("${mysql.maxthreadpoolsize}")
    private Integer mysqlMaxThreadPoolSize;

    @Value("${mysql.connectiontimeoutmillis}")
    private Integer connectionTimeoutMillis;

    @Value("${mysql.idletimeoutmillis}")
    private Integer idleTimeoutMillis;

    @Value("${mysql.maxlifetimemillis}")
    private Integer maxLifeTimeMillis;

    @Value("${mysql.generateddl}")
    private boolean dll;

    @Value("${mysql.showsql}")
    private boolean showSql;

    @Value("${mysql.statistics}")
    private String statistics;

    /**
     * Data source data source.
     *
     * @return the data source
     * @throws PropertyVetoException the property veto exception
     */
    @Bean
    @Primary
    DataSource dataSource() throws PropertyVetoException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(mysqlURL);
        config.setDriverClassName(Driver.class.getName());

        //The connection test query is needed for replication driver to work
        config.setConnectionTestQuery("SELECT 1");
        config.setUsername(mysqlUsername);
        config.setPassword(mysqlPassword);
        config.setMinimumIdle(mysqlMinIdleThreads);
        config.setMaximumPoolSize(mysqlMaxThreadPoolSize);
        config.setConnectionTimeout(connectionTimeoutMillis);
        config.setIdleTimeout(idleTimeoutMillis);
        config.setMaxLifetime(maxLifeTimeMillis);

        //https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        return new HikariDataSource(config);
    }

    /**
     * Entity manager factory entity manager factory.
     *
     * @return the entity manager factory
     * @throws PropertyVetoException the property veto exception
     */
    @Bean
    @Primary
    public EntityManagerFactory entityManagerFactory() throws PropertyVetoException {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(dll);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        vendorAdapter.setShowSql(showSql);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.cosmos.entity");
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();

        return factory.getObject();
    }


}
