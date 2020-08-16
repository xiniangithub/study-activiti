package com.wez.study.activiti.config;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DbConfig {
	
	private static final String ORACLE_DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
	private static final String POSTGRESQL_DRIVER_CLASS_NAME = "org.postgresql.Driver";
	private static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	
	private static final String ORACLE_URL_PREFIX = "jdbc:oracle:thin:@";
	private static final String POSTGRESQL_URL_PREFIX = "jdbc:postgresql://";
	private static final String MYSQL_URL_PREFIX = "jdbc:mysql://";
	private static final String MYSQL_URL_SUFFIX = "?useUnicode=true&characterEncoding=UTF-8";
	
	private static final String ORACLE_DIALECT = "org.hibernate.dialect.Oracle10gDialect";
	private static final String POSTGRESQL_DIALECT = "org.hibernate.dialect.PostgreSQLDialect";
	private static final String MYSQL_DIALECT = "org.hibernate.dialect.MySQLDialect";
	
	private static final String ORACLE_VALIDATION_QUERY = "select 1 FROM DUAL";
	private static final String POSTGRESQL_VALIDATION_QUERY = "select 1 L";
	private static final String MYSQL_VALIDATION_QUERY = "select 1";
	
	private static final String DATABASE_TYPE_ORACLE = "oracle";
	private static final String DATABASE_TYPE_POSTGRESQL = "postgresql";
	private static final String DATABASE_TYPE_MYSQL = "mysql";
	
	private static String databaseUrl = null;
	private static String username = null;
	private static String password = null;
	
	@Autowired
	private DbProperties dbProperties;
	
	@PostConstruct
	private void init() throws Exception {
	    databaseUrl = dbProperties.getDatabaseUrl();
	    username = dbProperties.getDatabaseUsername();
        password = dbProperties.getDatabasePassword();
	}
	
	@Bean(name="dataSource")
	public DruidDataSource dataSource(DbProperties dbProperties) throws Exception {
		String databaseType = dbProperties.getDatabaseType();
		DruidDataSource dataSource = new DruidDataSource();
		if (DATABASE_TYPE_POSTGRESQL.equals(databaseType)) {
			dataSource.setDriverClassName(POSTGRESQL_DRIVER_CLASS_NAME);
			dataSource.setUrl(POSTGRESQL_URL_PREFIX + databaseUrl);
			dataSource.setValidationQuery(POSTGRESQL_VALIDATION_QUERY);
		} else if (DATABASE_TYPE_ORACLE.equals(databaseType)) {
			dataSource.setDriverClassName(ORACLE_DRIVER_CLASS_NAME);
			dataSource.setUrl(ORACLE_URL_PREFIX + databaseUrl);
			dataSource.setValidationQuery(ORACLE_VALIDATION_QUERY);
		} else if (DATABASE_TYPE_MYSQL.equals(databaseType)) {
            dataSource.setDriverClassName(MYSQL_DRIVER_CLASS_NAME);
            dataSource.setUrl(MYSQL_URL_PREFIX + databaseUrl + MYSQL_URL_SUFFIX);
            dataSource.setValidationQuery(MYSQL_VALIDATION_QUERY);
        }
		
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setInitialSize(10);
		dataSource.setMinIdle(4);
		dataSource.setMaxActive(100);
		dataSource.setTestWhileIdle(true);
		dataSource.setTestOnBorrow(true);
		dataSource.setTestOnReturn(false);
		dataSource.setMaxWait(50000);
		dataSource.setMinEvictableIdleTimeMillis(30100);
		dataSource.setTimeBetweenEvictionRunsMillis(10000);
		/*dataSource.setConnectionProperties("config.decrypt=true");
		try {
			dataSource.setFilters("stat,wall,log4j");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		StatFilter statFilter = new StatFilter();
		statFilter.setMergeSql(true);
		statFilter.setSlowSqlMillis(10000);
		statFilter.setLogSlowSql(true);
		
		Log4jFilter log4jFilter = new Log4jFilter();
		log4jFilter.setResultSetLogEnabled(false);
		log4jFilter.setStatementExecutableSqlLogEnable(true);
		
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(statFilter);
		filters.add(log4jFilter);
		dataSource.setProxyFilters(filters);
		*/
		
		return dataSource;
	}
	
	@Bean(name="entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DruidDataSource dataSource, DbProperties dbProperties) throws Exception {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setGenerateDdl(true);
		
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
		jpaProperties.put("hibernate.generate_statistics", false);
		jpaProperties.put("hibernate.default_schema", username);
		jpaProperties.put("hibernate.hbm2ddl.auto", "none");
		jpaProperties.put("hibernate.show_sql", false);
		jpaProperties.put("hibernate.format_sql", false);
		jpaProperties.put("hibernate.enable_lazy_load_no_trans", true);
		
		String databaseType = dbProperties.getDatabaseType();
		if (DATABASE_TYPE_POSTGRESQL.equals(databaseType)) {
			hibernateJpaVendorAdapter.setDatabasePlatform(POSTGRESQL_DIALECT);
			jpaProperties.put("hibernate.dialect", POSTGRESQL_DIALECT);
		} else if (DATABASE_TYPE_ORACLE.equals(databaseType)) {
			hibernateJpaVendorAdapter.setDatabasePlatform(ORACLE_DIALECT);
			jpaProperties.put("hibernate.dialect", ORACLE_DIALECT);
		} else if (DATABASE_TYPE_MYSQL.equals(databaseType)) {
            hibernateJpaVendorAdapter.setDatabasePlatform(MYSQL_DIALECT);
            jpaProperties.put("hibernate.dialect", MYSQL_DIALECT);
        }
		
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource);
		entityManagerFactory.setJpaVendorAdapter(hibernateJpaVendorAdapter);
		entityManagerFactory.setPackagesToScan("com.dscomm.task.light.po", "com.dscomm.task.po");
		entityManagerFactory.setJpaProperties(jpaProperties);
		return entityManagerFactory;
	}
	
}
