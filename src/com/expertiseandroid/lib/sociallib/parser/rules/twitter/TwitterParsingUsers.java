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

package com.expertiseandroid.lib.sociallib.parser.rules.twitter;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import com.expertiseandroid.lib.sociallib.model.twitter.TwitterUser;
import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

/**
 * ParsingRules to fetch Twitter users data
 * @author Expertise Android
 *
 */
public class TwitterParsingUsers extends TwitterParsing implements ParsingRules{

  private List<TwitterUser> list;
  private TwitterUser currentUser;
  private boolean inStatus;

  public TwitterParsingUsers(){
    list = new ArrayList<TwitterUser>();
    inStatus = false;
  }
  
  public List<TwitterUser> getContents() {
    return list;
  }

  public void enter(String tag, Attributes attributes, int lvl) {
    if(tag.equals(USER)) addUser();
    else if(tag.equals(STATUS)) addStatus();
  }

  public void text(String tag, String text, int lvl) {
    if(tag.equals(ID)&&(!inStatus)) setUserId(text);
    else if(tag.equals(NAME)) setName(text);
    else if(tag.equals(SCREEN_NAME)) setScreenName(text);
    else if(tag.equals(LOCATION)) setLocation(text);
    else if(tag.equals(DESCRIPTION)) setDescription(text);
    else if(tag.equals(IMAGE_URL)) setImageUrl(text);
    else if(tag.equals(URL)) setUrl(text);
    else if(tag.equals(NB_FOLLOWERS)) setNbFollowers(text);
    else if(tag.equals(NB_FRIENDS)) setNbFriends(text);
    else if(tag.equals(NB_STATUSES)) setNbStatuses(text);
    else if(tag.equals(TEXT)) setText(text);
    
  }
  
  private void setImageUrl(String text) {
    currentUser.imageUrl = text;  
  }

  public void leave(String tag, int lvl) {
    if(tag.equals(STATUS)) endStatus();
  }
  
  private void addUser() {
    TwitterUser tu = new TwitterUser();
    list.add(tu);
    currentUser = tu;  
  }
  
  private void addStatus() {
    inStatus = true;
  }
  
  private void endStatus(){
    inStatus = false;
  }
  
  private void setUserId(String text2) {
    currentUser.id = new Long(text2);
  }
  
  private void setName(String text2) {
    currentUser.name = text2;
  }
  
  private void setScreenName(String text2) {
    currentUser.screenName = text2;
  }
  
  private void setLocation(String text2) {
    currentUser.location = text2;
  }
  
  private void setDescription(String text2) {
    currentUser.description = text2;  
  }
  
  private void setUrl(String text2) {
    currentUser.url = text2;
  }
  
  private void setNbFollowers(String text2) {
    currentUser.nbFollowers = new Integer(text2);
  }
  
  private void setNbFriends(String text2) {
    currentUser.nbFriends = new Integer(text2);
  }
  
  private void setNbStatuses(String text2) {
    currentUser.nbStatuses = new Integer(text2);
  }
  
  private void setText(String text2) {
    currentUser.status = text2;
  }
}