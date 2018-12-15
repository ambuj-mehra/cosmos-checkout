package com.cosmos.utils;

import com.cosmos.auth.bean.UserAuth;

import javax.servlet.http.HttpServletRequest;

/**
 * The type Controller utils.
 *
 * @author ambujmehra
 */
public class ControllerUtils {


    /**
     * Gets customer.
     *
     * @param request the request
     * @return the customer
     */
    public static UserAuth getCustomer(HttpServletRequest request) {

        return (UserAuth) request.getAttribute("user_auth");
    }
}
