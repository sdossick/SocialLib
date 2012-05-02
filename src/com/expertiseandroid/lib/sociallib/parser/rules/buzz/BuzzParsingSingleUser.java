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

package com.expertiseandroid.lib.sociallib.parser.rules.buzz;

import java.util.ArrayList;
import java.util.List;


import org.xml.sax.Attributes;

import com.expertiseandroid.lib.sociallib.model.buzz.BuzzUser;
import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

/**
 * Parsing rules to fetch a Google Buzz user
 * @author Expertise Android
 *
 */
public class BuzzParsingSingleUser extends BuzzParsing implements ParsingRules {

  private BuzzUser user;
  
  public BuzzParsingSingleUser(){
    user = new BuzzUser();
  }
  
  public List<?> getContents() {
    List<BuzzUser> list = new ArrayList<BuzzUser>();
    list.add(user);
    return list;
  }
  
  public void enter(String tag, Attributes attributes, int lvl) {}

  public void leave(String tag, int lvl) {}

  public void text(String tag, String text, int lvl) {
    if(tag.equals(ID)) setId(text);
    else if(tag.equals(DISPLAYNAME)) setUserName(text);
  }

  private void setId(String text){
    user.id = text;
  }
  
  private void setUserName(String text){
    user.username = text;
  }
}
