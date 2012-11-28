package some.group;

import static org.meth4j.futil.CommonKey.ERROR_TOO_LARGE;
import static org.meth4j.futil.Message.errMesg;
import static org.meth4j.futil.Validate.notNegativeInt;
import static org.meth4j.futil.Validate.notNullString;
import static org.meth4j.futil.Validate.parseInt;
import static some.group.SquareMethods.area;
import static some.group.UnitOfLength.valueOf;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("sg.sideValidator")
public class SideValidator implements Validator {
	
	@Override public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		final int num = notNegativeInt(parseInt(notNullString(value)));
		final UISelectOne select = (UISelectOne) component.getAttributes().get("attr");
		final UnitOfLength unit = valueOf((String) select.getSubmittedValue());
		final double area = area(num, unit);
		if (area >= 1) {
			throw new ValidatorException(errMesg(ERROR_TOO_LARGE, num));
		}
	}
	
	
}
