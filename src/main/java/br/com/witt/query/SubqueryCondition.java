package br.com.witt.query;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Subqueries;

public class SubqueryCondition extends Condition {

	private Query subquery;
	private SubqueryOperator operator;
	private String property;
	private Object value;

	private SubqueryCondition(Object value, String property, SubqueryOperator operator, Query subquery) {
		this.operator = operator;
		this.subquery = subquery;
		this.property = property;
		this.value = value;

		if (this.operator == null) {
			throw new RuntimeException("Operator is mandatory");
		}
		if (this.subquery == null) {
			throw new RuntimeException("Subquery is mandatory");
		}
		if (!this.subquery.select().hasSingleProjection()) {
			throw new RuntimeException("Subquery must have one projection");
		}
		if (this.value != null && !this.operator.isValue()) {
			throw new RuntimeException("Operator is not allowed for value subquery condition");
		}
		if (this.property != null && !this.operator.isProperty()) {
			throw new RuntimeException("Operator is not allowed for property subquery condition");
		}
		if (this.value == null && this.operator.isValue()) {
			throw new RuntimeException("Value is mandatory for this operator");
		}
		if (this.property == null && this.operator.isProperty()) {
			throw new RuntimeException("Property is mandatory for this operator");
		}
	}

	public SubqueryCondition(SubqueryOperator operator, Query subquery) {
		this(null, null, operator, subquery);
	}

	public SubqueryCondition(String property, SubqueryOperator operator, Query subquery) {
		this(null, property, operator, subquery);
	}

	public SubqueryCondition(Object value, SubqueryOperator operator, Query subquery) {
		this(value, null, operator, subquery);
	}

	@Override
	protected Criterion getCriterion() {
		Criterion criterion;

		switch (operator) {
		case EXISTS:
			criterion = Subqueries.exists(subquery.getCriteria());
			break;
		case NOT_EXISTS:
			criterion = Subqueries.notExists(subquery.getCriteria());
			break;
		case EQ:
			criterion = Subqueries.eq(value, subquery.getCriteria());
			break;
		case NE:
			criterion = Subqueries.ne(value, subquery.getCriteria());
			break;
		case GT:
			criterion = Subqueries.gt(value, subquery.getCriteria());
			break;
		case GE:
			criterion = Subqueries.ge(value, subquery.getCriteria());
			break;
		case LT:
			criterion = Subqueries.lt(value, subquery.getCriteria());
			break;
		case LE:
			criterion = Subqueries.le(value, subquery.getCriteria());
			break;
		case PEQ:
			criterion = Subqueries.propertyEq(property, subquery.getCriteria());
			break;
		case PNE:
			criterion = Subqueries.propertyNe(property, subquery.getCriteria());
			break;
		case PGT:
			criterion = Subqueries.propertyGt(property, subquery.getCriteria());
			break;
		case PGE:
			criterion = Subqueries.propertyGe(property, subquery.getCriteria());
			break;
		case PLT:
			criterion = Subqueries.propertyLt(property, subquery.getCriteria());
			break;
		case PLE:
			criterion = Subqueries.propertyLe(property, subquery.getCriteria());
			break;
		case PIN:
			criterion = Subqueries.propertyIn(property, subquery.getCriteria());
			break;
		case PNIN:
			criterion = Subqueries.propertyNotIn(property, subquery.getCriteria());
			break;
		default:
			throw new RuntimeException("Unsupported subquery operator");
		}

		return criterion;
	}
}
