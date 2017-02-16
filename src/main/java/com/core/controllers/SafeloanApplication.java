package com.core.controllers;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by t.konst on 16.02.2017.
 */

public class SafeloanApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(Users.class);
        s.add(Groups.class);
        s.add(Loans.class);
        return s;
    }
}
