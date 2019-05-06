package br.com.witt.query;

import java.util.Collection;
import java.util.Stack;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class Where extends Condition {

	private Where parentBlock;
	private Condition condition;
	private Stack<Boolean> enabled;
	private boolean ended;

	protected Where(Where parentBlock) {
		this.parentBlock = parentBlock;
		enabled = new Stack<Boolean>();
		enabled.push(Boolean.TRUE);
		ended = false;
	}

	@Override
	protected Criterion getCriterion() {
		if (isEmpty() || (hasParent() && !isEnded())) {
			throw new RuntimeException("Block is not ended");
		}
		return condition.getCriterion();
	}

	private boolean hasParent() {
		return parentBlock != null;
	}

	private boolean isEmpty() {
		return condition == null;
	}

	private void end() {
		ended = true;
	}

	private boolean isEnded() {
		return ended;
	}

	private boolean isEnabled() {
		return Boolean.TRUE.equals(enabled.peek());
	}

	private void addCondition(Condition condition) {
		if (isEnabled()) {
			if (isEmpty()) {
				this.condition = condition;
			} else if (this.condition instanceof BooleanOperator) {
				BooleanOperator operator = (BooleanOperator) this.condition;
				if (operator.isComplete() && operator.getLast() instanceof Not) {
					Not not = (Not) operator.getLast();
					not.setConditionToNegate(condition);
				} else {
					operator.addCondition(condition);
				}
			} else if (this.condition instanceof Not) {
				Not not = (Not) this.condition;
				not.setConditionToNegate(condition);
			} else {
				throw new RuntimeException("Bad syntax");
			}
		}
	}

	public Where blockBegin() {
		if (isEnabled()) {
			Where block = new Where(this);
			addCondition(block);
			return block;
		} else {
			return this;
		}
	}

	public Where blockEnd() {
		if (isEnabled()) {
			this.end();
			return (hasParent() ? parentBlock : this);
		} else {
			return this;
		}
	}

	public Where ifBegin(boolean expression) {
		if (isEnabled()) {
			enabled.push(Boolean.valueOf(expression));
		} else {
			enabled.push(Boolean.FALSE);
		}
		return this;
	}

	public Where ifEnd() {
		enabled.pop();
		return this;
	}

	public Where and() {
		if (!isEmpty()) {
			if (isEnabled()) {
				if (condition instanceof Where) {
					Where block = (Where) this.condition;
					if (block.isEmpty()) {
						throw new RuntimeException("And operator must have a left side");
					} else if (block.isEnded()) {
						condition = new And(condition);
					} else {
						throw new RuntimeException("Block not ended");
					}
				} else if (!(condition instanceof And)) {
					condition = new And(condition);
				}
			}
			return this;
		} else {
			throw new RuntimeException("And operator must have a left side");
		}
	}

	public Where or() {
		if (!isEmpty()) {
			if (isEnabled()) {
				if (condition instanceof Where) {
					Where block = (Where) this.condition;
					if (block.isEmpty()) {
						throw new RuntimeException("Or operator must have a left side");
					} else if (block.isEnded()) {
						condition = new Or(condition);
					} else {
						throw new RuntimeException("Block not ended");
					}
				} else if (!(condition instanceof Or)) {
					condition = new Or(condition);
				}
			}
			return this;
		} else {
			throw new RuntimeException("Or operator must have a left side");
		}
	}

	public Where not() {
		addCondition(new Not());
		return this;
	}

	public Where eq(String property, Object value) {
		addCondition(new SimpleCondition(Restrictions.eq(property, value)));
		return this;
	}

	public Where ne(String property, Object value) {
		addCondition(new SimpleCondition(Restrictions.ne(property, value)));
		return this;
	}

	public Where gt(String property, Object value) {
		addCondition(new SimpleCondition(Restrictions.gt(property, value)));
		return this;
	}

	public Where ge(String property, Object value) {
		addCondition(new SimpleCondition(Restrictions.ge(property, value)));
		return this;
	}

	public Where lt(String property, Object value) {
		addCondition(new SimpleCondition(Restrictions.lt(property, value)));
		return this;
	}

	public Where le(String property, Object value) {
		addCondition(new SimpleCondition(Restrictions.le(property, value)));
		return this;
	}

	public Where like(String property, String value) {
		if (value != null) {
			addCondition(new SimpleCondition(Restrictions.ilike(property, value, MatchMode.ANYWHERE)));
		}
		return this;
	}

	public Where sql(String sql) {
		addCondition(new SimpleCondition(Restrictions.sqlRestriction(sql)));
		return this;
	}

	public Where in(String property, Object... values) {
		addCondition(new SimpleCondition(Restrictions.in(property, values)));
		return this;
	}

	public Where in(String property, Collection<Object> values) {
		addCondition(new SimpleCondition(Restrictions.in(property, values)));
		return this;
	}

	public Where notIn(String property, Object... values) {
		addCondition(new SimpleCondition(Restrictions.not(Restrictions.in(property, values))));
		return this;
	}

	public Where notIn(String property, Collection<Object> values) {
		addCondition(new SimpleCondition(Restrictions.not(Restrictions.in(property, values))));
		return this;
	}

	public Where peq(String property1, String property2) {
		addCondition(new SimpleCondition(Restrictions.eqProperty(property1, property2)));
		return this;
	}

	public Where pne(String property1, String property2) {
		addCondition(new SimpleCondition(Restrictions.neProperty(property1, property2)));
		return this;
	}

	public Where pgt(String property1, String property2) {
		addCondition(new SimpleCondition(Restrictions.gtProperty(property1, property2)));
		return this;
	}

	public Where pge(String property1, String property2) {
		addCondition(new SimpleCondition(Restrictions.geProperty(property1, property2)));
		return this;
	}

	public Where plt(String property1, String property2) {
		addCondition(new SimpleCondition(Restrictions.ltProperty(property1, property2)));
		return this;
	}

	public Where ple(String property1, String property2) {
		addCondition(new SimpleCondition(Restrictions.leProperty(property1, property2)));
		return this;
	}

	public Where exists(Query subquery) {
		addCondition(new SubqueryCondition(SubqueryOperator.EXISTS, subquery));
		return this;
	}

	public Where notExists(Query subquery) {
		addCondition(new SubqueryCondition(SubqueryOperator.NOT_EXISTS, subquery));
		return this;
	}

	public Where eq(Object value, Query subquery) {
		addCondition(new SubqueryCondition(value, SubqueryOperator.EQ, subquery));
		return this;
	}

	public Where ne(Object value, Query subquery) {
		addCondition(new SubqueryCondition(value, SubqueryOperator.NE, subquery));
		return this;
	}

	public Where gt(Object value, Query subquery) {
		addCondition(new SubqueryCondition(value, SubqueryOperator.GT, subquery));
		return this;
	}

	public Where ge(Object value, Query subquery) {
		addCondition(new SubqueryCondition(value, SubqueryOperator.GE, subquery));
		return this;
	}

	public Where lt(Object value, Query subquery) {
		addCondition(new SubqueryCondition(value, SubqueryOperator.LT, subquery));
		return this;
	}

	public Where le(Object value, Query subquery) {
		addCondition(new SubqueryCondition(value, SubqueryOperator.LE, subquery));
		return this;
	}

	public Where peq(String property, Query subquery) {
		addCondition(new SubqueryCondition(property, SubqueryOperator.PEQ, subquery));
		return this;
	}

	public Where pne(String property, Query subquery) {
		addCondition(new SubqueryCondition(property, SubqueryOperator.PNE, subquery));
		return this;
	}

	public Where pgt(String property, Query subquery) {
		addCondition(new SubqueryCondition(property, SubqueryOperator.PGT, subquery));
		return this;
	}

	public Where pge(String property, Query subquery) {
		addCondition(new SubqueryCondition(property, SubqueryOperator.PGE, subquery));
		return this;
	}

	public Where plt(String property, Query subquery) {
		addCondition(new SubqueryCondition(property, SubqueryOperator.PLT, subquery));
		return this;
	}

	public Where ple(String property, Query subquery) {
		addCondition(new SubqueryCondition(property, SubqueryOperator.PLE, subquery));
		return this;
	}

	public Where in(String property, Query subquery) {
		addCondition(new SubqueryCondition(property, SubqueryOperator.PIN, subquery));
		return this;
	}

	public Where notIn(String property, Query subquery) {
		addCondition(new SubqueryCondition(property, SubqueryOperator.PNIN, subquery));
		return this;
	}
}
