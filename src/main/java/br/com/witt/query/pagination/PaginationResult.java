package br.com.witt.query.pagination;

import java.util.ArrayList;
import java.util.List;

public class PaginationResult {

	private Integer pageNumber;
	private Integer pageSize;
	private Long rowCount;
	private List<Object> pageRows;

	public PaginationResult(Integer pageNumber, Integer pageSize, Long rowCount) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.rowCount = rowCount;
		this.pageRows = new ArrayList<Object>();
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public List<Object> getPageRows() {
		return pageRows;
	}

	public void setPageRows(List<Object> pageRows) {
		this.pageRows = pageRows;
	}
}
