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
import com.expertiseandroid.lib.sociallib.model.linkedin.LinkedInConnectionPost;
import com.expertiseandroid.lib.sociallib.model.linkedin.LinkedInPost;
import com.expertiseandroid.lib.sociallib.model.linkedin.LinkedInUser;
import com.expertiseandroid.lib.sociallib.parser.rules.linkedin.LinkedInParsingConnectionPosts;
import com.expertiseandroid.lib.sociallib.parser.rules.linkedin.LinkedInParsingPosts;
import com.expertiseandroid.lib.sociallib.parser.rules.linkedin.LinkedInParsingUsers;
import com.expertiseandroid.lib.sociallib.utils.Utils;

/**
 * An object that is in charge of parsing LinkedIn responses
 * @author Expertise Android
 *
 */
public class LinkedInReader{

  /**
   * Extracts a single LinkedInUser
   * @param response an XML-formatted response
   * @return the target user
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public LinkedInUser readUser(ReadableResponse response) throws SAXException, ParserConfigurationException, IOException{
    List<LinkedInUser> list = (List<LinkedInUser>) Utils.parseXML(response, new LinkedInParsingUsers(true));
    if(list.size()==1) return list.get(0);
    else return null;
  }
  
  /**
   * Extracts a list of LinkedInUsers
   * @param response an XML-formatted response
   * @return the list of users
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public List<LinkedInUser> readUsers(ReadableResponse response) throws SAXException, ParserConfigurationException, IOException{
    return (List<LinkedInUser>) Utils.parseXML(response, new LinkedInParsingUsers(false));
  }

  /**
   * Extracts a list of LinkedInPosts
   * @param response an XML-formatted response
   * @return the list of posts
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public List<LinkedInPost> readPosts(ReadableResponse response) throws SAXException, ParserConfigurationException, IOException {
    return (List<LinkedInPost>) Utils.parseXML(response, new LinkedInParsingPosts());
  }

  /**
   * Extracts a list of LinkedInConnectionPosts
   * @param response an XML-formatted response
   * @return the list of connection posts
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public List<LinkedInConnectionPost> readConnectionPosts(ReadableResponse response) throws SAXException, ParserConfigurationException, IOException {
    return (List<LinkedInConnectionPost>) Utils.parseXML(response, new LinkedInParsingConnectionPosts());
  }
  
  /**
   * Warning : not yet implemented for LinkedIn, will always return true
   * Determines whether an action was successful or not
   * The message of the response is printed on the debug output
   * @param response an XML-formatted response
   * @return true if the operation was successful
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */
  public boolean readResponse(ReadableResponse response) throws SAXException, ParserConfigurationException, IOException{
    return true;
  }
}