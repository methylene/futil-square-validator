package some.group;

import static javax.faces.event.PhaseId.RESTORE_VIEW;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

public class MessageListener implements PhaseListener {

	private static final long serialVersionUID = 14L;
	
	@Override public void afterPhase(PhaseEvent event) {
		final FacesContext fc = event.getFacesContext();
		final ExternalContext ec = fc.getExternalContext();
		final HttpServletRequest r = (HttpServletRequest) ec.getRequest();
		Messages.clearMesg(r);
	}

	@Override public void beforePhase(PhaseEvent event) {
		final FacesContext fc = event.getFacesContext();
		final ExternalContext ec = fc.getExternalContext();
		final HttpServletRequest r = (HttpServletRequest) ec.getRequest();
		Messages.restoreMesg(r);
	}

	@Override public PhaseId getPhaseId() {
		return RESTORE_VIEW;
	}

}
