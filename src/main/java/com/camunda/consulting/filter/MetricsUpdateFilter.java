package com.camunda.consulting.filter;

import com.camunda.consulting.metrics.base.MetricsProvider;
import com.camunda.consulting.metrics.base.MetricsProviderRegistry;
import io.prometheus.client.spring.web.PrometheusTimeMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;


@Component
public class MetricsUpdateFilter implements Filter {

    @Autowired
    MetricsProviderRegistry metricsProviderRegistry;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    @PrometheusTimeMethod(name = "camunda_prometheus_metrics_update", help = "Requests for prometheus metrics count and duration in seconds")
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        List<MetricsProvider> metricsProviderList = metricsProviderRegistry.getMetricsProviders();

        if(metricsProviderList != null) {
            for (MetricsProvider metricsProvider : metricsProviderList) {
                metricsProvider.updateMetrics();
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
