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

package com.expertiseandroid.lib.sociallib.parser.rules.linkedin;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import com.expertiseandroid.lib.sociallib.model.linkedin.LinkedInPost;
import com.expertiseandroid.lib.sociallib.model.linkedin.LinkedInPostType;
import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

/**
 * ParsingRules to fetch LinkedIn posts data
 * @author Expertise Android
 *
 */
public class LinkedInParsingPosts extends LinkedInParsing implements ParsingRules{

  protected List<LinkedInPost> content;
  protected LinkedInPost currentPost;
  
  public LinkedInParsingPosts(){
    this.content = new ArrayList<LinkedInPost>();
  }

  public List<LinkedInPost> getContents() {
    return content;
  }

  public void enter(String tag, Attributes attributes, int lvl) {
    if(tag.equals(UPDATE)) addUpdate();
  }

  protected void addUpdate() {
    LinkedInPost lip = new LinkedInPost();
    content.add(lip);
    currentPost = lip;
  }

  public void leave(String tag, int lvl) {

  }

  public void text(String tag, String text, int lvl) {
    if(tag.equals(UPD_KEY)) setId(text);
    else if(tag.equals(CSTATUS)) setStatus(text);
    else if(tag.equals(FNAME)) setUserFName(text);
    else if(tag.equals(LNAME)) setUserLName(text);
    else if(tag.equals(ID)) setUserId(text);
    else if(tag.equals(ISCOMMENTABLE)) setIsCommentable(text);
    else if(tag.equals(UPD_TYPE)) setType(text);
  }

  private void setType(String text) {
    currentPost.type = LinkedInPostType.getTypeByName(text);
  }

  private void setIsCommentable(String text) {
    currentPost.isCommentable = new Boolean(text);
  }

  private void setUserId(String text) {
    currentPost.userId = text;  
  }

  private void setUserLName(String text) {
    currentPost.userLName = text;
  }

  private void setUserFName(String text) {
    currentPost.userFName = text;
  }

  private void setStatus(String text) {
    currentPost.content = text;
  }

  private void setId(String text) {
    currentPost.id = text;
  }
}