package some.group;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import com.google.common.collect.ImmutableList;



@ManagedBean
@RequestScoped
public class ContentsOf {

	private List<SelectItem> selectItems;

	@PostConstruct public void init() {
		selectItems = toSelectItems(UnitOfLength.values());
	}

	public List<SelectItem> getUnits() {
		return selectItems;
	}
	
	
	public static List<SelectItem> toSelectItems(IKey[] kvs) {
		final ImmutableList.Builder<SelectItem> unitBuilder = ImmutableList.builder();
		for (final IKey kv: kvs) {
			unitBuilder.add(new SelectItem(kv.getKey(), kv.getLabel()));
		}
		return unitBuilder.build();
	}

}
