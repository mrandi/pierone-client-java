/**
 * Copyright (C) 2015 Zalando SE (http://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.stups.fullstop.clients.pierone.spring;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserApprovalRequiredException;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

/**
 * @author  jbellmann
 */
public class TestAccessTokenProvider implements AccessTokenProvider {

    private final String accessToken;

    public TestAccessTokenProvider(final String tokenValue) {
        this.accessToken = tokenValue;
    }

    @Override
    public OAuth2AccessToken obtainAccessToken(final OAuth2ProtectedResourceDetails details,
            final AccessTokenRequest parameters) throws UserRedirectRequiredException, UserApprovalRequiredException,
        AccessDeniedException {
        return new UpperCaseHeaderToken(accessToken);
    }

    @Override
    public boolean supportsResource(final OAuth2ProtectedResourceDetails resource) {
        return true;
    }

    @Override
    public OAuth2AccessToken refreshAccessToken(final OAuth2ProtectedResourceDetails resource,
            final OAuth2RefreshToken refreshToken, final AccessTokenRequest request)
        throws UserRedirectRequiredException {
        throw new UnsupportedOperationException("Not Supported 'refreshAccessToken'");
    }

    @Override
    public boolean supportsRefresh(final OAuth2ProtectedResourceDetails resource) {
        return false;
    }

    /**
     * Because {@link #getTokenType()} is used in the header it should return 'Bearer' instead of 'bearer'.
     *
     * @author  jbellmann
     */
    static class UpperCaseHeaderToken extends DefaultOAuth2AccessToken {

        private static final long serialVersionUID = 1L;

        UpperCaseHeaderToken(final OAuth2AccessToken accessToken) {
            super(accessToken);
        }

        public UpperCaseHeaderToken(final String token) {
            super(token);
        }

        /**
         * because this will be used in the header it should start with 'B' instead of 'b'.
         */
        @Override
        public String getTokenType() {
            return OAuth2AccessToken.BEARER_TYPE;
        }

    }

}
