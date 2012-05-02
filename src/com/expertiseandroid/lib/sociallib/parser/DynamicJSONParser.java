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

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

/**
 * A syntactic parser for JSON parsing
 * Whenever a tag is encountered, it should call a method from the ParsingRules object
 * @author Expertise Android
 *
 */
public class DynamicJSONParser {
  
  private static final String ROOT = "root";
  
  public ParsingRules rules;
  private int lvl;
  
  /**
   * Constructs a syntactic JSON parser
   * @param rules semantic parsing rules associated with the parser
   */
  public DynamicJSONParser(ParsingRules rules){
    this.rules = rules;
    this.lvl = -1;
  }
  
  /**
   * Parse a JSON string
   * @param jsonString
   * @throws JSONException
   */
  public void parse(String jsonString) throws JSONException{
    JSONObject root = new JSONObject(jsonString);
    parseJSONObject(ROOT, root);
  }
  
  @SuppressWarnings("unchecked")
  private void parseJSONObject(String key, JSONObject obj) throws JSONException{
    rules.enter(key, null, ++lvl);
    
    for(Iterator<String> it = obj.keys(); it.hasNext();){
      String childKey = it.next();
      Object child = obj.get(childKey);
      parseUnknown(childKey, child, child.getClass());
    }

    rules.leave(key, lvl--);
  }
  
  private void parseJSONArray(String key, JSONArray arr) throws JSONException{
    for(int i=0; i<arr.length(); i++){
      Object o = arr.get(i);
      parseUnknown(key, o, o.getClass());
    }
  }
  
  private void parseKeyValuePair(String key, Object value){
    rules.text(key, value.toString(), lvl);
  }
  
  private void parseUnknown(String key, Object child, Class<? extends Object> c) throws JSONException{
    if(c.equals(JSONObject.class)) parseJSONObject(key, (JSONObject) child);
    else if(c.equals(JSONArray.class)) parseJSONArray(key, (JSONArray) child);
    else parseKeyValuePair(key, child);
  }
  
  

}