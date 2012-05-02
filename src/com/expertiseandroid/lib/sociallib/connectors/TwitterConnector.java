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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.scribe.http.Request;
import org.scribe.oauth.Token;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.expertiseandroid.lib.sociallib.connectors.interfaces.FollowersSocialNetwork;
import com.expertiseandroid.lib.sociallib.connectors.interfaces.PostsSocialNetwork;
import com.expertiseandroid.lib.sociallib.connectors.interfaces.SignedCustomRequestSocialNetwork;
import com.expertiseandroid.lib.sociallib.exceptions.NotAuthentifiedException;
import com.expertiseandroid.lib.sociallib.messages.HttpResponseWrapper;
import com.expertiseandroid.lib.sociallib.messages.ReadableResponse;
import com.expertiseandroid.lib.sociallib.messages.ScribeResponseWrapper;
import com.expertiseandroid.lib.sociallib.model.Post;
import com.expertiseandroid.lib.sociallib.model.twitter.TwitterStatus;
import com.expertiseandroid.lib.sociallib.model.twitter.TwitterUser;
import com.expertiseandroid.lib.sociallib.readers.TwitterReader;
import com.expertiseandroid.lib.sociallib.utils.Utils;

/**
 * A connector to Twitter that provides methods to retrieve data and post content
 * @author ExpertiseAndroid
 *
 */
public class TwitterConnector implements PostsSocialNetwork, FollowersSocialNetwork, SignedCustomRequestSocialNetwork{

  private static final String GET_USER = "http://api.twitter.com/1/account/verify_credentials.xml";
  private static final String GET_FOLLOWERS = "http://api.twitter.com/1/statuses/followers.xml";
  private static final String GET_FOLLOWED = "http://api.twitter.com/1/statuses/friends.xml";
  private static final String POST_CONTENT = "http://api.twitter.com/1/statuses/update.xml";
  private static final String POST_RETWEET = "http://api.twitter.com/1/statuses/retweet/";
  private static final String DOTXML = ".xml";
  private static final String GET_MY_TL = "http://api.twitter.com/1/statuses/user_timeline.xml";
  private static final String GET_FRIENDS_TL = "http://api.twitter.com/1/statuses/friends_timeline.xml";
  private static final String GET_HOME_TL = "http://api.twitter.com/1/statuses/home_timeline.xml";
  private static final String GET_PUBLIC_TL = "http://api.twitter.com/1/statuses/public_timeline.xml";
  private static final String LOGOUT = "http://api.twitter.com/version/account/end_session.xml";
  private static final String TWITTER_REQUEST = "http://api.twitter.com/oauth/request_token";
  private static final String TWITTER_ACCESS = "http://api.twitter.com/oauth/access_token";
  private static final String AUTHORIZE = "http://api.twitter.com/oauth/authorize";
  private static final String STATUS = "status";
  private static final String NETWORK = "Twitter";

  private CommonsHttpOAuthProvider httpOauthprovider;
  private Token accessToken;
  private TwitterReader reader;
  private boolean authentified;
  private String callback;
  
  /**
   * The OAuth consumer, used to sign OAuth requests
   */
  public CommonsHttpOAuthConsumer httpOauthConsumer;

  /**
   * The application's TwitPic key to upload photos
   */
  public String twitPicKey;
  
  protected TwitterConnector(String consumerKey, String consumerSecret, String callback){
    this(consumerKey, consumerSecret, callback, "");
  }

  protected TwitterConnector(String consumerKey, String consumerSecret, String callback, String twitPicKey){
    this.authentified = false;
    this.reader = new TwitterReader();
    this.twitPicKey = twitPicKey;
    this.callback = callback;
    httpOauthConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    httpOauthprovider = new CommonsHttpOAuthProvider(TWITTER_REQUEST, TWITTER_ACCESS, AUTHORIZE);

  }

