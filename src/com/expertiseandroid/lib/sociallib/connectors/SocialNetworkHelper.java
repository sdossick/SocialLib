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

package com.expertiseandroid.lib.sociallib.connectors;

import com.facebook.android.Facebook;

/**
 * A helper class to instantiate social networks connectors
 * @author ExpertiseAndroid
 *
 */
public class SocialNetworkHelper {

  /**
   * Creates a new FacebookConnector
   * @param appId the facebook application id
   * @param permissions a list of permissions
   * @see http://developers.facebook.com/docs/authentication/permissions
   * @return
   */
  public static FacebookConnector createFacebookConnector(String appId, String[] permissions){
    return new FacebookConnector(appId, permissions);
  }

  /**
   * Creates a new FacebookConnector with a dialog listener
   * A Facebook Dialog is a callback interface to listen to dialog events
   * @see com.facebook.android.Facebook.DialogListener
   * @param appId the facebook application id
   * @param permissions a list of permission
   * @see http://developers.facebook.com/docs/authentication/permissions
   * @param fbListener the callback interface for facebook dialogs events
   * @return
   */
  public static FacebookConnector createFacebookConnector(String appId, String[] permissions, Facebook.DialogListener fbListener){
    return new FacebookConnector(appId, permissions, fbListener);
  }

  /**
   * Creates a new TwitterConnector
   * @param consumerKey the application's consumer key
   * @param consumerSecret the application's consumer secret
   * @param callback the callback URI
   * @return
   */
  public static TwitterConnector createTwitterConnector(String consumerKey, String consumerSecret, String callback){
    return new TwitterConnector(consumerKey, consumerSecret, callback);
  }
  
  /**
   * Creates a new TwitterConnector, with a TwitPic key, in order to be able to upload pictures
   * @param consumerKey the application's consumer key
   * @param consumerSecret the application's consumer secret
   * @param callback the callback URI
   * @param twitPicKey the application's TwitPic key
   * @return
   */
  public static TwitterConnector createTwitterConnector(String consumerKey, String consumerSecret, String callback, String twitPicKey){
    return new TwitterConnector(consumerKey, consumerSecret, callback, twitPicKey);
  }

  /**
   * Creates a new BuzzConnector
   * @param consumerKey the application's consumer key
   * @param consumerSecret the application's consumer secret
   * @param callback the callback URI
   * @return
   */
  public static BuzzConnector createBuzzConnector(String consumerKey, String consumerSecret, String callback){
    return new BuzzConnector(consumerKey, consumerSecret, callback);
  }

  /**
   * Creates a new LinkedInConnector
   * @param consumerKey the application's consumer key
   * @param consumerSecret the application's consumer secret
   * @param callback the callback URI
   * @return
   */
  public static LinkedInConnector createLinkedInConnector(String consumerKey, String consumerSecret, String callback){
    return new LinkedInConnector(consumerKey, consumerSecret, callback);
  }
  
  public static DiggConnector createDiggConnector(String consumerKey, String consumerSecret, String callback){
    return new DiggConnector(consumerKey, consumerSecret, callback);
  }

}