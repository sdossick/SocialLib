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

package com.expertiseandroid.lib.sociallib.readers;

import java.util.List;

import org.json.JSONException;

import com.expertiseandroid.lib.sociallib.messages.ReadableResponse;
import com.expertiseandroid.lib.sociallib.model.facebook.FacebookPost;
import com.expertiseandroid.lib.sociallib.model.facebook.FacebookUser;
import com.expertiseandroid.lib.sociallib.parser.rules.facebook.FacebookParsingPosts;
import com.expertiseandroid.lib.sociallib.parser.rules.facebook.FacebookParsingResponse;
import com.expertiseandroid.lib.sociallib.parser.rules.facebook.FacebookParsingSingleUser;
import com.expertiseandroid.lib.sociallib.parser.rules.facebook.FacebookParsingUsers;
import com.expertiseandroid.lib.sociallib.utils.Utils;
/**
 * An object that is in charge of parsing Facebook responses.
 * @author Expertise Android
 *
 */
public class FacebookReader{
  
  /**
   * Extracts a list of FacebookUsers
   * @param response a JSON-formatted response
   * @return
   * @throws JSONException
   */
  @SuppressWarnings("unchecked")
  public List<FacebookUser> readUsers(ReadableResponse response) throws JSONException{
    return (List<FacebookUser>) Utils.parseJSON(response, new FacebookParsingUsers());
  }
  
  /**
   * Extracts a single FacebookUser
   * @param response a JSON-formatted response
   * @return
   * @throws JSONException
   */
  @SuppressWarnings("unchecked")
  public FacebookUser readUser(ReadableResponse response) throws JSONException{
    List<FacebookUser> list = (List<FacebookUser>) Utils.parseJSON(response, new FacebookParsingSingleUser());
    if(list.size()==1) return list.get(0);
    else return null;
  }
  
  /**
   * Extracts a list of FacebookPosts
   * @param response a JSON-formatted response
   * @return
   * @throws JSONException
   */
  @SuppressWarnings("unchecked")
  public List<FacebookPost> readPosts(final ReadableResponse response) throws JSONException{
    return (List<FacebookPost>) Utils.parseJSON(response, new FacebookParsingPosts());
  }
  
  /**
   * Determines whether an action was successful or not
   * @param response a JSON-formatted response
   * @return true if the response does not contain any error or message
   * @throws JSONException
   */
  public boolean readResponse(ReadableResponse response) throws JSONException{
    return (Boolean) Utils.parseJSON(response, new FacebookParsingResponse());
    
  }
  
  



}