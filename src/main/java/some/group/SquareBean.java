package some.group;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.meth4j.futil.Message;
import org.meth4j.futil.Messages;

import com.google.common.base.Preconditions;

@ManagedBean
@RequestScoped
public class SquareBean {

	private Integer side;
	private UnitOfLength unit;
	
	public String action() {
		Preconditions.checkNotNull(side);
		Preconditions.checkNotNull(unit);
		double area = SquareMethods.area(side, unit);
		Messages.flash(Message.info(Key.INFO_SUCCESS));
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
