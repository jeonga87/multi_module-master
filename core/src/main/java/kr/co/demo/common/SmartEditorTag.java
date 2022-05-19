package kr.co.demo.common;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class SmartEditorTag extends TagSupport {
	private String id;
	private String name;
	private String content;
	private SmartEditorRenderer smartEditorRenderer;

	@Override
	public int doStartTag() throws JspException {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
		smartEditorRenderer = ctx.getBean(SmartEditorRenderer.class);

		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			String contents = smartEditorRenderer.renderSmartEditor(id, name, content);
			out.println(contents);
			return EVAL_PAGE;
		} catch (IOException e) {
			throw new JspException();
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
