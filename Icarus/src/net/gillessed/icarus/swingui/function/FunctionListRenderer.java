package net.gillessed.icarus.swingui.function;

import net.gillessed.icarus.Function;
import net.gillessed.icarus.swingui.components.CustomListRenderer;

public class FunctionListRenderer extends CustomListRenderer<Function> {

	private static final long serialVersionUID = 1L;

	@Override
	protected String getListString(Function value, int index) {
		return "Function " + (index + 1);
	}

}
