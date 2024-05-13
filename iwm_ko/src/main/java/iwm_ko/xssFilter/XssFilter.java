package iwm_ko.xssFilter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class XssFilter implements Filter {
	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 로직 (필요한 경우)
    }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		 chain.doFilter(new XssRequestWrapper((HttpServletRequest) request), response);		
	}
	
    @Override
    public void destroy() {
        // 필터 파괴 로직 (필요한 경우)
    }

}
