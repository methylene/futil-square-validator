package some.group;


import static some.group.Key.ERROR_TOO_LARGE;
import static some.group.Message.errMesg;
import static some.group.SquareMethods.area;
import static some.group.UnitOfLength.valueOf;
import static some.group.Validate.integer;
import static some.group.Validate.positive;
import static some.group.Validate.requiredString;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;


@FacesValidator("sg.sideValidator")
public class SideValidator extends ValidatingConverter {
	
	@Override protected void validate(FacesContext facesContext, UIComponent component, Object value,
			UIComponent attribute) throws ValidatorException {
		final Integer num = positive(integer(requiredString(value)));
		final UISelectOne select = (UISelectOne) attribute;
		final UnitOfLength unit = valueOf((String) select.getSubmittedValue());
		final double area = area(num, unit);
		if (area >= 1) {
			throw new ValidatorException(errMesg(ERROR_TOO_LARGE));
		}
	}
	
}
