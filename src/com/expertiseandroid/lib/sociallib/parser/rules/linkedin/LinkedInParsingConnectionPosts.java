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

package com.expertiseandroid.lib.sociallib.parser.rules.linkedin;

import org.xml.sax.Attributes;

import com.expertiseandroid.lib.sociallib.model.linkedin.LinkedInConnectionPost;

/**
 * An object to parse LinkedIn connection posts
 * @author Expertise Android
 *
 */
public class LinkedInParsingConnectionPosts extends LinkedInParsingPosts {

  private boolean isInConnections;
  private LinkedInConnectionPost currentConnPost;
  
  public LinkedInParsingConnectionPosts(){
    super();
    isInConnections = false;
  }
  
  @Override
  public void enter(String tag, Attributes attributes, int lvl) {
    if(tag.equals(CONNECTIONS)) isInConnections = true;
    super.enter(tag, attributes, lvl);
  }

  @Override
  public void leave(String tag, int lvl) {
    super.leave(tag, lvl);
    if(tag.equals(CONNECTIONS)) isInConnections = false;
  }

  @Override
  public void text(String tag, String text, int lvl) {
    if(isInConnections){
      if(tag.equals(ID)) setConnId(text);
      else if(tag.equals(FNAME)) setConnFName(text);
      else if(tag.equals(LNAME)) setConnLName(text);
    }
    else super.text(tag, text, lvl);
  }
  
  @Override
  protected void addUpdate() {
    LinkedInConnectionPost lip = new LinkedInConnectionPost();
    content.add(lip);
    currentPost = lip;
    currentConnPost = lip;
  }

  private void setConnLName(String text) {
    currentConnPost.connLName = text;
  }

  private void setConnFName(String text) {
    currentConnPost.connFName = text;
  }

  private void setConnId(String text) {
    currentConnPost.connId = text;
  }
}