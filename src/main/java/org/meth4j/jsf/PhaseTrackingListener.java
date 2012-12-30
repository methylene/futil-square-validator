package org.meth4j.jsf;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Iterables.concat;
import static javax.faces.event.PhaseId.ANY_PHASE;

import java.util.List;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.google.common.collect.ImmutableList;
import com.industrieit.jsf.stateless.impl.SJSFStatePool;
import com.industrieit.jsf.stateless.impl.SJSFURIBuilder;

public class PhaseTrackingListener implements PhaseListener {
	
	private static final long serialVersionUID = 14L;
	private static final String TRACKER = "org.meth4j.jsf.PHASE_TRACKER";
	private static final String VR_FROM_CACHE = "org.meth4j.jsf.VR_FROM_CACHE";
	
	public static class PhaseTracker {
		
		private final List<PhaseId> ids;
		
		private PhaseTracker(List<PhaseId> ids) {
			super();
			this.ids = ids;
		}
		
		public static PhaseTracker track(final PhaseEvent evt) {
			final PhaseId phaseId = evt.getPhaseId();
			final FacesContext fc = evt.getFacesContext();
			final Map<Object, Object> attrs = fc.getAttributes();
			final PhaseTracker tracker = (PhaseTracker) attrs.get(TRACKER);
			final ImmutableList<PhaseId> wrapped = ImmutableList.of(phaseId);
			final List<PhaseId> updatedList;
			if (tracker == null) {
				updatedList = wrapped;
			} else {
				updatedList = copyOf(concat(tracker.ids, wrapped));
			}
			final PhaseTracker updatedTracker = new PhaseTracker(updatedList);
			attrs.put(TRACKER, updatedTracker);
			return updatedTracker;
		}
		
		public List<PhaseId> getPhaseIds() {
			return ids;
		}
	}

	@Override public PhaseId getPhaseId() {
		return ANY_PHASE;
	}

	@Override public void afterPhase(PhaseEvent event) {
	}

	@Override public void beforePhase(PhaseEvent evt) {
		if (evt.getPhaseId().equals(PhaseId.RESTORE_VIEW)) {
			final UIViewRoot vr = SJSFStatePool.get(SJSFURIBuilder.getURI());
			final String fromCache = vr == null ? "--none--" : vr.getViewId();
			final FacesContext fc = evt.getFacesContext();
			final Map<Object, Object> attrs = fc.getAttributes();
			attrs.put(VR_FROM_CACHE, fromCache);
		}
		PhaseTracker.track(evt);
	}

}
