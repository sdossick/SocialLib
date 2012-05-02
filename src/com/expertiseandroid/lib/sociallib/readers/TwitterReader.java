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

package com.expertiseandroid.lib.sociallib.readers;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.expertiseandroid.lib.sociallib.messages.ReadableResponse;
import com.expertiseandroid.lib.sociallib.model.twitter.TwitterStatus;
import com.expertiseandroid.lib.sociallib.model.twitter.TwitterUser;
import com.expertiseandroid.lib.sociallib.parser.rules.twitter.TwitterParsingResponse;
import com.expertiseandroid.lib.sociallib.parser.rules.twitter.TwitterParsingStatuses;
import com.expertiseandroid.lib.sociallib.parser.rules.twitter.TwitterParsingUrl;
import com.expertiseandroid.lib.sociallib.parser.rules.twitter.TwitterParsingUsers;
import com.expertiseandroid.lib.sociallib.utils.Utils;

/**
 * An object that is in charge of parsing Twitter responses
 * @author Expertise Android
 *
 */
public class TwitterReader{

  /**
   * Extracts a list of tweets
   * @param response an XML-formatted response
   * @return the list of posts
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public List<TwitterStatus> readStatuses(ReadableResponse response) throws SAXException, ParserConfigurationException, IOException{
    List<TwitterStatus> list = (List<TwitterStatus>) Utils.parseXML(response, new TwitterParsingStatuses());
    return list;
  }
  
  /**
   * Extracts a list of users
   * @param response an XML-formatted response
   * @return the list of users
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public List<TwitterUser> readUsers(ReadableResponse response) throws SAXException, ParserConfigurationException, IOException{
    List<TwitterUser> list = (List<TwitterUser>) Utils.parseXML(response, new TwitterParsingUsers());
    return list;
  }
  
  /**
   * Extracts a single user
   * @param response an XML-formatted response
   * @return the target user
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public TwitterUser readUser(ReadableResponse response) throws SAXException, ParserConfigurationException, IOException{
    List<TwitterUser> list = (List<TwitterUser>) Utils.parseXML(response, new TwitterParsingUsers());
    if(list.size()==1) return list.get(0);
    return null;
  }
  
  /**
   * Determines whether an action was successful or not
   * The message of the response is printed on the debug output
   * @param response an XML-formatted response
   * @return true if the operation was successful
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */
  public boolean readResponse(ReadableResponse response) throws SAXException, ParserConfigurationException, IOException{
    return (Boolean) Utils.parseXML(response, new TwitterParsingResponse());
  }
  
  /**
   * Extracts a URL
   * @param response an XML-formatted response
   * @return the URL extracted
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */
  public String readUrl(ReadableResponse response) throws SAXException, ParserConfigurationException, IOException{
    return (String) Utils.parseXML(response, new TwitterParsingUrl());
  }


  

}