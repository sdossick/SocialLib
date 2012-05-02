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

package com.expertiseandroid.lib.sociallib.parser.rules.twitter;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import com.expertiseandroid.lib.sociallib.model.twitter.TwitterStatus;
import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

/**
 * ParsingRules to fetch Twitter statuses data
 * @author Expertise Android
 *
 */
public class TwitterParsingStatuses extends TwitterParsing implements ParsingRules{

  private List<TwitterStatus> list;
  private TwitterStatus currentStatus;
  private boolean inUser;

  public TwitterParsingStatuses(){
    list = new ArrayList<TwitterStatus>();
    inUser = false;
  }

  public List<TwitterStatus> getContents() {
    return list;
  }
  
  public void enter(String tag, Attributes attributes, int lvl) {
    if(tag.equals(USER)) addUser();
    else if(tag.equals(STATUS)) addStatus();
  }

  public void leave(String tag, int lvl) {
    if(tag.equals(USER)) endUser();
  }

  public void text(String tag, String text, int lvl) {
    if(tag.equals(ID)&&inUser) setUserId(text);
    else if(tag.equals(ID)&&!inUser) setStatusId(text);
    else if(tag.equals(SCREEN_NAME)) setScreenName(text);
    else if(tag.equals(DATE)) setDate(text);
    else if(tag.equals(TEXT)) setText(text);
    else if(tag.equals(SOURCE)) setSource(text);
  }

  private void setSource(String text) {
    currentStatus.source = text;
  }

  private void setText(String text) {
    currentStatus.contents = text;
  }

  private void setDate(String text) {
    currentStatus.dateString = text;
  }

  private void setScreenName(String text) {
    currentStatus.userScreenName = text;  
  }

  private void setStatusId(String text) {
    currentStatus.id = text;
    
  }

  private void setUserId(String text) {}

  private void endUser() {
    inUser = false;
  }

  private void addStatus() {
    TwitterStatus ts = new TwitterStatus("");
    list.add(ts);
    currentStatus = ts;  
  }

  private void addUser() {
    inUser = true;
  }
}