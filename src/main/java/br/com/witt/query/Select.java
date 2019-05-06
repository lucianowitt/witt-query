package br.com.witt.query;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;

public class Select {

	private boolean empty;
	private boolean single;
	private boolean distinct;
	private List<Projection> projections;
	protected ResultTransformer resultTransformer;

	protected Select(Query query) {
		empty = true;
		single = false;
		distinct = false;
		projections = new ArrayList<Projection>();
		resultTransformer = null;
	}

	/**
	 * Adds the primary key of the root entity to the SELECT part of the SQL.
	 * 
	 * @return This {@link Select select}
	 */
	public Select id() {
		add(Projections.id());
		return this;
	}

	/**
	 * Adds the primary key of the root entity to the SELECT part of the SQL, with
	 * the given alias.
	 * 
	 * @param alias
	 * @return This {@link Select select}
	 */
	public Select id(String alias) {
		add(Projections.id(), alias);
		return this;
	}

	/**
	 * Adds a COUNT(*) to the SELECT part of the SQL.
	 * 
	 * @return {@link Select select}
	 */
	public Select rowCount() {
		add(Projections.rowCount());
		return this;
	}

	/**
	 * Adds a COUNT(*) to the SELECT part of the SQL, with the given alias.
	 * 
	 * @param alias
	 * @return {@link Select select}
	 */
	public Select rowCount(String alias) {
		add(Projections.rowCount(), alias);
		return this;
	}

	/**
	 * Adds the DISTINCT to the beginning of the SELECT part of the SQL.
	 * 
	 * @return {@link Select select}
	 */
	public Select distinct() {
		distinct = true;
		return this;
	}

	/**
	 * Adds the given prperty to the SELECT part of the SQL.
	 * 
	 * @param property
	 * @return {@link Select select}
	 */
	public Select property(String property) {
		add(Projections.property(property));
		return this;
	}

	/**
	 * Adds the given property to the SELECT part of the SQL, with the given alias.
	 * 
	 * @param property
	 * @param alias
	 * @return {@link Select select}
	 */
	public Select property(String property, String alias) {
		add(Projections.property(property), alias);
		return this;
	}

	/**
	 * Adds the given property to the SELECT and the GROUP BY parts of the SQL.
	 * 
	 * @param property
	 * @return {@link Select select}
	 */
	public Select group(String property) {
		add(Projections.groupProperty(property));
		return this;
	}

	/**
	 * Adds the given property to the SELECT and the GROUP BY parts of the SQL, with
	 * the given alias.
	 * 
	 * @param property
	 * @param alias
	 * @return {@link Select select}
	 */
	public Select group(String property, String alias) {
		add(Projections.groupProperty(property), alias);
		return this;
	}

	/**
	 * Adds the MAX aggregation function, with the given property as argument, to
	 * the SELECT part of the SQL, with the given alias.
	 * 
	 * @param property
	 * @param alias
	 * @return {@link Select select}
	 */
	public Select max(String property, String alias) {
		add(Projections.max(property), alias);
		return this;
	}

	/**
	 * Adds the MIN aggregation function, with the given property as argument, to
	 * the SELECT part of the SQL, with the given alias.
	 * 
	 * @param property
	 * @param alias
	 * @return {@link Select select}
	 */
	public Select min(String property, String alias) {
		add(Projections.min(property), alias);
		return this;
	}

	/**
	 * Adds the SUM aggregation function, with the given property as argument, to
	 * the SELECT part of the SQL, with the given alias.
	 * 
	 * @param property
	 * @param alias
	 * @return {@link Select select}
	 */
	public Select sum(String property, String alias) {
		add(Projections.sum(property), alias);
		return this;
	}

	/**
	 * Adds the AVG aggregation function, with the given property as argument, to
	 * the SELECT part of the SQL, with the given alias.
	 * 
	 * @param property
	 * @param alias
	 * @return {@link Select select}
	 */
	public Select avg(String property, String alias) {
		add(Projections.avg(property), alias);
		return this;
	}

	/**
	 * Adds the given SQL chunk to the SELECT part of the SQL, with the given
	 * aliases and types.
	 * 
	 * @param sql
	 * @param columnAliases
	 * @param types
	 * @return {@link Select select}
	 */
	public Select sql(String sql, String[] columnAliases, Type[] types) {
		add(Projections.sqlProjection(sql, columnAliases, types));
		return this;
	}

	/**
	 * Adds the given SQL chunk to the SELECT and the GROUP BY parts of the SQL,
	 * with the given aliases and types.
	 * 
	 * @param sql
	 * @param groupBy
	 * @param columnAliases
	 * @param types
	 * @return {@link Select select}
	 */
	public Select sqlGroup(String sql, String groupBy, String[] columnAliases, Type[] types) {
		add(Projections.sqlGroupProjection(sql, groupBy, columnAliases, types));
		return this;
	}

	/**
	 * Like in a "SELECT DISTINCT * FROM ...", the query will return a list of
	 * distinct instances of the root entity o the query.
	 * 
	 * @return {@link Select select}
	 */
	public Select rootEntity() {
		resultTransformer = Criteria.DISTINCT_ROOT_ENTITY;
		projections.clear();
		empty = true;
		single = false;
		distinct = false;
		return this;
	}

	/**
	 * Setup the query to return a list of instances of the given bean class. The
	 * query selected property aliases must match the given bean class property
	 * names.
	 * 
	 * @param beanClass
	 * @return {@link Select select}
	 */
	public Select bean(Class<?> beanClass) {
		resultTransformer = Transformers.aliasToBean(beanClass);
		return this;
	}

	private void add(Projection projection) {
		if (empty) {
			empty = false;
			single = true;
		} else if (single) {
			single = false;
		}
		projections.add(projection);
	}

	private void add(Projection projection, String alias) {
		if (empty) {
			empty = false;
			single = true;
		} else if (single) {
			single = false;
		}
		projections.add(Projections.alias(projection, alias));
	}

	protected ResultTransformer getResultTransformer() {
		return resultTransformer;
	}

	protected boolean hasProjection() {
		return !empty;
	}

	protected boolean hasSingleProjection() {
		return single;
	}

	protected Projection getProjection() {
		Projection projection;
		if (empty) {
			throw new RuntimeException("Query has no projection");
		} else if (single) {
			projection = projections.get(0);
		} else {
			ProjectionList pl = Projections.projectionList();
			for (Projection p : projections) {
				pl.add(p);
			}
			projection = pl;
		}
		if (distinct) {
			projection = Projections.distinct(projection);
		}
		return projection;
	}
}
