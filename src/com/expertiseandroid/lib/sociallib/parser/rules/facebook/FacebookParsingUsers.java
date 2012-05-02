/** Copyright (C) 2010  Expertise Android

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */

package com.expertiseandroid.lib.sociallib.parser.rules.facebook;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import com.expertiseandroid.lib.sociallib.model.facebook.FacebookUser;
import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

/**
 * Parsing rules to parse Facebook users
 * @author Expertise Android
 *
 */
public class FacebookParsingUsers extends FacebookParsing implements ParsingRules{

  private List<FacebookUser> content;
  private FacebookUser currentUser;
  
  public FacebookParsingUsers(){
    content = new ArrayList<FacebookUser>();
  }
  
  public List<?> getContents() {
    return content;
  }
  
  public void leave(String tag, int lvl) {}

  public void text(String tag, String text, int lvl) {
    if(tag.equals(ID)) setId(text);
    if(tag.equals(NAME)) setName(text);  
  }

  public void enter(String tag, Attributes attributes, int lvl) {
    if(tag.equals(DATA)) addUser();
  }
  
  private void addUser() {
    FacebookUser fu = new FacebookUser();
    content.add(fu);
    currentUser = fu;
  }

  private void setName(String text) {
    currentUser.name = text;
  }

  private void setId(String text) {
    currentUser.id = new Long(text);
  }
}