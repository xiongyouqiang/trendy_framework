package com.trendy.fw.common.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.util.StringKit;
import com.trendy.fw.common.web.HttpResponseKit;

public class ResponseNoCacheFilter implements Filter {
	private static Logger log = LoggerFactory.getLogger(ResponseNoCacheFilter.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		HttpResponseKit.setNoCache(httpServletResponse);
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
