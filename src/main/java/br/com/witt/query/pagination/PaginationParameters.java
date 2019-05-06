package br.com.witt.query.pagination;

public abstract class PaginationParameters {

	private Integer pageNumber;
	private Integer pageSize;

	protected PaginationParameters() {
		this(null, null);
	}

	protected PaginationParameters(Integer pageNumber, Integer pageSize) {
		this.pageNumber = (pageNumber == null ? 1 : pageNumber);
		this.pageSize = (pageSize == null ? 10 : pageSize);
		if (this.pageSize > 1000) {
			this.pageSize = 1000;
		}
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public boolean isValid() {
		return pageNumber > 0 && pageSize > 0;
	}

	public Integer getPageStart() {
		return ((pageNumber - 1) * pageSize) + 1;
	}

	public Integer getPageEnd() {
		return pageNumber * pageSize;
	}
}
