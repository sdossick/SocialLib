
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

package com.expertiseandroid.lib.sociallib.exceptions;

/**
 * A NotAuthentifiedException is thrown if a request that needs authentication
 * is about to be submitted and the application is not authenticated to the
 * corresponding social network
 * 
 * @author Expertise Android
 * 
 */
public class NotAuthentifiedException extends Exception {

  private static final long serialVersionUID = 4534050283336967423L;
  private String network;

  public NotAuthentifiedException(String network) {
    this.network = network;
  }

  @Override
  public String getMessage() {
    return "You are not authentified to " + network;
  }

}