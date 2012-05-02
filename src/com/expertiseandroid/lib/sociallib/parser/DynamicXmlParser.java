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

package com.expertiseandroid.lib.sociallib.parser;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

/**
 * A syntactic parser for XML parsing
 * Whenever a tag is encountered, it should call a method from the ParsingRules object
 * @author Expertise Android
 *
 */
public class DynamicXmlParser extends DefaultHandler{

  public ParsingRules rules;
  private Stack<String> stack;
  private int lvl;

  /**
   * Constructs a syntactic Xml parser
   * @param rules semantic parsing rules associated with the parser
   */
  public DynamicXmlParser(ParsingRules rules){
    super();
    this.rules = rules;
    this.stack = new Stack<String>();
    this.lvl = -1;
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes){
    stack.push(localName);
    rules.enter(localName, attributes, ++lvl);
  }

  @Override
  public void characters(char[] ch, int start, int length){
    rules.text(stack.peek(), new String(ch, start, length), lvl);
  }

  @Override
  public void endElement(String uri, String localName, String qName){
    stack.pop();
    rules.leave(localName, lvl--);
  }



}