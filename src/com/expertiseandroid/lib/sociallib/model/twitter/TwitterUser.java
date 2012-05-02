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

package com.expertiseandroid.lib.sociallib.model.twitter;

import com.expertiseandroid.lib.sociallib.model.User;

/**
 * A user on Twitter
 * @author Expertise Android
 *
 */
public class TwitterUser extends User {
  
  public long id;
  public int nbFollowers, nbFriends, nbStatuses;
  public String name, screenName, location, description, imageUrl, url;
  public String status;
  
  
  
  public TwitterUser() {
    super();
  }

  @Override
  public String getUsername() {
    return screenName;
  }
  
  public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append(screenName).append('|').append(nbFollowers).append(" followers|").append(nbFriends).append(" friends\n");
    sb.append(status);
    return sb.toString();
  }

}