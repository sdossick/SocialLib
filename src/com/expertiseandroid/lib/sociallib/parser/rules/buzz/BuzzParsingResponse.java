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

import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

/**
 * Parsing Rules to fetch Google Buzz responses
 * @author Expertise Android
 *
 */
public class BuzzParsingResponse extends BuzzParsing implements ParsingRules{

  private static final String TRUE = "true";
  private static final String FALSE = "false";
  
  private String descriptor;
  
  public BuzzParsingResponse(){
    descriptor = TRUE;
  }


  public List<?> getContents() {
    List<String> list = new ArrayList<String>();
    list.add(descriptor);
    return list;
  }
  
  public void enter(String tag, Attributes attributes, int lvl) {
    if(tag.equals(ERROR)){
      descriptor = FALSE;
    }
  }

  public void leave(String tag, int lvl) {}

  public void text(String tag, String text, int lvl) {
    if(tag.equals(MESSAGE)) descriptor = text;
  }

}