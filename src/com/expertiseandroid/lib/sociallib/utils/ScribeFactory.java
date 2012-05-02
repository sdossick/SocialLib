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

package com.expertiseandroid.lib.sociallib.utils;

import java.util.Properties;

import org.scribe.oauth.Scribe;

/**
 * A factory class to instantiate Scribe objects that are in charge of
 * authentication using OAuth
 * @author ExpertiseAndroid
 *
 */
public class ScribeFactory {
  
  private static final String POST = "POST";
  
  private static final String PROP_KEY = "consumer.key";
  private static final String PROP_SECRET = "consumer.secret";
  private static final String PROP_REQ_VERB = "request.token.verb";
  private static final String PROP_REQ_URL = "request.token.url";
  private static final String PROP_ACC_VERB = "access.token.verb";
  private static final String PROP_ACC_URL = "access.token.url";
  private static final String PROP_CALLBACK = "callback.url";
  private static final String PROP_EQUALIZER = "scribe.equalizer";
  
  private static final String TWITTER_REQUEST = "http://api.twitter.com/oauth/request_token";
  private static final String TWITTER_ACCESS = "http://api.twitter.com/oauth/access_token";
  
  private static final String LINKEDIN_REQUEST = "https://api.linkedin.com/uas/oauth/requestToken";
  private static final String LINKEDIN_ACCESS = "https://api.linkedin.com/uas/oauth/accessToken";
  private static final String LINKEDIN_EQUALIZER = "org.scribe.eq.LinkedInEqualizer";
  
  
  public static Scribe getTwitterScribe(String consumerKey, String consumerSecret, String callback){
      Properties props = new Properties();
        props.setProperty(PROP_KEY, consumerKey);
        props.setProperty(PROP_SECRET, consumerSecret);
        props.setProperty(PROP_REQ_VERB, POST);
        props.setProperty(PROP_REQ_URL, TWITTER_REQUEST);
        props.setProperty(PROP_ACC_VERB, POST);
        props.setProperty(PROP_ACC_URL, TWITTER_ACCESS);
        props.setProperty(PROP_CALLBACK, callback);
        props.setProperty(PROP_EQUALIZER, "org.scribe.oauth.TwitterEqualizer");
        return new Scribe(props);
  }
  
  public static Scribe getLinkedInScribe(String consumerKey, String consumerSecret, String callback){
    Properties props = new Properties();
    props.setProperty(PROP_KEY, consumerKey);
        props.setProperty(PROP_SECRET, consumerSecret);
        props.setProperty(PROP_REQ_VERB, POST);
        props.setProperty(PROP_REQ_URL, LINKEDIN_REQUEST);
        props.setProperty(PROP_ACC_VERB, POST);
        props.setProperty(PROP_ACC_URL, LINKEDIN_ACCESS);
        props.setProperty(PROP_CALLBACK, callback);
        props.setProperty(PROP_EQUALIZER, LINKEDIN_EQUALIZER);
    
    return new Scribe(props);
    
  }

}