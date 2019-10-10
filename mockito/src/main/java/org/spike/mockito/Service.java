package org.spike.mockito;

import java.time.DayOfWeek;
import java.util.logging.Logger;

public class Service {


    private Dao dao;
    private Logger logger;

    public Dao getDao() {
        return dao;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}

