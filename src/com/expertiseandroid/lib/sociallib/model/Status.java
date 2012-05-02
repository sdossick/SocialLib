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

package com.expertiseandroid.lib.sociallib.model;

import java.util.Date;

/**
 * A class to represent status posts
 * @author Expertise Android
 *
 */
public class Status extends Post{

  public String contents;
  
  /**
   * Creates a new status
   * @param author the
   * @param date
   * @param contents
   */
  public Status(User author, Date date, String contents){
    construct(PostType.status, author, date);
    this.contents = contents;
  }
  
  /**
   * Creates a new status
   * @param contents the contents of the status, which would be the message of the status
   */
  public Status(String contents){
    this.contents = contents;
  }

  @Override
  public String getContents() {
    return contents;
  }
  
  @Override
  public void setContents(String contents) {
    this.contents = contents;
  }

  @Override
  public String getId() {
    return null;
  }

  @Override
  public void setId(String id) {}
}