  /**
   * Gets the current user's public timeline
   * @return a list of TwitterStatus corresponding to the public timeline
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws NotAuthentifiedException
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   */
  public List<TwitterStatus> getPublicTimeLine() throws SAXException, ParserConfigurationException, IOException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = signedGetRequest(GET_PUBLIC_TL);
    return reader.readStatuses(response);
  }

  /**
   * Gets the current user's home timeline
   * @return a list of TwitterStatus corresponding to the home timeline
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws NotAuthentifiedException
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   */
  public List<TwitterStatus> getHomeTimeLine() throws SAXException, ParserConfigurationException, IOException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = signedGetRequest(GET_HOME_TL);
    return reader.readStatuses(response);
  }

  /**
   * Gets the current user's friends timeline
   * @return a list of TwitterStatus corresponding to the friends timeline
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws NotAuthentifiedException
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   */
  public List<TwitterStatus> getFriendsTimeLine() throws SAXException, ParserConfigurationException, IOException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = signedGetRequest(GET_FRIENDS_TL);
    return reader.readStatuses(response);
  }

  /**
   * Gets the current user's timeline
   * @return a list of TwitterStatus corresponding to the user's timeline
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws NotAuthentifiedException
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   */
  public List<TwitterStatus> getUserTimeLine() throws SAXException, ParserConfigurationException, IOException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = signedGetRequest(GET_MY_TL);
    return reader.readStatuses(response);
  }

  /**
   * Gets the current user's timeline
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   */
  public List<TwitterStatus> getWallPosts() throws SAXException, ParserConfigurationException, IOException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
    return getHomeTimeLine();
  }

  /**
   * Sends a tweet
   * @param tweet the tweet's content
   * @return true if the operation was successful
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws NotAuthentifiedException
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   */
  public boolean tweet(String tweet) throws SAXException, ParserConfigurationException, IOException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    List<NameValuePair> bodyParams = new ArrayList<NameValuePair>();
    bodyParams.add(new BasicNameValuePair(STATUS, tweet));
    ReadableResponse response = signedPostRequest(POST_CONTENT, bodyParams);
    return reader.readResponse(response);
  }
  
  /**
   * Retweets a tweet
   * @param id the numerical ID of the desired status
   * @return true if the operation was successful
   * @throws NotAuthentifiedException
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   */
  public boolean retweet(String id) throws NotAuthentifiedException, SAXException, ParserConfigurationException, IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = signedPostRequest(POST_RETWEET + id + DOTXML, new ArrayList<NameValuePair>());
    return reader.readResponse(response);
  }
  
  /**
   * Retweets a tweet
   * @param tweet the desired status
   * @return true if the operation was successful
   * @throws NotAuthentifiedException
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws OAuthCommunicationException 
   * @throws OAuthExpectationFailedException 
   * @throws OAuthMessageSignerException 
   */
  public boolean retweet(Post tweet) throws NotAuthentifiedException, SAXException, ParserConfigurationException, IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException{
    return retweet(tweet.getId());
  }
  
  public boolean post(Post content) throws NotAuthentifiedException, SAXException, ParserConfigurationException, IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
    return tweet(content.getContents());
  }

  public List<TwitterUser> getFollowed() throws SAXException, ParserConfigurationException, IOException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = signedGetRequest(GET_FOLLOWED);
    return reader.readUsers(response);
  }

  public List<TwitterUser> getFollowers() throws IllegalStateException, IOException, SAXException, ParserConfigurationException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = signedGetRequest(GET_FOLLOWERS);
    return reader.readUsers(response);
  }

  public TwitterUser getUser() throws SAXException, ParserConfigurationException, IOException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = signedGetRequest(GET_USER);
    return reader.readUser(response);
  }


  public boolean isAuthentified() {
    return authentified;
  }


  public void requestAuthorization(Context ctx) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
    String authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer, callback);
    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
  }

  public void authorize(Activity ctx) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException{
    String verifier = ctx.getIntent().getData().getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
    httpOauthprovider.retrieveAccessToken(httpOauthConsumer, verifier);
    accessToken = new Token(httpOauthConsumer.getToken(), httpOauthConsumer.getTokenSecret());
    authentified = true;
  }

  public boolean logout(Context ctx) throws NotAuthentifiedException, SAXException, ParserConfigurationException, IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = signedPostRequest(LOGOUT, new ArrayList<NameValuePair>());
    return reader.readResponse(response);
  }

  public boolean authentify(Token accessToken) {
    this.accessToken = accessToken;
    httpOauthConsumer.setTokenWithSecret(accessToken.getToken(), accessToken.getSecret());
    this.authentified = true;
    return true;
  }

  public Token getAccessToken() {
    accessToken = new Token(httpOauthConsumer.getToken(), httpOauthConsumer.getTokenSecret());
    return accessToken;
  }

  public ReadableResponse customRequest(String httpMethod, String request) {
    return customRequest(httpMethod, request, new HashMap<String,String>());
  }

  public ReadableResponse customRequest(String httpMethod, String request,Map<String, String> bodyParams) {
    Request requestObj = new Request(Utils.getScribeVerb(httpMethod), request);
    for(Iterator<String> it = bodyParams.keySet().iterator(); it.hasNext();){
      String key = it.next();
      requestObj.addBodyParameter(key, bodyParams.get(key));
    }
    return new ScribeResponseWrapper(requestObj.send());
  }

  public ReadableResponse signedCustomRequest(String httpMethod, String request) throws NotAuthentifiedException {
    return signedCustomRequest(httpMethod, request, new HashMap<String,String>());
  }

  public ReadableResponse signedCustomRequest(String httpMethod, String request, Map<String, String> bodyParams) throws NotAuthentifiedException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    Request requestObj = new Request(Utils.getScribeVerb(httpMethod), request);
    Utils.addBodyParams(requestObj, bodyParams);
    //scribe.signRequest(requestObj, accessToken);
    return new ScribeResponseWrapper(requestObj.send());
  }
  
  public ReadableResponse signedCustomRequest(String httpMethod, String request, String body) throws NotAuthentifiedException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    Request requestObj = new Request(Utils.getScribeVerb(httpMethod), request);
    requestObj.addPayload(body);
    requestObj.addHeader("X-Auth-Service-Provider", "http://api.twitter.com/1/account/verify_credentials.json");
    return new ScribeResponseWrapper(requestObj.send());
  }
  
  private ReadableResponse signedPostRequest(String request, List<NameValuePair> bodyParams) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException{
    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(request);
    post.setEntity(new UrlEncodedFormEntity(bodyParams, HTTP.UTF_8));
    post.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
    httpOauthConsumer.sign(post);
    HttpResponse response = client.execute(post);
    return new HttpResponseWrapper(response);
  }
  
  private ReadableResponse signedGetRequest(String request) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException{
    HttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet(request);
    httpOauthConsumer.sign(get);
    HttpResponse response = client.execute(get);
    return new HttpResponseWrapper(response);
  }
  
  /**
   * Sends a photo to TwitPic
   * @param filePath the path of the picture
   * @param caption a caption associated with the picture
   * @return the url of the picture on TwitPic
   * @throws OAuthMessageSignerException
   * @throws OAuthExpectationFailedException
   * @throws OAuthCommunicationException
   * @throws UnsupportedEncodingException
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException
   */
  public String sendPhoto(String filePath, String caption) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, UnsupportedEncodingException, IOException, SAXException, ParserConfigurationException{
    Bitmap image = BitmapFactory.decodeFile(filePath);
    ReadableResponse response =  Utils.postTwitpic(image, httpOauthConsumer, twitPicKey, caption);
    return reader.readUrl(response);
  }
  
  /**
   * Sends a photo to TwitPic and tweets it
   * @param tweet the tweet's content
   * @param filePath the path of the picture
   * @param caption a caption associated with the picture
   * @return true if the tweet was successfully posted
   * @throws OAuthMessageSignerException
   * @throws OAuthExpectationFailedException
   * @throws OAuthCommunicationException
   * @throws UnsupportedEncodingException
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws NotAuthentifiedException
   */
  public boolean sendPhotoAndTweet(String tweet, String filePath, String caption) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, UnsupportedEncodingException, SAXException, ParserConfigurationException, IOException, NotAuthentifiedException{
    return tweet(tweet + ' ' + sendPhoto(filePath, caption));
  }
}