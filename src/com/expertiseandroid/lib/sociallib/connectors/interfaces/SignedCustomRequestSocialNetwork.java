/** 
 * Copyright (C) 2010  Expertise Android
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.expertiseandroid.lib.sociallib.connectors.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import com.expertiseandroid.lib.sociallib.exceptions.NotAuthentifiedException;
import com.expertiseandroid.lib.sociallib.messages.ReadableResponse;

/**
 * An interface for connectors that provides signed custom requests capability
 * @author ExpertiseAndroid
 *
 */
public interface SignedCustomRequestSocialNetwork extends CustomRequestSocialNetwork {
  
  /**
   * Sends a custom signed request
   * @param httpMethod the method that should be used to submit the request
   * @param request the URI of the request
   * @return a ReadableResponse that can be read through a Reader object
   * @throws FileNotFoundException
   * @throws MalformedURLException
   * @throws IOException
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   */
  public ReadableResponse signedCustomRequest(String httpMethod, String request) throws FileNotFoundException, MalformedURLException, IOException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException;
  
  /**
   * Sends a custom signed request
   * @param httpMethod the method that should be used to submit the request
   * @param request the URI of the request
   * @param bodyParams the body parameters of the request, the key should be the parameter name, and the value its value
   * @return a ReadableResponse that can be read through a Reader object
   * @throws FileNotFoundException
   * @throws MalformedURLException
   * @throws IOException
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   */
  public ReadableResponse signedCustomRequest(String httpMethod, String request, Map<String,String> bodyParams) throws FileNotFoundException, MalformedURLException, IOException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException;

}