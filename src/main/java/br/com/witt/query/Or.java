package br.com.witt.query;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

public class Or extends BooleanOperator {

	public Or(Condition firstCondition) {
		super(firstCondition);
	}

	@Override
	protected Criterion getCriterion() {
		Disjunction or = Restrictions.disjunction();
		for (Condition condition : getConditions()) {
			or.add(condition.getCriterion());
		}
		return or;
	}
}
