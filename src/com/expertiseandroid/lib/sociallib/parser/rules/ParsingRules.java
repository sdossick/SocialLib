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

package com.expertiseandroid.lib.sociallib.parser.rules;

import java.util.List;

import org.xml.sax.Attributes;

/**
 * ParsingRules are semantic analyzers.
 * A Parsing Rule method is fired by the syntactic parser.
 * A Parsing Rule is in charge of building the object to return after the parsing is complete
 * @author Expertise Android
 *
 */
public interface ParsingRules {
  
  /**
   * Fired when the syntactic parser enters a new tag
   * @param tag
   * @param attributes the tag's attributes
   * @param lvl level of imbrication
   */
  public void enter(String tag, Attributes attributes, int lvl);
  
  /**
   * Fired when the syntactic parser enters a tag containing text data
   * @param tag
   * @param text the text data of the tag
   * @param lvl level of imbrication
   */
  public void text(String tag, String text, int lvl);
  
  /**
   * Fired when the syntactic parser leaves a tag
   * @param tag
   * @param lvl level of imbrication
   */
  public void leave(String tag, int lvl);
  
  /**
   * Accessor to the object built by the semantic analyzer.
   * The object should always be a List, even if it contains 0 or 1 element.
   * @return the object built by the semantic analyzer
   */
  public abstract List<?> getContents();

}