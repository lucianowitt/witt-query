package br.com.witt.query;

import org.hibernate.criterion.Criterion;

public abstract class Condition {

	protected abstract Criterion getCriterion();
}
