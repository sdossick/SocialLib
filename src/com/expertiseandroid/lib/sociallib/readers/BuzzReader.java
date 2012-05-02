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
import com.expertiseandroid.lib.sociallib.model.buzz.BuzzActivity;
import com.expertiseandroid.lib.sociallib.model.buzz.BuzzUser;
import com.expertiseandroid.lib.sociallib.parser.rules.buzz.BuzzParsingActivities;
import com.expertiseandroid.lib.sociallib.parser.rules.buzz.BuzzParsingResponse;
import com.expertiseandroid.lib.sociallib.parser.rules.buzz.BuzzParsingSingleUser;
import com.expertiseandroid.lib.sociallib.parser.rules.buzz.BuzzParsingUsers;
import com.expertiseandroid.lib.sociallib.utils.Utils;

/**
 * An object that is in charge of parsing Google Buzz responses
 * @author Expertise Android
 *
 */
public class BuzzReader{
  
  /**
   * Extracts a list of buzz activities
   * @param response
   * @return
   * @throws JSONException
   */
  @SuppressWarnings("unchecked")
  public List<BuzzActivity> readActivities(ReadableResponse response) throws JSONException{
    return (List<BuzzActivity>) Utils.parseJSON(response, new BuzzParsingActivities());
  }
  
  /**
   * Extracts a list of buzz users
   * @param response
   * @return
   * @throws JSONException
   */
  @SuppressWarnings("unchecked")
  public List<BuzzUser> readUsers(ReadableResponse response) throws JSONException{
    return (List<BuzzUser>) Utils.parseJSON(response, new BuzzParsingUsers());
  }
  
  /**
   * Extracts one buzz user
   * @param response
   * @return
   * @throws JSONException
   */
  public BuzzUser readUser(ReadableResponse response) throws JSONException{
    return (BuzzUser) Utils.parseJSON(response, new BuzzParsingSingleUser());
    
  }
  
  /**
   * Determines whether an action was successful or not
   * @param response
   * @return
   * @throws JSONException
   */
  public boolean readResponse(ReadableResponse response) throws JSONException{
    return (Boolean) Utils.parseJSON(response, new BuzzParsingResponse());
  }

  

  

}