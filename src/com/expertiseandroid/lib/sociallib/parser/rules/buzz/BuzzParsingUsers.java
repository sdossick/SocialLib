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
 * Parsing rules to fetch Google Buzz users
 * @author Expertise Android
 *
 */
public class BuzzParsingUsers extends BuzzParsing implements ParsingRules{
  
  private List<BuzzUser> contents;
  private static BuzzUser currentUser;
  
  public BuzzParsingUsers(){
    contents = new ArrayList<BuzzUser>();
  }
  
  public List<BuzzUser> getContents() {
    return contents;
  }

  public void enter(String tag, Attributes attributes, int lvl) {
    if(tag.equals(ENTRY)) addUser();
  }

  public void leave(String tag, int lvl) {}

  public void text(String tag, String text, int lvl) {
    if(tag.equals(ID)) setId(text);
    if(tag.equals(DISPLAYNAME)) setName(text);
  }
  
  private void addUser(){
    BuzzUser bu = new BuzzUser();
    contents.add(bu);
    currentUser = bu;
  }
  
  private void setName(String text){
    currentUser.username = text;
  }
  
  private void setId(String text){
    currentUser.id = text;
  }

}