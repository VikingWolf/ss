package org.orion.ss.test.validation;

public class DoubleValidator extends AbstractValidator {

	public final static int MODE_ALL = 1;
	public final static int MODE_POSITIVE = 1;
	public final static int MODE_NEGATIVE = 2;

	private int mode = MODE_ALL;

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	@Override
	public boolean validate(Object input) {
		boolean result = true;
		if (input == null) {
			setMessage("The input is null");
			result = false;
		} else {
			try {
				String item = (String) input;
				double value = Double.parseDouble(item);
				if (mode == MODE_POSITIVE && value < 0) {
					setMessage("The input must be positive.");
					result = false;

				} else if (mode == MODE_NEGATIVE && value > 0) {
					setMessage("The input must be negative.");
					result = false;
				}
			} catch (ClassCastException e) {
				setMessage("The input is not a String.");
				result = false;
			} catch (NumberFormatException e) {
				setMessage("The input cannot be converted into a double");
				result = false;
			}
		}
		return result;
	}
}
