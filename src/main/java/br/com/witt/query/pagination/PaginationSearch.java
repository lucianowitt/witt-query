package br.com.witt.query.pagination;

import org.hibernate.Session;

import br.com.witt.query.Query;

public class PaginationSearch {

	public static PaginationResult execute(Query query, PaginationParameters params, Session session) throws Exception {

		Long totalRows = query.rowCount(session);

		PaginationResult result = new PaginationResult(params.getPageNumber(), params.getPageSize(), totalRows);

		if (totalRows > 0L) {
			result.setPageRows(query.getPage(params.getPageNumber(), params.getPageSize(), session));
		}

		return result;
	}
}
