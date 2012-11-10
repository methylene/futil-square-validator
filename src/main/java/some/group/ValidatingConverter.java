package some.group;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public abstract class ValidatingConverter implements Validator {

	private static final String ERRORS = "org.meth4j.futil.errors";
	private static final String ATTRIBUTE = "org.meth4j.futil.attribute";

	protected abstract void validate(FacesContext facesContext, UIComponent component, Object value,
			UIComponent attribute) throws ValidatorException;

	@Override public final void validate(final FacesContext fc, final UIComponent component, final Object value) {
		try {
			validate(fc, component, value, (UIComponent) component.getAttributes().get(ATTRIBUTE));
		} catch (final ValidatorException e) {
			final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
			@SuppressWarnings("unchecked")
			Map<String, Integer> errors = (Map<String, Integer>) requestMap.get(ERRORS);
			if (errors == null) {
				errors = new HashMap<String, Integer>();
				requestMap.put(ERRORS, errors);
			}
			errors.put(component.getId(), 1);
			throw e;
		}
	}

}