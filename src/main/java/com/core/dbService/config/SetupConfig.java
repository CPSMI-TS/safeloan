package com.core.dbService.config;

import com.core.dbService.entities.*;
import org.hibernate.cfg.Configuration;

/**
 * Created by t.konst on 21.01.2017.
 */
public class SetupConfig {
    public static Configuration configureHibernate(String username, String pass) {
        Configuration config = new Configuration();
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        config.setProperty("hibernate.connection.url", "jdbc:mysql://eu-cdbr-west-01.cleardb.com/heroku_ac57419099e82de?reconnect=true");
        config.setProperty("hibernate.connection.username", "b3811fe87764ea");
        config.setProperty("hibernate.connection.password", "dea1631c");
        config.setProperty("hibernate.show_sql", "false");
        //config.setProperty("hibernate.hbm2ddl.auto", "update");
        config.setProperty("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
        config.addAnnotatedClass(User.class);
        config.addAnnotatedClass(Group.class);
        config.addAnnotatedClass(Loan.class);
        config.addAnnotatedClass(Item.class);
        config.addAnnotatedClass(UsersGroups.class);
        config.addAnnotatedClass(LoanUsers.class);
        return config;
    }
}
