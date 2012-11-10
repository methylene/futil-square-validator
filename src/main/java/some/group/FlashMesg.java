package some.group;

import static com.google.common.base.Preconditions.checkNotNull;

import static some.group.Message.errMesg;
import static some.group.Message.infoMesg;
import static some.group.Message.warnMesg;

import java.io.Serializable;

import javax.faces.application.FacesMessage;

public class FlashMesg implements Serializable {

	private static final long serialVersionUID = 12L;

	private static enum Severity {
		INFO, WARNING, TERROR
	}

	private final IKey key;
	private final Severity severity;

	private FlashMesg(IKey key, Severity severity) {
		super();
		this.key = key;
		this.severity = severity;
	}

	public static FlashMesg flashInfo(IKey key) {
		checkNotNull(key);
		return new FlashMesg(key, Severity.INFO);
	}

	public static FlashMesg flashWarn(IKey key) {
		checkNotNull(key);
		return new FlashMesg(key, Severity.WARNING);
	}

	public static FlashMesg flashErr(IKey key) {
		checkNotNull(key);
		return new FlashMesg(key, Severity.TERROR);
	}

	public FacesMessage toFacesMessage() {
		switch (severity) {
		case INFO:
			return infoMesg(key);
		case WARNING:
			return warnMesg(key);
		case TERROR:
			return errMesg(key);
		default:
			throw new IllegalStateException();
		}
	}

}
