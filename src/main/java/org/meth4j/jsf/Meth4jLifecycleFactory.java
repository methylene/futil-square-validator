package org.meth4j.jsf;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
/**
 *
 * methylene: This file is a modified version of svn revision 10973 of
 * https://svn.java.net/svn/mojarra~svn/trunk/jsf-ri/src/main/java/com/sun/faces/lifecycle/LifecycleFactoryImpl.java
 * 
 * Its purpose is to help override com.sun.faces.lifecycle.RestoreViewPhase
 *
 */
public class Meth4jLifecycleFactory extends LifecycleFactory {
	


    // Log instance for this class
    private static Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();

    protected ConcurrentHashMap<String,Lifecycle> lifecycleMap = null;


    // ------------------------------------------------------------ Constructors


    public Meth4jLifecycleFactory() {
        super();
        lifecycleMap = new ConcurrentHashMap<String,Lifecycle>();

        // We must have an implementation under this key.
        lifecycleMap.put(LifecycleFactory.DEFAULT_LIFECYCLE,
                         new Meth4jLifecycleImpl(FacesContext.getCurrentInstance()));
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Created Default Lifecycle");
        }
    }


    // -------------------------------------------------- Methods from Lifecycle


    public void addLifecycle(String lifecycleId, Lifecycle lifecycle) {
        if (lifecycleId == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "lifecycleId"));
        }
        if (lifecycle == null) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "lifecycle"));
        }
        if (null != lifecycleMap.get(lifecycleId)) {
            final Object params[] = {lifecycleId};
            final String message =
                MessageUtils.getExceptionMessageString(MessageUtils.LIFECYCLE_ID_ALREADY_ADDED_ID,
                                         params);
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(MessageUtils.getExceptionMessageString(
                        MessageUtils.LIFECYCLE_ID_ALREADY_ADDED_ID,params));
            }
            throw new IllegalArgumentException(message);
        }
        lifecycleMap.put(lifecycleId, lifecycle);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("addedLifecycle: " + lifecycleId + " " + lifecycle);
        }
    }


    public Lifecycle getLifecycle(String lifecycleId) throws FacesException {

        if (null == lifecycleId) {
            throw new NullPointerException(
                MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "lifecycleId"));
        }

        if (null == lifecycleMap.get(lifecycleId)) {
            final Object[] params = {lifecycleId};
            final String message =
                MessageUtils.getExceptionMessageString(
                    MessageUtils.CANT_CREATE_LIFECYCLE_ERROR_MESSAGE_ID,
                    params);
            throw new IllegalArgumentException(message);
        }

        final Lifecycle result = lifecycleMap.get(lifecycleId);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("getLifecycle: " + lifecycleId + " " + result);
        }
        return result;
    }


    public Iterator<String> getLifecycleIds() {
        return lifecycleMap.keySet().iterator();
    }



// The testcase for this class is TestLifecycleFactoryImpl.java 

}
