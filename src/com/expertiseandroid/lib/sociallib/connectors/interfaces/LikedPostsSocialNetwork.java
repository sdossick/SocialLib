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

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.json.JSONException;

import com.expertiseandroid.lib.sociallib.exceptions.NotAuthentifiedException;
import com.expertiseandroid.lib.sociallib.model.Post;

/**
 * An interface for social networkds that give the ability to like content.
 * @author ExpertiseAndroid
 *
 */
public interface LikedPostsSocialNetwork extends PostsSocialNetwork {

  /**
   * Likes a post
   * @param post
   * @return true if the operation succeded
   * @throws FileNotFoundException
   * @throws MalformedURLException
   * @throws IOException
   * @throws NotAuthentifiedException 
   * @throws JSONException 
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   */
  public boolean like(Post post) throws FileNotFoundException, MalformedURLException, IOException, NotAuthentifiedException, JSONException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException;
}