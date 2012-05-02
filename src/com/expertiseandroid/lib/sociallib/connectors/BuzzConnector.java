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

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.scribe.http.Request;
import org.scribe.oauth.Token;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.expertiseandroid.lib.sociallib.connectors.interfaces.CommentedPostsSocialNetwork;
import com.expertiseandroid.lib.sociallib.connectors.interfaces.FollowersSocialNetwork;
import com.expertiseandroid.lib.sociallib.connectors.interfaces.LikedPostsSocialNetwork;
import com.expertiseandroid.lib.sociallib.connectors.interfaces.SignedCustomRequestSocialNetwork;
import com.expertiseandroid.lib.sociallib.exceptions.NotAuthentifiedException;
import com.expertiseandroid.lib.sociallib.messages.ReadableResponse;
import com.expertiseandroid.lib.sociallib.messages.ScribeResponseWrapper;
import com.expertiseandroid.lib.sociallib.model.Post;
import com.expertiseandroid.lib.sociallib.model.buzz.BuzzActivity;
import com.expertiseandroid.lib.sociallib.model.buzz.BuzzUser;
import com.expertiseandroid.lib.sociallib.readers.BuzzReader;
import com.expertiseandroid.lib.sociallib.utils.Utils;

/**
 * A connector to Google Buzz that provides methods to post and retrieve data
 * @author Expertise Android
 *
 */
public class BuzzConnector implements FollowersSocialNetwork, CommentedPostsSocialNetwork, LikedPostsSocialNetwork, SignedCustomRequestSocialNetwork {

  //Constants
  private static final String XML_TYPE = "application/atom+xml";
  private static final String COMMENTS = "/@comments?alt=json";
  private static final String POST_COMMENT = "https://www.googleapis.com/buzz/v1/activities/@me/@self/";
  private static final String POST2 = "POST";
  private static final String PUT = "PUT";
  private static final String XML_ENTITY2 = "</content></entry>";
  private static final String XML_ENTITY1 = "<entry xmlns=\"http://www.w3.org/2005/Atom\"><content>";
  private static final String PUT_LIKE = "https://www.googleapis.com/buzz/v1/activities/@me/@liked/";
  private static final String JSON_TYPE = "application/json";
  private static final String JSON_ENTITY2 = "\" } } }";
  private static final String JSON_ENTITY1 = "{ \"data\": { \"object\": { \"type\": \"note\", \"content\": \"";
  private static final String POST_ACTIVITY = "https://www.googleapis.com/buzz/v1/activities/@me/@self?alt=json";
  private static final String GET_WALLPOSTS = "https://www.googleapis.com/buzz/v1/activities/@me/@consumption?alt=json";
  private static final String GET_USER1 = "https://www.googleapis.com/buzz/v1/people/";
  private static final String GET_USER2 = "/@self?alt=json";
  private static final String ME = "@me";
  private static final String GET_FOLLOWERS = "https://www.googleapis.com/buzz/v1/people/@me/@groups/@followers?alt=json";
  private static final String GET_FOLLOWED = "https://www.googleapis.com/buzz/v1/people/@me/@groups/@following?alt=json";
  private static final String SCOPE = "https://www.googleapis.com/auth/buzz";
  private static final String BUZZ_REQUEST = "https://www.google.com/accounts/OAuthGetRequestToken?scope=";
  private static final String BUZZ_ACCESS = "https://www.google.com/accounts/OAuthGetAccessToken";
  private static final String BUZZ_AUTHORIZE = "https://www.google.com/accounts/OAuthAuthorizeToken?hd=default";
  private static final String NETWORK = "Google Buzz";

  //Attributes
  public BuzzReader reader;
  private CommonsHttpOAuthProvider httpOauthprovider;
  private CommonsHttpOAuthConsumer httpOauthConsumer;
  private Token accessToken;
  private String callback;
  private boolean authentified;

  //Constructors
  protected BuzzConnector(String consumerKey, String consumerSecret, String callback){
    this.authentified = false;
    this.reader = new BuzzReader();
    this.callback = callback;
    httpOauthConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    httpOauthprovider = new CommonsHttpOAuthProvider(BUZZ_REQUEST + SCOPE, BUZZ_ACCESS, BUZZ_AUTHORIZE);


  }

  public List<BuzzUser> getFollowed() throws ClientProtocolException,
  IOException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, JSONException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = Utils.signedGetRequest(GET_FOLLOWED, httpOauthConsumer, null);
    return reader.readUsers(response);
  }

  public List<BuzzUser> getFollowers() throws ClientProtocolException,
  IOException, IllegalStateException, SAXException,
  ParserConfigurationException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, NotAuthentifiedException, JSONException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = Utils.signedGetRequest(GET_FOLLOWERS, httpOauthConsumer, null);
    return reader.readUsers(response);
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

  public BuzzUser getUser() throws ClientProtocolException, IOException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, NotAuthentifiedException, JSONException {
    return getUser(ME);
  }
  
  public BuzzUser getUser(String identifier) throws JSONException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException, NotAuthentifiedException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = Utils.signedGetRequest(GET_USER1 + identifier + GET_USER2, httpOauthConsumer, null);
    return reader.readUser(response);
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

  public List<BuzzActivity> getWallPosts() throws NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException, JSONException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = Utils.signedGetRequest(GET_WALLPOSTS, httpOauthConsumer, null);
    return reader.readActivities(response);
  }

  public boolean post(String content) throws NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException, JSONException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);

    ReadableResponse response = Utils.signedPostRequest(POST_ACTIVITY, JSON_ENTITY1 + content + JSON_ENTITY2, httpOauthConsumer, JSON_TYPE);
    return reader.readResponse(response);

  }

  public boolean post(Post content) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, NotAuthentifiedException, IOException, JSONException {
    return post(content.getContents());
  }

  public boolean like(Post post) throws NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException, JSONException {
    String id = post.getId();
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    ReadableResponse response = Utils.signedRequest(PUT, PUT_LIKE + id, httpOauthConsumer, JSON_TYPE, new ArrayList<NameValuePair>());
    return reader.readResponse(response);
  }

  public Token getAccessToken() {
    accessToken = new Token(httpOauthConsumer.getToken(), httpOauthConsumer.getTokenSecret());
    return accessToken;
  }

  public boolean comment(Post post, String comment) throws FileNotFoundException, MalformedURLException, IOException, NotAuthentifiedException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, JSONException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    String entity = XML_ENTITY1 + comment + XML_ENTITY2;
    ReadableResponse response = Utils.signedRequest(POST2, POST_COMMENT + post.getId() + COMMENTS, httpOauthConsumer, XML_TYPE, entity);
    return reader.readResponse(response);
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
    return new ScribeResponseWrapper(requestObj.send());
  }

}