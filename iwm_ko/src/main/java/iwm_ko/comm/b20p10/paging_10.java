package iwm_ko.comm.b20p10;

public class paging_10 {
	public String getPagingStr(int pageno, int fetchrow, int totalcount, String param1 ) {
		int blockSize = 10; // 페이징이 몇개까지 할 건지
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
		if (page == 0 ) {
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
		if (page % blockSize == 0 || page % blockSize == -4) {
			block = page / blockSize;
		}
		else {
			block = page / blockSize + 1;
		}
		
		// 현재 블럭의 시작 페이지번호, 끝 페이지 번호
		blockStartPage = (block - 1) * blockSize + 1;
		blockEndPage = block * blockSize;
		if (blockEndPage > totalPage) blockEndPage = totalPage;
		
		// 이전 블럭의 첫 페이지로		
		String first = "<li class=\"page-item disabled\"><a class=\"icon first\" href=\"#\">&laquo;</a></li>";

		if (block > 1) {
		    int firstPageInPrevBlock = (block - 2) * blockSize + 1;
		    
		    if( firstPageInPrevBlock < 1) {
		    	firstPageInPrevBlock = 1;
			}

		    if(param1.equals("")) {
		        first = "<li class=\"page-item\"><a class=\"icon first\" href=\"javascript:goPage(" + firstPageInPrevBlock + ")\">&laquo;</a></li>";
		    } else {
		        first = "<li class=\"page-item\"><a class=\"icon first\" href=\"javascript:goPage(" + firstPageInPrevBlock + ",'" + param1 + "')\">&laquo;</a></li>";
		    }
		}
			
		// 페이지 블럭이동
		String prevPage = "<li class=\"page-item disabled\"><a class=\"icon prev\" href=\"#\">&laquo;</a></li>";

		if (page > 1) {
		    int prevPageNum = page - 1;
		    
		    if (param1.equals("")) {
		        prevPage = "<li class=\"page-item\"><a class=\"icon prev\" href=\"javascript:goPage(" + prevPageNum + ")\">&lt;</a></li>";
		    } else {
		        prevPage = "<li class=\"page-item\"><a class=\"icon prev\" href=\"javascript:goPage(" + prevPageNum + ",'" + param1 + "')\">&lt;</a></li>";
		    }
		} else {
		    // 현재 페이지가 첫 번째 페이지인 경우, 링크를 비활성화합니다.
		    prevPage = "<li class=\"page-item disabled\"><a class=\"icon prev\" href=\"#\">&lt;</a></li>";
		}

		// 페이지 네비게이션
		String pageCenter = "";
		for (int i = blockStartPage; i <= blockEndPage; i++) {
			if (page == i) {
				pageCenter = pageCenter + "<li class=\"num\"><a class=\"on\" href=\"javascript:goPage(" + i + ")\">" + i + "</a></li>";
			}
			else {
				if(param1.equals("") ) {
					pageCenter = pageCenter + "<li class=\"page-item\"><a class=\"on\" href=\"javascript:goPage(" + i + ")\">" + i + "</a></li>";
				} else {
					pageCenter = pageCenter + "<li class=\"page-item\"><a class=\"on\" href=\"javascript:goPage(" + i + ",'"+param1+"')\">" + i + "</a></li>";
				}
			}
		}

		// 다음 블럭이동
		String nextPage = "<li class=\"page-item disabled\"><a class=\"icon next\" href=\"#\">&raquo;</a></li>";
		
		if (page < totalPage) {
		    int nextPageNum = page + 1;

		    if (param1.equals("")) {
		        nextPage = "<li class=\"page-item\"><a class=\"icon next\" href=\"javascript:goPage(" + nextPageNum + ")\">&gt;</a></li>";
		    } else {
		        nextPage = "<li class=\"page-item\"><a class=\"icon next\" href=\"javascript:goPage(" + nextPageNum + ",'" + param1 + "')\">&gt;</a></li>";
		    }
		} else {
		    // 현재 페이지가 마지막 페이지인 경우, 링크를 비활성화합니다.
		    nextPage = "<li class=\"page-item disabled\"><a class=\"icon next\" href=\"#\">&gt;</a></li>";
		}
		
		
		String last = "<li class=\"icon last\"><a class=\"icon last\" href=\"#\">&laquo;</a></li>";
		
		if (page < totalPage) {	// 현재 블럭에서 다음 블럭으로 이동. (다음 10개)
			
			tmpPage = block * blockSize + 1;	
			
			if(tmpPage > totalPage) {
				tmpPage = totalPage;
			}
			
			
			if(param1.equals("") ) {
				last = "<li class=\"page-item\"><a class=\"icon last\" href=\"javascript:goPage(" + tmpPage + ")\">&raquo;</a></li>";
			} else {
				last = "<li class=\"page-item\"><a class=\"icon last\" href=\"javascript:goPage(" + tmpPage + ",'"+param1+"')\">&raquo;</a></li>";
			}
		}
	     
		StringBuffer strBuf = new StringBuffer();
		
		strBuf.append("<ul class=\"paging\">");
		strBuf.append(first);
		strBuf.append(prevPage);
		strBuf.append(pageCenter);
		strBuf.append(nextPage);
		strBuf.append(last);
		strBuf.append("</ul>");
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
