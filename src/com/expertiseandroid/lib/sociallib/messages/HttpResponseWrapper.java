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

package com.expertiseandroid.lib.sociallib.messages;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

/**
 * A Wrapper class for HttpResponses to implement ReadableResponse
 * @author Expertise Android
 *
 */
public class HttpResponseWrapper implements ReadableResponse{

  public HttpResponse response;
  private StringBuilder sb;
  private BufferedReader reader;
  
  /**
   * Constructs a ReadableResponse from an HttpResponse
   * @param response
   */
  public HttpResponseWrapper(HttpResponse response){
    this.response = response;
    sb = new StringBuilder();
  }

  public String getContents() {
    String line;
    try {
      reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      while((line=reader.readLine()) != null){
        sb.append(line).append('\n');
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return sb.toString();
  }
  
  
}