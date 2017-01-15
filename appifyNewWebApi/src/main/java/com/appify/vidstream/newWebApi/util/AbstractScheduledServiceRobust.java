package com.appify.vidstream.newWebApi.util;

import com.appify.vidstream.newWebApi.GetImageServlet;
import com.google.common.util.concurrent.AbstractScheduledService;
import org.apache.log4j.Logger;

/**
 * Created by swapnil on 15/01/17.
 * <p>
 * <p>This class handles exception after calling work method from the derived classes. Doing this helps service gets
 * executed even after failing once.
 */
public abstract class AbstractScheduledServiceRobust extends AbstractScheduledService {

    private static final Logger logger = Logger.getLogger(AbstractScheduledServiceRobust.class);

    public void runOneIteration() {
        try {
            logger.info("Starting service " + getName());
            work();
            logger.info(getName() + " service completed successfully.");
        } catch (Exception e) {
            logger.error(getName() + " service encountered error " + e);
        }
    }

    /**
     * Override this method for custom name.
     */
    protected String getName() {
        return getClass().getName();
    }

    protected abstract void work();
}
