package some.group;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import org.meth4j.futil.Util;


@ManagedBean
@RequestScoped
public class ContentsOf {

	private List<SelectItem> selectItems;

	@PostConstruct public void init() {
		selectItems = Util.toSelectItems(UnitOfLength.values());
	}

	public List<SelectItem> getUnits() {
		return selectItems;
	}

}
