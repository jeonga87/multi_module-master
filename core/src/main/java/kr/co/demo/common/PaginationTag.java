package kr.co.demo.common;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class PaginationTag extends TagSupport {
	private Pagination pagination;
	private String jsFunction;
	private PaginationRenderer paginationRenderer;

	@Override
	public int doStartTag() throws JspException {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
		paginationRenderer = ctx.getBean(PaginationRenderer.class);

		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		try {

			JspWriter out = pageContext.getOut();

			String contents = paginationRenderer.renderPagination(pagination, jsFunction);

			out.println(contents);

			return EVAL_PAGE;

		} catch (IOException e) {
			throw new JspException();
		}
	}

	public void setJsFunction(String jsFunction) {
		this.jsFunction = jsFunction;
	}

	public void setPaginationInfo(Pagination pagination) {
		this.pagination = pagination;
	}
}
