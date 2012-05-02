
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

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.scribe.oauth.Token;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;

import com.expertiseandroid.lib.sociallib.exceptions.NotAuthentifiedException;
import com.expertiseandroid.lib.sociallib.model.User;

/**
 * An interface for authorized social networks
 * @author Expertise Android
 *
 */
public interface AuthorizedSocialNetwork extends SocialNetwork {
  
  /**
   * Redirects the user to a page so he can give the application the rights to access his data
   * @param ctx the context from where the page will be called
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthNotAuthorizedException 
   * @throws OAuthMessageSignerException 
   */
  public void requestAuthorization(Context ctx) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException;
  
  /**
   * Auhtorizes the application to access the user's data
   * @param ctx the activity that is called right after the user has allowed the application to access his data
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthNotAuthorizedException 
   * @throws OAuthMessageSignerException 
   */
  public void authorize(Activity ctx) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException;
  
  /**
   * Logs out the application from the social network
   * @param ctx the context from where the logout request will be called
   * @return true the logout succeded
   * @throws MalformedURLException
   * @throws IOException
   * @throws JSONException 
   * @throws ParserConfigurationException 
   * @throws SAXException 
   * @throws NotAuthentifiedException 
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   */
  public boolean logout(Context ctx) throws MalformedURLException, IOException, JSONException, SAXException, ParserConfigurationException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException;
  
  /**
   * 
   * @return true if the application is authentified to the social network
   */
  public boolean isAuthentified();
  
  /**
   * Current user's accessor
   * @return an object-representation of the authentified user
   * @throws ClientProtocolException
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws JSONException
   * @throws NotAuthentifiedException 
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   */
  public User getUser() throws ClientProtocolException, IOException, SAXException, ParserConfigurationException, JSONException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException;
  
  /**
   * Can authentify an application from an access token
   * This method should be used if you store the access token rather than logging out, and then use the token again
   * You should verify that the access token has not expired
   * You should verify that the authentification succeded
   * @param accessToken
   * @return true
   */
  public boolean authentify(Token accessToken);
  
  /**
   * 
   * @return the access token used or null if the application is not authorized
   */
  public Token getAccessToken();

}