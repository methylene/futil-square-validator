package some.group;

import static com.google.common.base.Preconditions.checkNotNull;
import static some.group.Message.infoMesg;
import static some.group.Messages.addMesg;
import static some.group.SquareMethods.area;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class SquareBean {

	private Integer side;
	private UnitOfLength unit;
	private double area;

	public String action() {
		checkNotNull(side);
		checkNotNull(unit);
		area = area(side, unit);
		addMesg(infoMesg(Key.INFO_SUCCESS, side, unit.getLabel()));
		return "/outcome";
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

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

}