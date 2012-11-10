package some.group;

import static com.google.common.primitives.Ints.tryParse;
import static some.group.Key.*;
import static some.group.Message.errMesg;

import javax.faces.validator.ValidatorException;

public class Validate {
	
	public static Integer integer(String value) {
		final Integer num = tryParse(value);
		if (num == null || num < 1) {
			throw new ValidatorException(errMesg(ERROR_INTEGER));
		} else {
			return num;
		}
	}
	
	public static Integer positive(Integer value) {
		if (value < 1) {
			throw new ValidatorException(errMesg(ERROR_POSITIVE_NATURAL_NUMBER));
		} else {
			return value;
		}
	}
	
	public static final String requiredString(Object value) throws ValidatorException {
		if (value == null || value.toString().isEmpty()) {
			throw new ValidatorException(errMesg(ERROR_REQUIRED));
		} else {
			return value.toString();
		}
	}
	
	public static final void required(String value) throws ValidatorException {
		if (value == null || value.isEmpty()) {
			throw new ValidatorException(errMesg(ERROR_REQUIRED));
		}
	}
	
	public static final void maxLength(String value, int maxLength) throws ValidatorException {
		if (value != null) {
			if (value.length() > maxLength) {
				throw new ValidatorException(errMesg(ERROR_TOO_LONG));
			}
		}
	}
	
	public static final void minLength(String value, int minLength) throws ValidatorException {
		if (value != null) {
			if (value.length() < minLength) {
				throw new ValidatorException(errMesg(ERROR_TOO_SHORT));
			}
		}
	}
	
}
