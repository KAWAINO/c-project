package radius.comm;

public class paging {
	public String getPagingStr(int pageno, int fetchrow, int totalcount, String param1 ) {
		int blockSize = 5; // 페이징이 몇개까지 할 건지
		int page = pageno; // 현재 페이지
		int listSize = fetchrow; // 한페이지당 리스트 건수
		int totalCnt = totalcount; // 전체 갯수
		int totalPage = 1; // 전체페이지 수
		int blockCnt = 1; // 블럭 갯수
		int block = 1; // 현재 블럭
		int blockStartPage = 1; // 현재 블럭에서 시작 페이지
		int blockEndPage = 1; // 마지막 페이지
		int tmpPage = 1; // 임시페이지

		// 현재 페이지
		if (page == 0) {
			page = 1;
		}

		// 전체 페이지수
		if (totalCnt % listSize == 0) {
			totalPage = totalCnt / listSize;
		}
		else {
			totalPage = totalCnt / listSize + 1;
		}

		// 블럭 갯수
		if (totalPage % blockSize == 0) {
			blockCnt = totalPage / blockSize;
		}
		else {
			blockCnt = totalPage / blockSize + 1;
		}

		// 현재 블럭
		if (page % blockSize == 0) {
			block = page / blockSize;
		}
		else {
			block = page / blockSize + 1;
		}

		// 현재 블럭의 시작 페이지번호, 끝 페이지 번호
		blockStartPage = (block - 1) * blockSize + 1;
		blockEndPage = block * blockSize;
		if (blockEndPage > totalPage) blockEndPage = totalPage;
		
		// 페이지 블럭이동
		String prevJump = "<li class=\"page-item disabled\"><a class=\"page-link\" href=\"#\">&laquo;</a></li>";
		if (page - 1 > 0) {	// 현재 블럭에서 이전 블럭으로 이동 ( 이전 10개 )
			tmpPage = (block - 1) * blockSize -4;

			if(param1.equals("") ) {
				prevJump = "<li class=\"page-item\"><a class=\"page-link\" href=\"javascript:goPage(" + tmpPage + ")\">&laquo;</a></li>";
			} else {
				prevJump = "<li class=\"page-item\"><a href=\"javascript:goPage(" + tmpPage + ",'"+param1+"')\" class=\"page-link\">&laquo;</a></li>";
			}
		}

		// 페이지 네비게이션
		String pageCenter = "";
		for (int i = blockStartPage; i <= blockEndPage; i++) {
			if (page == i) {
				pageCenter = pageCenter + "<li class=\"page-item active\"><a class=\"page-link\" href=\"javascript:goPage(" + i + ")\">" + i + "</a></li>";
			}
			else {
				if(param1.equals("") ) {
					pageCenter = pageCenter + "<li class=\"page-item\"><a class=\"page-link\" href=\"javascript:goPage(" + i + ")\">" + i + "</a></li>";
				} else {
					pageCenter = pageCenter + "<li class=\"page-item\"><a class=\"page-link\" href=\"javascript:goPage(" + i + ",'"+param1+"')\">" + i + "</a></li>";
				}
			}
		}

		// 다음 블럭이동
		String nextJump = "<li class=\"page-item disabled\"><a class=\"page-link\" href=\"#\">&raquo;</a></li>";
		if (page < totalPage) {	// 현재 블럭에서 다음 블럭으로 이동. (다음 10개)
		
			tmpPage = block * blockSize + 1;	
			
			if(param1.equals("") ) {
				nextJump = "<li class=\"page-item\"><a class=\"page-link\" href=\"javascript:goPage(" + tmpPage + ")\">&raquo;</a></li>";
			} else {
				nextJump = "<li class=\"page-item\"><a class=\"page-link\" href=\"javascript:goPage(" + tmpPage + ",'"+param1+"')\">&raquo;</a></li>";
			}
		}
	     
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<div>");
		strBuf.append("<ul class=\"pagination justify-content-center\">");
		strBuf.append(prevJump);
		strBuf.append(pageCenter);
		strBuf.append(nextJump);
		strBuf.append("</ul>");
		strBuf.append("</div>");
		String retStr = "";
		if (!pageCenter.equals("")) {
			retStr = strBuf.toString();
		}
		return retStr;	
	}

	public String getPagingStr(int pageno, int fetchrow, int totalcount ) {
		return getPagingStr( pageno,  fetchrow,  totalcount, "" );
	}	
}
