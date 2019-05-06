package br.com.witt.query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.sql.JoinType;

public class Join {

	private String path;
	private String alias;
	private JoinType type;
	private Where on;

	protected Join(String path, String alias, JoinType type) {
		this.path = path;
		this.alias = alias;
		this.type = type;
	}

	public Where on() {
		if (on == null) {
			on = new Where(null);
		}
		return on;
	}

	protected void apply(DetachedCriteria criteria) {
		if (on == null) {
			criteria.createAlias(path, alias, type);
		} else {
			criteria.createAlias(path, alias, type, on.getCriterion());
		}
	}
}
