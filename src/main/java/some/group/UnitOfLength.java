package some.group;

import org.meth4j.futil.IKey;

public enum UnitOfLength implements IKey {

//	@formatter:off
	MILLIMETERS(0.001, Key.LABEL_MM), 
	CENTIMETERS(0.01, Key.LABEL_CM), 
	INCH(0.0254, Key.LABEL_INCH);
//	@formatter:on

	private final double meters;
	private final IKey _key;

	private UnitOfLength(double meters, IKey key) {
		this.meters = meters;
		this._key = key;
	}

	@Override public String getKey() {
		return name();
	}

	@Override public String getLabel() {
		return _key.getLabel();
	}

	public double getMeters() {
		return meters;
	}

	public String toString() {
		return String.format("[%s|%f]", name(), meters);
	}

}
