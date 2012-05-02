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

package com.expertiseandroid.lib.sociallib.parser.rules.facebook;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import com.expertiseandroid.lib.sociallib.model.facebook.FacebookUser;

/**
 * ParsingRules to fetch a single Facebook user data
 * @author Expertise Android
 *
 */
public class FacebookParsingSingleUser extends FacebookParsingUsers {
  
  private FacebookUser user;
  
  public FacebookParsingSingleUser(){
    user = new FacebookUser();
  }
  
  @Override
  public List<?> getContents() {
    List<FacebookUser> l = new ArrayList<FacebookUser>();
    l.add(user);
    return l;
  }
  
  @Override
  public void enter(String tag, Attributes attributes, int lvl) {}
  
  @Override
  public void leave(String tag, int lvl) {}
  
  @Override
  public void text(String tag, String text, int lvl) {
    if(tag.equals(ID)) setId(text);
    if(tag.equals(NAME)) setName(text);
  }
  
  private void setId(String text){
    user.id = new Long(text);
  }
  
  private void setName(String text){
    user.name = text;
  }
}