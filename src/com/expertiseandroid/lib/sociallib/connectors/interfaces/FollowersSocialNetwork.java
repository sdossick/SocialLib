
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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.xml.sax.SAXException;

import com.expertiseandroid.lib.sociallib.exceptions.NotAuthentifiedException;
import com.expertiseandroid.lib.sociallib.model.User;

/**
 * An interface for Twitter-like social networks (followers and friends are different)
 * @author ExpertiseAndroid
 *
 */
public interface FollowersSocialNetwork extends AuthorizedSocialNetwork{

  /**
   * Gets the list of followers
   * @return a list of users representing the followers
   * @throws ClientProtocolException
   * @throws IOException
   * @throws IllegalStateException
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws NotAuthentifiedException 
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   * @throws JSONException 
   */
  public List<? extends User> getFollowers() throws ClientProtocolException, IOException, IllegalStateException, SAXException, ParserConfigurationException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, JSONException;
  
  /**
   * Gets the list of users followed (friends) by the user
   * @return a list of users representing the followed users
   * @throws ClientProtocolException
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws NotAuthentifiedException 
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   * @throws JSONException 
   */
  public List<? extends User> getFollowed() throws ClientProtocolException, IOException, SAXException, ParserConfigurationException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, JSONException;
}