package br.com.witt.query;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class And extends BooleanOperator {

	public And(Condition firstCondition) {
		super(firstCondition);
	}

	@Override
	protected Criterion getCriterion() {
		Conjunction and = Restrictions.conjunction();
		for (Condition condition : getConditions()) {
			and.add(condition.getCriterion());
		}
		return and;
	}
}
