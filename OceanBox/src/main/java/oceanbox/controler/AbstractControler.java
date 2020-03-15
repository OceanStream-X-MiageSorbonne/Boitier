package oceanbox.controler;

import oceanbox.model.AbstractModel;

public abstract class AbstractControler {
	protected AbstractModel model;

	public AbstractControler(AbstractModel model) {
		this.model = model;
	}

	abstract void control();
}
