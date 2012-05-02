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

package com.expertiseandroid.lib.sociallib.connectors.interfaces;

import java.io.IOException;

import org.json.JSONException;

/**
 * An interface for social networks that supports photo sharing
 * @author ExpertiseAndroid
 *
 */
public interface PhotoSocialNetwork {

  /**
   * Sends a picture to the default location
   * @param filePath picture file path
   * @param caption a caption associated with the picture
   * @return
   * @throws IOException
   * @throws JSONException 
   */
  public boolean sendPhoto(String filePath, String caption) throws IOException, JSONException;
  
  /**
   * Sends a picture to the specified album or resource
   * @param filePath picture file path
   * @param caption a caption associated with the picture
   * @param albumId an identifier for the album that the picture should be uploaded to
   * @return
   * @throws IOException
   * @throws JSONException 
   */
  public boolean sendPhoto(String filePath, String caption, String albumId)throws IOException, JSONException;

}