package iwm_ko.comm.b20p10;

public class BaseVo_20 {
	
	//paging
	private int	startrow=1;			//start row index
	private int	fetchrow=20;		//row range
	private int pageno=1;			//page index
	private int totalcount=0;		//total row
	
	
	public int getStartrow() {
		return startrow;
	}
	public void setStartrow(int startrow) {
		this.startrow = startrow;
	}
	public int getFetchrow() {
		return fetchrow;
	}
	public void setFetchrow(int fetchrow) {
		this.fetchrow = fetchrow;
	}
	public int getPageno() {
		return pageno;
	}
	public void setPageno(int pageno) {
		this.pageno = pageno;
	}
	public int getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}

}
