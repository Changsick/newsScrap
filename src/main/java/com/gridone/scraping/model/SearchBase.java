package com.gridone.scraping.model;

import java.io.Serializable;

//import org.apache.commons.text.StringEscapeUtils;




public class SearchBase implements Serializable  {

	private static final long serialVersionUID = -4293818674148167940L;

	protected String searchVal;
	
	/** 현재페이지 */
	protected int pageNo = 1;
	
	/** 페이지당 레코드 갯수 */
	protected int recordCountPerPage = 10;
	
	/** 페이지사이즈 */
	protected int pageSize = 10;
	
	private Integer keyword_id;
	
	private Integer userId;

	public Integer getKeyword_id() {
		return keyword_id;
	}

	public void setKeyword_id(Integer keyword_id) {
		this.keyword_id = keyword_id;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setSearchVal(String searchVal) {
//		this.searchVal = replaceChar(searchVal);
		this.searchVal = searchVal;
	}	
	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageIndex) {
		this.pageNo = pageIndex;
	}

	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}

	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}	
	
	
	public int getFirstIndex() {
		int firstIndex = (pageNo - 1) * recordCountPerPage;
		return firstIndex;
	}

	@Override
	public String toString() {
		return "SearchBase [searchVal=" + searchVal + ", pageNo=" + pageNo + ", recordCountPerPage="
				+ recordCountPerPage + ", pageSize=" + pageSize + "]";
	}
	
	/*public String replaceChar(String target) {
		if(target != null) {
			target = target.replaceAll("&amp;amp;", "");
			target = StringEscapeUtils.unescapeHtml4(target);
			
			target = target.replaceAll("\\%", "\\\\%");

			//target = target.replaceAll("[\\{\\}\\[\\]?;:|\\)*~`!^\\-+<>@\\#$%&\\=\\(\\'\\\"]", "");
		}
		return target;
	}*/
	
	
}