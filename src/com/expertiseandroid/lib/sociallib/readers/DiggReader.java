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

import android.util.Log;

import com.expertiseandroid.lib.sociallib.messages.ReadableResponse;
import com.expertiseandroid.lib.sociallib.model.digg.DiggPost;
import com.expertiseandroid.lib.sociallib.model.digg.DiggUser;
import com.expertiseandroid.lib.sociallib.parser.rules.digg.DiggParsingPosts;
import com.expertiseandroid.lib.sociallib.parser.rules.digg.DiggParsingResponse;
import com.expertiseandroid.lib.sociallib.parser.rules.digg.DiggParsingUsers;
import com.expertiseandroid.lib.sociallib.utils.Utils;

/**
 * An object that is in charge of parsing Digg responses
 * @author Expertise Android
 *
 */
public class DiggReader{


  /**
   * Extracts a list of DiggUsers
   * @param response
   * @return
   * @throws JSONException
   */
  @SuppressWarnings("unchecked")
  public List<DiggUser> readUsers(ReadableResponse response) throws JSONException{  
    return (List<DiggUser>) Utils.parseJSON(response, new DiggParsingUsers());
  }
  /**
   * Extracts a single DiggUser
   * @param response
   * @return
   * @throws JSONException
   */
  public DiggUser readSingleUser(ReadableResponse response) throws JSONException{
    List<DiggUser> list = readUsers(response);
    if(list.size()==1) return list.get(0);
    else return null;
  }
  
  /**
   * Extracts a list of Digg posts
   * @param response
   * @return
   * @throws JSONException
   */
  @SuppressWarnings("unchecked")
  public List<DiggPost> readPosts(ReadableResponse response) throws JSONException{
    return (List<DiggPost>) Utils.parseJSON(response, new DiggParsingPosts());
  }
  
  /**
   * Determines wheter an action was successful or not
   * @param response
   * @return
   * @throws JSONException
   */
  public boolean readResponse(ReadableResponse response) throws JSONException{
    Log.d("DiggReader", response.getContents());
    return (Boolean) Utils.parseJSON(response, new DiggParsingResponse());
  }

}