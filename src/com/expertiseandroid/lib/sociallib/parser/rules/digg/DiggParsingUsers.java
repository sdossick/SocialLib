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

package com.expertiseandroid.lib.sociallib.parser.rules.digg;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import com.expertiseandroid.lib.sociallib.model.digg.DiggUser;
import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

/**
 * ParsingRules to fetch Digg Users data
 * @author Expertise Android
 *
 */
public class DiggParsingUsers extends DiggParsing implements ParsingRules{
  
  private List<DiggUser> content;
  private DiggUser currentUser;
  
  public DiggParsingUsers(){
    content = new ArrayList<DiggUser>();
  }
  
  public void enter(String tag, Attributes attributes, int lvl) {
    if(tag.equals(USER)) addUser(attributes);
  }
  
  public void leave(String tag, int lvl) {}  
  
  public void text(String tag, String text, int lvl) {
    if(tag.equals(USERNAME)) setUsername(text);
    else if(tag.equals(USER_ID)) setUserId(text);
    else if(tag.equals(NAME)) setName(text);
    else if(tag.equals(ICON)) setIcon(text);
    else if(tag.equals(FOLLOWERS)) setFollowers(text);
    else if(tag.equals(FOLLOWING)) setFollowing(text);
  }

  private void setFollowing(String text) {
    currentUser.following = new Integer(text);
  }

  private void setFollowers(String text) {
    currentUser.followers = new Integer(text);
  }

  private void setIcon(String text) {
    currentUser.icon = text;
  }

  private void setName(String text) {
    currentUser.name = text;
  }

  private void setUserId(String text) {
    currentUser.id = text;
  }

  private void setUsername(String text) {
    currentUser.username = text;
  }

  public List<DiggUser> getContents() {
    return content;
  }
  
  private void addUser(Attributes attr){
    DiggUser du = new DiggUser();
    content.add(du);
    currentUser = du;
  }
  
  

}