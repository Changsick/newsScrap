package com.gridone.scraping.model;

import java.util.List;

public class ResultList {

	public ResultList() {
		super();		
		paging = new PagingInfo();
	}
	
	public ResultList(SearchBase search ) {		
		super();
		paging = new PagingInfo();
		paging.setSearchBase(search);
	}

	List<?> nextTimeList;	
	List<?> resultList;	
	PagingInfo paging ;

	public List<?> getNextTimeList() {
		return nextTimeList;
	}

	public void setNextTimeList(List<?> nextTimeList) {
		this.nextTimeList = nextTimeList;
	}

	public List<?> getResultList() {
		return resultList;
	}

	public void setResultList(List<?> resultList) {
		this.resultList = resultList;
	}

	public PagingInfo getPaging() {
		return paging;
	}

	public void setPaging(PagingInfo paging) {
		this.paging = paging;
	}

	public void setTotalRecordCount(Integer totalRecordCount) {
		this.paging.setTotalRecordCount(totalRecordCount);		
	}


}
