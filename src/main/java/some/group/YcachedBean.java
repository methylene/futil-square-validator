package some.group;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
@ApplicationScoped
public class YcachedBean {
	
	private int count = 0;

	public String formSubmit() {
		count++;
		FacesContext.getCurrentInstance().getAttributes().put("count", Integer.toString(count));
		if (count % 2 == 1) {
			return "/ycached_because";
		} else {
			return null;
		}
	}

	public int getCount() {
		return count;
	}

}
