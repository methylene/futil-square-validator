package org.meth4j.jsf;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PhaseListener;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.lifecycle.ClientWindow;
import javax.faces.lifecycle.Lifecycle;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.lifecycle.ApplyRequestValuesPhase;
import com.sun.faces.lifecycle.ClientWindowImpl;
import com.sun.faces.lifecycle.InvokeApplicationPhase;
import com.sun.faces.lifecycle.Phase;
import com.sun.faces.lifecycle.ProcessValidationsPhase;
import com.sun.faces.lifecycle.RenderResponsePhase;
import com.sun.faces.lifecycle.UpdateModelValuesPhase;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;

/**
 * 
 * methylene: This file is a modified version of svn revision 10973 of
 * https://svn.java.net/svn/mojarra~svn/trunk/jsf-ri/src/main/java/com/sun/faces/lifecycle/LifecycleImpl.java
 * 
 * Its purpose is to help override com.sun.faces.lifecycle.RestoreViewPhase
 *
 */
public class Meth4jLifecycleImpl extends Lifecycle {
	
	   // -------------------------------------------------------- Static Variables


    // Log instance for this class
    private static Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();


    // ------------------------------------------------------ Instance Variables

    // The Phase instance for the render() method
    private Phase response = new RenderResponsePhase();

    // The set of Phase instances that are executed by the execute() method
    // in order by the ordinal property of each phase
    private Phase[] phases = {
        null, // ANY_PHASE placeholder, not a real Phase
        new Meth4jRestoreViewPhase(),
        new ApplyRequestValuesPhase(),
        new ProcessValidationsPhase(),
        new UpdateModelValuesPhase(),
        new InvokeApplicationPhase(),
        response
    };

    // List for registered PhaseListeners
    private List<PhaseListener> listeners =
          new CopyOnWriteArrayList<PhaseListener>();
    private boolean isClientWindowEnabled = false;
    private WebConfiguration config;
    
    public Meth4jLifecycleImpl() {
        
    }

    public Meth4jLifecycleImpl(FacesContext context) {
        final ExternalContext extContext = context.getExternalContext();
        config = WebConfiguration.getInstance(extContext);
        context.getApplication().subscribeToEvent(PostConstructApplicationEvent.class,
                         Application.class, new PostConstructApplicationListener());
        
    }
    
    private class PostConstructApplicationListener implements SystemEventListener {

        public boolean isListenerForSource(Object source) {
            return source instanceof Application;
        }

        public void processEvent(SystemEvent event) throws AbortProcessingException {
        	Meth4jLifecycleImpl.this.postConstructApplicationInitialization();
        }
        
    }
    
    private void postConstructApplicationInitialization() {
        final String optionValue = config.getOptionValue(WebConfiguration.WebContextInitParameter.ClientWindowMode);
        isClientWindowEnabled = (null != optionValue) && !optionValue.equals(WebConfiguration.WebContextInitParameter.ClientWindowMode.getDefaultValue());
    }

    // ------------------------------------------------------- Lifecycle Methods

    @Override
    public void attachWindow(FacesContext context) {
        if (!isClientWindowEnabled) {
            return;
        }
        if (context == null) {
            throw new NullPointerException
                (MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context"));
        }

        final ExternalContext extContext = context.getExternalContext();
        ClientWindow myWindow = extContext.getClientWindow();
        if (null == myWindow) {
            myWindow = new ClientWindowImpl();
            myWindow.decode(context);
            extContext.setClientWindow(myWindow);
            
        }
        
        
        // If you need to do the "send down the HTML" trick, be sure to
        // mark responseComplete true after doing so.  That way
        // the remaining lifecycle methods will not execute.
        
    }
    
    // Execute the phases up to but not including Render Response
    public void execute(FacesContext context) throws FacesException {

        if (context == null) {
            throw new NullPointerException
                (MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context"));
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("execute(" + context + ")");
        }

        for (int i = 1, len = phases.length -1 ; i < len; i++) { // Skip ANY_PHASE placeholder

            if (context.getRenderResponse() ||
                context.getResponseComplete()) {
                break;
            }

            phases[i].doPhase(context, this, listeners.listIterator());

        }

    }


    // Execute the Render Response phase
    public void render(FacesContext context) throws FacesException {

        if (context == null) {
            throw new NullPointerException
                (MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context"));
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("render(" + context + ")");
        }

        if (!context.getResponseComplete()) {
            response.doPhase(context, this, listeners.listIterator());
        }

    }


    // Add a new PhaseListener to the set of registered listeners
    public void addPhaseListener(PhaseListener listener) {

        if (listener == null) {
            throw new NullPointerException
                  (MessageUtils.getExceptionMessageString
                        (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "listener"));
        }

        if (listeners == null) {
            listeners = new CopyOnWriteArrayList<PhaseListener>();
        }

        if (listeners.contains(listener)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "jsf.lifecycle.duplicate_phase_listener_detected",
                           listener.getClass().getName());
            }
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "addPhaseListener({0},{1})",
                           new Object[]{
                                 listener.getPhaseId().toString(),
                                 listener.getClass().getName()});
            }
            listeners.add(listener);
        }

    }


    // Return the set of PhaseListeners that have been registered
    public PhaseListener[] getPhaseListeners() {

        return listeners.toArray(new PhaseListener[listeners.size()]);

    }


    // Remove a registered PhaseListener from the set of registered listeners
    public void removePhaseListener(PhaseListener listener) {

        if (listener == null) {
            throw new NullPointerException
                  (MessageUtils.getExceptionMessageString
                        (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "listener"));
        }

        if (listeners.remove(listener) && LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE,
                       "removePhaseListener({0})",
                       new Object[]{listener.getClass().getName()});
        }

    }
}
