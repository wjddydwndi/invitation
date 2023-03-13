package com.invitation.module.api.filter;


import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.InputStream;

public class HttpRequestWrapper extends HttpServletRequestWrapper {
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */

    private byte[] byteArray;

    public HttpRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream is   = super.getInputStream();
        byteArray = IOUtils.toByteArray(is);
    }

    public byte[] getByteArray() { return this.byteArray; }

}
