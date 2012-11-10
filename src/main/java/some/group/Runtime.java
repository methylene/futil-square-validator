package some.group;

import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class Runtime {
	
	public String getFreeMemory() {
		return format("%d kB", getRuntime().freeMemory() / 1024);
	}

}
