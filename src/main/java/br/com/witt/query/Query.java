package br.com.witt.query;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.sql.JoinType;

/**
 * Main class of the query API. This API is intended to give a more fluent and
 * intuitive experience for the programmer, for it is more similar to the SQL
 * syntax.
 * 
 * @see Select
 * @see Join
 * @see Where
 * 
 * @author Luciano Witt
 *
 */
public class Query {

	private Select selection;
	private Class<?> fromClass;
	private String fromAlias;
	private List<Join> joins;
	private Where where;

	public Query() {
		selection = new Select(this);
		selection.rootEntity();
		fromClass = null;
		fromAlias = null;
		joins = new ArrayList<Join>();
		where = null;
	}

	/**
	 * Gives access to the select part of the SQL query.
	 * 
	 * @return The {@link Select select}
	 */
	public Select select() {
		return selection;
	}

	/**
	 * Setup the root entity (main table) of the query.
	 * 
	 * @param entityClass
	 * @param alias
	 * @return {@link Query}
	 */
	public Query from(Class<?> entityClass, String alias) {
		this.fromClass = entityClass;
		this.fromAlias = alias;
		return this;
	}

	/**
	 * Creates a inner join for the association path.
	 * 
	 * @param associationPath
	 * @param alias
	 * @return {@link Join}
	 */
	public Join join(String associationPath, String alias) {
		Join join = new Join(associationPath, alias, JoinType.INNER_JOIN);
		joins.add(join);
		return join;
	}

	/**
	 * Creates a left outer join for the association path.
	 * 
	 * @param associationPath
	 * @param alias
	 * @return {@link Join}
	 */
	public Join leftJoin(String associationPath, String alias) {
		Join join = new Join(associationPath, alias, JoinType.LEFT_OUTER_JOIN);
		joins.add(join);
		return join;
	}

	/**
	 * Creates a right outer join for the association path.
	 * 
	 * @param associationPath
	 * @param alias
	 * @return {@link Join}
	 */
	public Join rightJoin(String associationPath, String alias) {
		Join join = new Join(associationPath, alias, JoinType.RIGHT_OUTER_JOIN);
		joins.add(join);
		return join;
	}

	/**
	 * Creates a full outer join for the association path.
	 * 
	 * @param associationPath
	 * @param alias
	 * @return {@link Join}
	 */
	public Join fullJoin(String associationPath, String alias) {
		Join join = new Join(associationPath, alias, JoinType.FULL_JOIN);
		joins.add(join);
		return join;
	}

	/**
	 * Gives access to the where part of the SQL query.
	 * 
	 * @return {@link Where}
	 */
	public Where where() {
		if (where == null) {
			where = new Where(null);
		}
		return where;
	}

	protected DetachedCriteria getCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(fromClass, fromAlias);
		if (!joins.isEmpty()) {
			for (Join join : joins) {
				join.apply(criteria);
			}
		}
		if (where != null) {
			criteria.add(where.getCriterion());
		}
		if (selection.hasProjection()) {
			criteria.setProjection(selection.getProjection());
		}
		if (selection.getResultTransformer() != null) {
			criteria.setResultTransformer(selection.getResultTransformer());
		}
		return criteria;
	}

	/**
	 * Returns the executable criteria for this query on the given Hibernate
	 * session.
	 * 
	 * @param session the Hibernate session
	 * @return {@link Criteria}
	 */
	public Criteria getCriteria(Session session) {
		return getCriteria().getExecutableCriteria(session);
	}

	/**
	 * Executes, on the given Hibernate session, a row count for this query.
	 * 
	 * @param session the Hibernate session
	 * @return the number of rows returned by this query.
	 */
	public Long rowCount(Session session) {
		Criteria criteria = getCriteria(session);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	/**
	 * Executes the query on the given Hibernate session, returning a single row.
	 * 
	 * @param session the Hibernate session
	 * @return a unique result
	 */
	@SuppressWarnings("unchecked")
	public <T> T uniqueResult(Session session) {
		return (T) getCriteria(session).uniqueResult();
	}

	/**
	 * Executes the query on the given Hibernate session, returning a list of rows.
	 * 
	 * @param session a Hibernate session
	 * @return a result list
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> list(Session session) {
		return getCriteria(session).list();
	}

	/**
	 * Executes the query on the given Hibernate session, returning the list of rows
	 * of the given page number and page size.
	 * 
	 * @param number the page number
	 * @param size the page size
	 * @param session the Hibernate session
	 * @return the result list of the page
	 */
	public <T> List<T> getPage(int number, int size, Session session) {
		if (number <= 0) {
			number = 1;
		}
		if (size <= 0) {
			size = 10;
		}

		Criteria criteria = getCriteria(session);
		criteria.setFirstResult((number - 1) * size);
		criteria.setMaxResults(size);

		@SuppressWarnings("unchecked")
		List<T> result = criteria.list();

		return result;
	}
}
