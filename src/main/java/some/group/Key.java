package some.group;

import org.meth4j.futil.IKey;
import org.meth4j.futil.Messages;


public enum Key implements IKey {
	
//	@formatter:off
	LABEL_INCH,
	LABEL_MM,
	LABEL_CM, 
	INFO_SUCCESS,
;//	@formatter:on
	
	@Override public String getLabel() {
		return Messages.getBundle().getString(getKey());
	}

	@Override public String getKey() {
		return name().toLowerCase();
	}
	
}
