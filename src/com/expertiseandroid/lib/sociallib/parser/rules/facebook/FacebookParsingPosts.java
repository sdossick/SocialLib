
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

import com.expertiseandroid.lib.sociallib.model.facebook.FacebookPost;
import com.expertiseandroid.lib.sociallib.model.facebook.FacebookUser;
import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

/**
 * Parsing rules to fetch Facebook posts data
 * @author Expertise Android
 *
 */
public class FacebookParsingPosts extends FacebookParsing implements ParsingRules{
  
  private List<FacebookPost> content;
  private FacebookPost currentPost;
  private FacebookUser currentUser;
  private boolean inUser;
  private boolean inComments;
  
  public FacebookParsingPosts(){
    content = new ArrayList<FacebookPost>();
    inUser = false;
    inComments = false;
  }

  public List<?> getContents() {
    return content;
  }

  public void enter(String tag, Attributes attributes, int lvl) {
    if(tag.equals(DATA)){
      if(lvl==1) addPost();
    }
    else if(tag.equals(FROM)) addFrom();
    else if(tag.equals(COMMENTS)) inComments = true;
  }

  private void addFrom() {
    currentUser = new FacebookUser();
    inUser = true;
  }

  private void addPost() {
    FacebookPost fp = new FacebookPost();
    content.add(fp);
    currentPost = fp;
  }

  public void text(String tag, String text, int lvl) {
    if(inComments){
      return;
    }
    
    if(tag.equals(ID)&&!inUser) setId(text);
    else if(tag.equals(ID)&&inUser) setUserId(text);
    else if(tag.equals(NAME)){
      if(lvl==1) setMessage(text);
      else setUserName(text);
    }
    else if(tag.equals(MESSAGE)) setMessage(text);
    else if(tag.equals(PRIVACY)) setPrivacy(text);
    else if(tag.equals(TYPE)) setType(text);
    else if(tag.equals(LIKES)) setLikes(text);
    else if(tag.equals(COMMENTS)) inComments = true;
    
  }

  private void setLikes(String text) {
    currentPost.likes = new Integer(text);
  }

  private void setType(String text) {}

  private void setPrivacy(String text) {}

  private void setUserName(String text) {
    currentUser.name = text;
  }

  private void setMessage(String text) {
    currentPost.message = text;
  }

  private void setUserId(String text) {
    currentUser.id = new Long(text);
  }

  private void setId(String text) {
    currentPost.id = text;  
  }

  public void leave(String tag, int lvl) {
    if(tag.equals(FROM)) leaveFrom();
    else if(tag.equals(COMMENTS)) inComments = false;
  }

  private void leaveFrom() {
    inUser = false;
    currentPost.author = currentUser;
  }
}