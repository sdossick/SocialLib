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

import com.expertiseandroid.lib.sociallib.model.linkedin.LinkedInUser;
import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

/**
 * ParsingRules to fetch LinkedIn users data
 * @author Expertise Android
 *
 */
public class LinkedInParsingUsers extends LinkedInParsing implements ParsingRules{
  
  private List<LinkedInUser> content;
  private LinkedInUser currentUser;
  private boolean singleUser;
  
  public LinkedInParsingUsers(boolean singleUser){
    this.content = new ArrayList<LinkedInUser>();
    this.singleUser = singleUser;
  }
  
  public List<LinkedInUser> getContents() {
    return content;
  }
  
  public void enter(String tag, Attributes attributes, int lvl) {
    if(singleUser&&lvl<1){
      addUser();
    }else if(tag.equals(LinkedInParsing.PERSON)&&lvl>1) addUser();
  }

  private void addUser() {
    //if(singleUser) return; //do not add the current user to the list
    LinkedInUser lu = new LinkedInUser();
    content.add(lu);
    currentUser = lu;
  }

  public void leave(String tag, int lvl) {
  }

  public void text(String tag, String text, int lvl) {
    if(tag.equals(LinkedInParsing.ID)) setId(text);
    else if(tag.equals(LinkedInParsing.FNAME)) setFName(text);
    else if(tag.equals(LinkedInParsing.LNAME)) setLName(text);
    else if(tag.equals(LinkedInParsing.INDUSTRY)) setIndustry(text);
    else if(tag.equals(LinkedInParsing.HEADLINE)) setHeadline(text);
    else if(tag.equals(LinkedInParsing.DISTANCE)) setDistance(text);
    else if(tag.equals(LinkedInParsing.CSTATUS)) setCStatus(text);
    else if(tag.equals(LinkedInParsing.NCONNECTIONS)) setNConnections(text);
  }

  private void setCStatus(String text) {
    currentUser.status = text;
  }

  private void setNConnections(String text) {
    currentUser.nConnections = new Integer(text);
  }

  private void setDistance(String text) {
    currentUser.distance = new Integer(text);
  }

  private void setHeadline(String text) {
    currentUser.headline = text;
  }

  private void setIndustry(String text) {
    currentUser.industry = text;
  }

  private void setLName(String text) {
    currentUser.lastName = text;
  }

  private void setFName(String text) {
    currentUser.firstName = text;
  }

  private void setId(String text) {
    currentUser.id = text;
  }
}