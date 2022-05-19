package kr.co.demo.filter;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

public class SiteMeshFilter extends ConfigurableSiteMeshFilter {
    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.addDecoratorPath("/*", "/WEB-INF/jsp/default.jsp")
                .addExcludedPath("/api/attach/*")
                .addExcludedPath("/error")
                .addExcludedPath("*.txt")
                .addExcludedPath("*.html")
                .addExcludedPath("*.jsp");
    }
}
