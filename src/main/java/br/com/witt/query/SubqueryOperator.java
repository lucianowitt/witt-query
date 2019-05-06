package br.com.witt.query;

public enum SubqueryOperator {

	EXISTS(false, false), //
	NOT_EXISTS(false, false), //

	EQ(false, true), //
	NE(false, true), //
	GT(false, true), //
	GE(false, true), //
	LT(false, true), //
	LE(false, true), //

	PEQ(true, false), //
	PNE(true, false), //
	PGT(true, false), //
	PGE(true, false), //
	PLT(true, false), //
	PLE(true, false), //
	PIN(true, false), //
	PNIN(true, false);

	private boolean property;
	private boolean value;

	private SubqueryOperator(boolean property, boolean value) {
		this.property = property;
		this.value = value;
	}

	public boolean isProperty() {
		return property;
	}

	public boolean isValue() {
		return value;
	}
}
