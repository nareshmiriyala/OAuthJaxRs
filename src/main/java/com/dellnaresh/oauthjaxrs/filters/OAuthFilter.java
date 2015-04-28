/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dellnaresh.oauthjaxrs.filters;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;

/**
 *
 * @author nareshm
 */
@PreMatching
@Provider
public class OAuthFilter implements ContainerRequestFilter {
    @Context
    private HttpServletRequest request;
    
      @Context
    private HttpServletResponse response;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        try {
            //        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
//        if (authHeader == null) {
//            throw new NotAuthorizedException("Bearer");
//        }
//        String token = parseToken(authHeader);
//        if (verifyToken(token) == false) {
//            throw new NotAuthorizedException("Bearer error=\"invalid_token\"");
//        }
            // Make the OAuth Request out of this request and validate it
            // Specify where you expect OAuth access token (request header, body or query string)
            OAuthAccessResourceRequest oauthRequest = new
                            OAuthAccessResourceRequest(request, ParameterStyle.HEADER);
            // Get the access token
            String accessToken = oauthRequest.getAccessToken();
 
             //... validate access token
            if (verifyToken(accessToken) == false) {
            throw new NotAuthorizedException("Bearer error=\"invalid_token\"");
        }
              
        } catch (OAuthSystemException ex) {
            Logger.getLogger(OAuthFilter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthProblemException ex) {
//            try {
                Logger.getLogger(OAuthFilter.class.getName()).log(Level.SEVERE, null, ex);
//                //build error response
//                OAuthResponse oauthResponse = OAuthRSResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
//                        .setRealm("Album Example").buildHeaderMessage();
//                
//                response.setHeader(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
                // Check if the error code has been set
            String errorCode = ex.getError();
            if(OAuthUtils.isEmpty(errorCode)){
                // If no error code then return a standard 401 Unauthorized response
                 throw new NotAuthorizedException("Bearer");
            }else{
                throw new NotAuthorizedException(ex.getDescription()+" .Error Code:"+errorCode);
            }
           
//            } catch (OAuthSystemException ex1) {
//                Logger.getLogger(OAuthFilter.class.getName()).log(Level.SEVERE, null, ex1);
//            }
        }
    }

    private String parseToken(String authHeader) {
        return "token_string";
    }

    private boolean verifyToken(String token) {
        return token!=null && token.equals("123455");
    }
}
