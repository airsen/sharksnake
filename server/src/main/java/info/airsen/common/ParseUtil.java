package info.airsen.common;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>一些转换工具</p>
 *
 * @author airsen
 * @since 13-1-25 下午7:36
 */
public class ParseUtil {

	public static String getParameter(String key) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (StringUtils.isNotBlank(request.getParameter(key)))
			return request.getParameter(key);
		return null;
	}

	public static String getParameter(String key, String defaultString) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (StringUtils.isNotBlank(request.getParameter(key)))
			return request.getParameter(key);
		else
			return defaultString;
	}

	public static Integer getParameter(String key, Integer defaultInteger) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (StringUtils.isNotBlank(request.getParameter(key)))
			return Integer.valueOf(request.getParameter(key));
		else
			return defaultInteger;
	}

	public static String[] getParameterValues(String key) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String[] result = request.getParameterValues(key);
		if (null != result && result.length > 0)
			return result;
		else
			return new String[1];

	}

}
