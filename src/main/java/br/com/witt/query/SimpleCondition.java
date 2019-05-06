package br.com.witt.query;

import org.hibernate.criterion.Criterion;

public class SimpleCondition extends Condition {

	private Criterion criterion;

	public SimpleCondition(Criterion criterion) {
		this.criterion = criterion;
	}

	@Override
	protected Criterion getCriterion() {
		return criterion;
	}
}
