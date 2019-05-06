package br.com.witt.query;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class Not extends Condition {

	private Condition conditionToNegate;

	public Not() {
	}
	
	public boolean isEmpty() {
		return conditionToNegate == null;
	}

	public void setConditionToNegate(Condition conditionToNegate) {
		this.conditionToNegate = conditionToNegate;
	}

	@Override
	protected Criterion getCriterion() {
		return Restrictions.not(conditionToNegate.getCriterion());
	}
}
