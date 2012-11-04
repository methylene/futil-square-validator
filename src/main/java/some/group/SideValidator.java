package some.group;

import static org.meth4j.futil.CommonKey.ERROR_TOO_LARGE;
import static org.meth4j.futil.Message.errMesg;
import static org.meth4j.futil.Validate.integer;
import static org.meth4j.futil.Validate.positive;
import static org.meth4j.futil.Validate.requiredString;
import static some.group.SquareMethods.area;
import static some.group.UnitOfLength.valueOf;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

import org.meth4j.futil.ValidatingConverter;

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
