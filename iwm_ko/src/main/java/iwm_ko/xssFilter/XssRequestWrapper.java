package iwm_ko.xssFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class XssRequestWrapper extends HttpServletRequestWrapper {

	public XssRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
   @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return cleanXss(value);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) return null;
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXss(values[i]);
        }
        return encodedValues;
    }

    private String cleanXss(String value) {
        // 기본 XSS 필터링 로직
        if (value != null) {
            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
            value = value.replaceAll("'", "&#39;");
            value = value.replaceAll("eval\\((.*)\\)", "");
            value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
            value = value.replaceAll("script", "");
        }
        return value;
    }

}
