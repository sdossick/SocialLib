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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.scribe.oauth.Token;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.expertiseandroid.lib.sociallib.connectors.interfaces.SignedCustomRequestSocialNetwork;
import com.expertiseandroid.lib.sociallib.exceptions.NotAuthentifiedException;
import com.expertiseandroid.lib.sociallib.messages.ReadableResponse;
import com.expertiseandroid.lib.sociallib.model.Post;
import com.expertiseandroid.lib.sociallib.model.digg.DiggPost;
import com.expertiseandroid.lib.sociallib.model.digg.DiggUser;
import com.expertiseandroid.lib.sociallib.readers.DiggReader;
import com.expertiseandroid.lib.sociallib.utils.Utils;

/**
 * A connector to Digg, to read, digg, and comment on stories
 * @author Expertise Android
 *
 */
public class DiggConnector implements SignedCustomRequestSocialNetwork{
  
  private static final String API_CALL = "http://services.digg.com/2.0/";
  private static final String STORY_DIGG = "story.digg?story_id=";
  private static final String COMMENT_TEXT = "&comment_text=";
  private static final String COMMENT_POST = "comment.post?story_id=";
  private static final String STORY_GET_TOP_NEWS = "story.getTopNews?limit=";
  private static final String USER_GET_MY_NEWS = "user.getMyNews?limit=";
  private static final String USER_GET_INFO = "user.getInfo";
  private static final String DIGG_REQUEST = "http://services.digg.com/oauth/request_token";
  private static final String DIGG_ACCESS = "http://services.digg.com/oauth/access_token";
  private static final String DIGG_AUTHORIZE = "http://new.digg.com/oauth/authorize";
  
  private static final String NETWORK = "Digg";
  private static final String GET = "GET";
  
  public DiggReader reader;
  private CommonsHttpOAuthProvider httpOauthprovider;
  private CommonsHttpOAuthConsumer httpOauthConsumer;
  private String callback;
  private Token accessToken;
  public String userName;
  private boolean authentified;
  
  protected DiggConnector(String consumerKey, String consumerSecret, String callback){
    this.authentified = false;
    this.reader = new DiggReader();
    this.callback = callback;
    httpOauthConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    httpOauthprovider = new CommonsHttpOAuthProvider(DIGG_REQUEST, DIGG_ACCESS, DIGG_AUTHORIZE);
  }

  public DiggUser getUser() throws ClientProtocolException, IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, NotAuthentifiedException, JSONException {
    ReadableResponse response = signedCustomRequest(GET, API_CALL + USER_GET_INFO);
    return reader.readSingleUser(response);
  }

  public boolean authentify(Token accessToken) {
    this.accessToken = accessToken;
    httpOauthConsumer.setTokenWithSecret(accessToken.getToken(), accessToken.getSecret());
    this.authentified = true;
    return true;
  }

  public void authorize(Activity ctx) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
    String verifier = ctx.getIntent().getData().getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
    httpOauthprovider.retrieveAccessToken(httpOauthConsumer, verifier);
    accessToken = new Token(httpOauthConsumer.getToken(), httpOauthConsumer.getTokenSecret());
    authentified = true;
  }

  public boolean isAuthentified() {
    return authentified;
  }

  public boolean logout(Context ctx) {
    return false;
  }

  public void requestAuthorization(Context ctx) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException {
    String authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer, callback);
    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));  
  }

  public boolean comment(Post post, Post comment) throws FileNotFoundException, MalformedURLException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException, NotAuthentifiedException, JSONException {
    return comment(post, comment.getContents());
  }

  public List<DiggPost> getWallPosts() throws FileNotFoundException, MalformedURLException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, JSONException, IOException, NotAuthentifiedException {
    return getMyNews(20);
  }
  
  /**
   * Gets a user's news (like on the MyNews tab on digg)
   * @param limit number of news to retrieve
   * @return the user's news
   * @throws JSONException
   * @throws FileNotFoundException
   * @throws MalformedURLException
   * @throws OAuthMessageSignerException
   * @throws OAuthExpectationFailedException
   * @throws OAuthCommunicationException
   * @throws IOException
   * @throws NotAuthentifiedException
   */
  public List<DiggPost> getMyNews(int limit) throws JSONException, FileNotFoundException, MalformedURLException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException, NotAuthentifiedException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = signedCustomRequest(GET, API_CALL + USER_GET_MY_NEWS + limit);
    return reader.readPosts(response);
  }
  
  /**
   * Gets Digg's top news
   * @param limit number of news to retrieve
   * @return the top news
   * @throws JSONException
   * @throws FileNotFoundException
   * @throws MalformedURLException
   * @throws OAuthMessageSignerException
   * @throws OAuthExpectationFailedException
   * @throws OAuthCommunicationException
   * @throws IOException
   * @throws NotAuthentifiedException
   */
  public List<DiggPost> getTopNews(int limit) throws JSONException, FileNotFoundException, MalformedURLException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException, NotAuthentifiedException{
    ReadableResponse response = customRequest(GET, API_CALL + STORY_GET_TOP_NEWS + limit);
    return reader.readPosts(response);
  }

  public Token getAccessToken() {
    accessToken = new Token(httpOauthConsumer.getToken(), httpOauthConsumer.getTokenSecret());
    return accessToken;
  }

  public boolean comment(Post post, String comment) throws FileNotFoundException, MalformedURLException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException, NotAuthentifiedException, JSONException  {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    String storyId = post.getId();
    ReadableResponse response = signedCustomRequest(GET, API_CALL + COMMENT_POST + storyId + COMMENT_TEXT + comment);  
    return reader.readResponse(response);
  }
  
  /**
   * Diggs a story
   * @param storyId the story's id
   * @return true if the operation was successful
   * @throws FileNotFoundException
   * @throws MalformedURLException
   * @throws OAuthMessageSignerException
   * @throws OAuthExpectationFailedException
   * @throws OAuthCommunicationException
   * @throws IOException
   * @throws NotAuthentifiedException
   * @throws JSONException
   */
  public boolean digg(String storyId) throws FileNotFoundException, MalformedURLException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException, NotAuthentifiedException, JSONException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = signedCustomRequest(GET, API_CALL + STORY_DIGG + storyId);
    
    return reader.readResponse(response);
  }

  public ReadableResponse signedCustomRequest(String httpMethod,
      String request) throws FileNotFoundException,
      MalformedURLException, IOException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
    
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    return Utils.signedRequest(httpMethod, request, httpOauthConsumer, null);
  }

  public ReadableResponse signedCustomRequest(String httpMethod,
      String request, Map<String, String> bodyParams)
      throws FileNotFoundException, MalformedURLException, IOException,
      NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    return Utils.signedRequest(httpMethod, request, httpOauthConsumer, null);
  }

  public ReadableResponse customRequest(String httpMethod, String request) throws ClientProtocolException, IOException{
    return Utils.unsignedGetRequest(request);
  }

  public ReadableResponse customRequest(String httpMethod, String request,
      Map<String, String> bodyParams) throws FileNotFoundException,
      MalformedURLException, IOException {
    return customRequest(httpMethod, request);
  }

}