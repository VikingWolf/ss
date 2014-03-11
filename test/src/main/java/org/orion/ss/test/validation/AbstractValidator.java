package org.orion.ss.test.validation;

public abstract class AbstractValidator {

	protected String message;

	public abstract boolean validate(Object input);

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
