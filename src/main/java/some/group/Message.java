package some.group;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import static javax.faces.application.FacesMessage.SEVERITY_WARN;

import java.text.MessageFormat;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

public final class Message {

	private Message() {
	}

	public static FacesMessage infoMesg(final IKey message, final Object... params) {
		return _mesg(message, SEVERITY_INFO, params);
	}

	public static FacesMessage warnMesg(final IKey message, final Object... params) {
		return _mesg(message, SEVERITY_WARN, params);
	}

	public static FacesMessage errMesg(final IKey message, final Object... params) {
		return _mesg(message, SEVERITY_ERROR, params);
	}

	private static FacesMessage _mesg(final IKey message, Severity severity, final Object... params) {
		final String msgFormat = message.getLabel();
		if (params == null || params.length == 0) {
			return new FacesMessage(severity, msgFormat, msgFormat);
		} else {
			final String msg = new MessageFormat(msgFormat).format(params);
			return new FacesMessage(severity, msg, msg);
		}
	}

}