package some.group;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.meth4j.futil.FlashMesg.info;
import static org.meth4j.futil.Messages.flash;
import static some.group.SquareMethods.area;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class SquareBean {

	private Integer side;
	private UnitOfLength unit;
	
	public String action() {
		checkNotNull(side);
		checkNotNull(unit);
		final double area = area(side, unit);
		flash(info(Key.INFO_SUCCESS));
		return "/outcome?faces-redirect=true&amp;area="+area;
	}

	public UnitOfLength getUnit() {
		return unit;
	}

	public void setUnit(UnitOfLength unit) {
		this.unit = unit;
	}

	public Integer getSide() {
		return side;
	}

	public void setSide(Integer side) {
		this.side = side;
	}

}
