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

package com.expertiseandroid.lib.sociallib.model.buzz;

import com.expertiseandroid.lib.sociallib.model.Post;

/**
 * An activity on Google Buzz
 * @author Expertise Android
 *
 */
public class BuzzActivity extends Post{
  
  public String contents, title, id;
  public BuzzUser author;
  public String date;
  
  public BuzzActivity(){
    this.type = PostType.status;
  }

  @Override
  public String getContents() {
    return contents;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setContents(String content) {
    this.contents = content;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

}