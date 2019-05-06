package br.com.witt.query;

import java.util.ArrayList;
import java.util.List;

public abstract class BooleanOperator extends Condition {

	private List<Condition> conditions;

	protected BooleanOperator(Condition condition) {
		conditions = new ArrayList<Condition>();
		conditions.add(condition);
	}

	public void addCondition(Condition condition) {
		conditions.add(condition);
	}

	protected List<Condition> getConditions() {
		return conditions;
	}

	protected Condition getLast() {
		return conditions.get(conditions.size() - 1);
	}

	protected boolean isComplete() {
		return conditions.size() > 1;
	}
}
