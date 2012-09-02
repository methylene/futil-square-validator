package some.group;

import static org.meth4j.futil.Validate.integer;
import static org.meth4j.futil.Validate.positive;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import org.meth4j.futil.CommonKey;
import org.meth4j.futil.ValidatingIntConverter;
import org.meth4j.futil.ValidatorException;
import org.slf4j.LoggerFactory;

@FacesConverter("sg.sideValidator")
public class SideValidator extends ValidatingIntConverter {

	@Override protected void validate(FacesContext facesContext, UIComponent component, String value,
			UIComponent attribute) throws ValidatorException {
		Integer num = positive(integer(value.toString()));
		UISelectOne unitComponent = (UISelectOne) attribute;
		String unit_$ = (String) unitComponent.getSubmittedValue();
		UnitOfLength unit = UnitOfLength.valueOf(unit_$);
		double area = SquareMethods.area(num, unit);
		boolean valid = area < 1;
		if (!valid) {
			throw new ValidatorException(CommonKey.ERROR_TOO_LARGE);
		}
	}

}
