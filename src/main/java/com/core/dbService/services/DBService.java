package com.core.dbService.services;

import com.core.dbService.config.SetupConfig;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Created by t.konst on 21.01.2017.
 */
public class DBService {
    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = SetupConfig.configureHibernate("root", "javaforever");
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry registry = builder.build();
        sessionFactory = configuration.buildSessionFactory(registry);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void stop() {
        if (sessionFactory.isOpen())
            sessionFactory.close();
    }
}
