package top.tangtian;

import javax.servlet.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author tangtian
 * @description
 * @date 2021/7/23 8:36
 */
public class ConuntingFactorizer implements Servlet {

    private final AtomicLong count = new AtomicLong(0);

    public long getCount(){
        return count.get();
    }
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
//        BigInteger i = extractFromRequest(servletRequest);
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
