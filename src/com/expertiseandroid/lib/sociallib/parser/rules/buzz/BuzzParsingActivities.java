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

import com.expertiseandroid.lib.sociallib.model.buzz.BuzzActivity;
import com.expertiseandroid.lib.sociallib.model.buzz.BuzzUser;
import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

/**
 * Parsing rules to fetch Google Buzz activities
 * @author Expertise Android
 *
 */
public class BuzzParsingActivities extends BuzzParsing implements ParsingRules{
  
  private List<BuzzActivity> contents;
  private BuzzActivity currentActivity;
  
  public BuzzParsingActivities(){
    contents = new ArrayList<BuzzActivity>();
  }
  
  public List<BuzzActivity> getContents() {
    return contents;
  }
  
  public void enter(String tag, Attributes attributes, int lvl) {
    if(tag.equals(ITEMS)) addActivity();
  }

  public void leave(String tag, int lvl) {
  }

  public void text(String tag, String text, int lvl) {
    if(tag.equals(CONTENT)) setContent(text);
    else if(tag.equals(UPDATED)&&lvl==2) setDate(text);
    else if(tag.equals(NAME)&&lvl==3) setUserName(text);
    else if(tag.equals(TITLE)) setTitle(text);
    else if(tag.equals(ID)){
      if(lvl==2)setId(text);
      else if(lvl==3)setUserId(text);
    }
  }
  
  private void addActivity(){
    BuzzActivity ba = new BuzzActivity();
    ba.author = new BuzzUser();
    contents.add(ba);
    currentActivity = ba;
  }
  
  private void setContent(String text){
    currentActivity.contents = text;
  }
  
  private void setId(String text){
    currentActivity.id = text;
  }
  
  private void setTitle(String title){
    currentActivity.title = title;
  }
  
  private void setUserId(String text){
    currentActivity.author.id = text;
  }
  
  private void setUserName(String text){
    currentActivity.author.username = text;
  }
  
  private void setDate(String text){
    currentActivity.date = text;
  }

}