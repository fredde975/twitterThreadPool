package com.example.twitter.filters;


import org.apache.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
@Order(1)
public class ConcurrentRequestsFilter implements Filter {
    private static final org.apache.log4j.Logger LOG = Logger.getLogger(ConcurrentRequestsFilter.class);
    private static int concurrentRequests = 0;


    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        LOG.info("init filter");
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        concurrentRequests++;
        System.out.println("Concurrent Requests: " + concurrentRequests);

        if(concurrentRequests > 1){
            concurrentRequests--;
            throw new ServletException("The number of concurrent requests is already at 3");
        }

        chain.doFilter(request, response);

        concurrentRequests--;
        System.out.println("Concurrent Requests: " + concurrentRequests);

    }

    @Override
    public void destroy() {
        LOG.warn("Destroy filter");
    }
}