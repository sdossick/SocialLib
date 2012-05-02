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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.scribe.http.Request;
import org.scribe.http.Request.Verb;
import org.scribe.oauth.Scribe;
import org.scribe.oauth.Token;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.expertiseandroid.lib.sociallib.connectors.interfaces.CommentedPostsSocialNetwork;
import com.expertiseandroid.lib.sociallib.connectors.interfaces.FriendsSocialNetwork;
import com.expertiseandroid.lib.sociallib.connectors.interfaces.PostsSocialNetwork;
import com.expertiseandroid.lib.sociallib.connectors.interfaces.SignedCustomRequestSocialNetwork;
import com.expertiseandroid.lib.sociallib.exceptions.NotAuthentifiedException;
import com.expertiseandroid.lib.sociallib.messages.ReadableResponse;
import com.expertiseandroid.lib.sociallib.messages.ScribeResponseWrapper;
import com.expertiseandroid.lib.sociallib.model.Post;
import com.expertiseandroid.lib.sociallib.model.linkedin.LinkedInConnectionPost;
import com.expertiseandroid.lib.sociallib.model.linkedin.LinkedInPost;
import com.expertiseandroid.lib.sociallib.model.linkedin.LinkedInUser;
import com.expertiseandroid.lib.sociallib.readers.LinkedInReader;
import com.expertiseandroid.lib.sociallib.utils.ScribeFactory;
import com.expertiseandroid.lib.sociallib.utils.Utils;

/**
 * A connector to LinkedIn that provides methods to retrieve data and post content
 * @author ExpertiseAndroid
 *
 */

public class LinkedInConnector implements FriendsSocialNetwork, PostsSocialNetwork, CommentedPostsSocialNetwork, SignedCustomRequestSocialNetwork {

  private static final String POST_COMMENT = "http://api.linkedin.com/v1/people/~/network/updates/key=";
  private static final String GET_WALLPOSTS = "http://api.linkedin.com/v1/people/~/network/updates";
  private static final String GET_CONN_UPDATES = "http://api.linkedin.com/v1/people/~/network/updates?type=CONN&count=";
  private static final String GET_STATUS_UPDATES = "http://api.linkedin.com/v1/people/~/network/updates?type=STAT&count=";
  private static final String AUTHORIZE = "https://api.linkedin.com/uas/oauth/authorize?oauth_token=";
  private static final String API_PATH = "https://api.linkedin.com/v1";
  private static final String INVALIDATE = "https://api.linkedin.com/uas/oauth/invalidateToken";
  private static final String CURRENT_STATUS = "current-status";
  private static final String OAUTH_VERIFIER = "oauth_verifier";
  private static final String PEOPLE = "/people";
  private static final String STATUS = "/current-status";
  private static final String COMMENT2 = "comment";
  private static final String UPDATE_COMMENT = "update-comment";
  private static final String UPDATE_COMMENTS = "/update-comments";
  private static final String CONNECTIONS_FIELD = ":(connections)";
  private static final String ME = "/~";
  private static final String ID = "/id=";
  private static final String DEF_FIELDS = ":(id,first-name,last-name,industry,headline,distance,current-status,num-connections,summary)";
  private static final String NETWORK = "LinkedIn";
  
  public LinkedInReader reader;
  private Scribe scribe;
  private Token requestToken;
  public Token accessToken;
  private boolean authentified;
  
  protected LinkedInConnector(String consumerKey, String consumerSecret, String callback){
    this.authentified = false;
    this.scribe = ScribeFactory.getLinkedInScribe(consumerKey, consumerSecret, callback);
    this.reader = new LinkedInReader();
    
  }

  public List<LinkedInUser> getFriends() throws SAXException, ParserConfigurationException, IOException, NotAuthentifiedException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    Request request = new Request(Verb.GET, API_PATH + PEOPLE + ME + CONNECTIONS_FIELD);
    scribe.signRequest(request, accessToken);
    ReadableResponse response = new ScribeResponseWrapper(request.send());  
    return reader.readUsers(response);
  }

  public boolean authentify(Token accessToken) {
    this.accessToken = accessToken;
    return true;
  }

  public void authorize(Activity ctx) {
    Map<String, String> params = Utils.decodeUri(ctx.getIntent().getData().toString());
    String verifier = params.get(OAUTH_VERIFIER);
    accessToken = scribe.getAccessToken(requestToken, verifier);
    authentified = true;
  }

  public LinkedInUser getUser() throws ClientProtocolException, IOException, SAXException, ParserConfigurationException, NotAuthentifiedException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    Request request = new Request(Verb.GET, API_PATH + PEOPLE + ME + DEF_FIELDS);
    scribe.signRequest(request, accessToken);
    ReadableResponse response = new ScribeResponseWrapper(request.send());  
    return reader.readUser(response);
  }
  
  /**
   * Retrieves information about a specific user
   * @param identifier the id of the target user
   * @return
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws NotAuthentifiedException
   */
  public LinkedInUser getUser(String identifier) throws SAXException, ParserConfigurationException, IOException, NotAuthentifiedException{
    Request request = new Request(Verb.GET, API_PATH + PEOPLE + ID + identifier + DEF_FIELDS);
    scribe.signRequest(request, accessToken);
    ReadableResponse response = new ScribeResponseWrapper(request.send());  
    return reader.readUser(response);
  }

  public boolean isAuthentified() {
    return authentified;
  }

  public boolean logout(Context ctx) throws SAXException, ParserConfigurationException, IOException {
    Request request = new Request(Verb.GET, INVALIDATE);
    scribe.signRequest(request, accessToken);
    ReadableResponse response = new ScribeResponseWrapper(request.send());
    return reader.readResponse(response);
  }

  public void requestAuthorization(Context ctx) {
    requestToken = scribe.getRequestToken();
    String url = AUTHORIZE + requestToken.getToken();
    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
  }
  
  public List<LinkedInPost> getStatusUpdates(int start, int count) throws NotAuthentifiedException, SAXException, ParserConfigurationException, IOException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    Request request = new Request(Verb.GET, GET_STATUS_UPDATES + count + "&start=" + start);
    scribe.signRequest(request, accessToken);
    ReadableResponse response = new ScribeResponseWrapper(request.send());  
    return reader.readPosts(response);
  }
  
  public List<LinkedInConnectionPost> getConnectionsUpdates(int start, int count) throws NotAuthentifiedException, SAXException, ParserConfigurationException, IOException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    Request request = new Request(Verb.GET, GET_CONN_UPDATES + count + "&start=" + start);
    scribe.signRequest(request, accessToken);
    ReadableResponse response = new ScribeResponseWrapper(request.send());  
    return reader.readConnectionPosts(response);
  }

  /**
   * Currently, only status updates and new connections are shown
   * @throws NotAuthentifiedException 
   * @throws IOException 
   * @throws ParserConfigurationException 
   * @throws SAXException 
   * @see com.expertiseandroid.lib.sociallib.connectors.interfaces.PostsSocialNetwork
   */
  public List<LinkedInPost> getWallPosts() throws NotAuthentifiedException, SAXException, ParserConfigurationException, IOException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    Request request = new Request(Verb.GET, GET_WALLPOSTS);
    scribe.signRequest(request, accessToken);
    ReadableResponse response = new ScribeResponseWrapper(request.send());
    return reader.readPosts(response);
  }

  public boolean post(Post content) throws NotAuthentifiedException, SAXException, ParserConfigurationException, IOException {
    return post(content.getContents());
  }

  /**
   * Posts a status
   * @param status
   * @return true if the operation was successful
   * @throws NotAuthentifiedException
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */
  public boolean post(String status) throws NotAuthentifiedException, SAXException, ParserConfigurationException, IOException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    Request request = new Request(Verb.PUT, API_PATH + PEOPLE + ME + STATUS);
    scribe.signRequest(request, accessToken);
    Map<String,String> params = new HashMap<String, String>();
    params.put(CURRENT_STATUS, status);
    request.addPayload(Utils.generateXML(params));
    ReadableResponse response = new ScribeResponseWrapper(request.send());
    return reader.readResponse(response);
  }

  public boolean comment(Post post, Post comment)
      throws FileNotFoundException, MalformedURLException, IOException, NotAuthentifiedException, SAXException, ParserConfigurationException {
    return comment(post, comment.getContents());
  }
  

  public boolean comment(Post post, String comment) throws NotAuthentifiedException, SAXException, ParserConfigurationException, IOException{
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    Request request = new Request(Verb.POST, POST_COMMENT + post.getId() + UPDATE_COMMENTS);
    scribe.signRequest(request, accessToken);
    Map<String,String> params = new HashMap<String, String>();
    params.put(COMMENT2, comment);
    request.addPayload(Utils.generateXML(params, UPDATE_COMMENT));
    ReadableResponse response = new ScribeResponseWrapper(request.send());
    return reader.readResponse(response);
  }

  public Token getAccessToken() {
    return accessToken;
  }

  public ReadableResponse signedCustomRequest(String httpMethod, String request) throws FileNotFoundException,
      MalformedURLException, IOException, NotAuthentifiedException {
    return signedCustomRequest(httpMethod, request, new HashMap<String,String>());
  }

  public ReadableResponse signedCustomRequest(String httpMethod, String request, Map<String, String> bodyParams)
      throws FileNotFoundException, MalformedURLException, IOException, NotAuthentifiedException {
    if(!isAuthentified()) throw new NotAuthentifiedException(NETWORK);
    Request rObj = new Request(Utils.getScribeVerb(httpMethod), request);
    Utils.addBodyParams(rObj, bodyParams);
    scribe.signRequest(rObj, accessToken);
    return new ScribeResponseWrapper(rObj.send());
  }

  public ReadableResponse customRequest(String httpMethod, String request) {
    return customRequest(httpMethod, request, new HashMap<String, String>());
  }

  public ReadableResponse customRequest(String httpMethod, String request,
      Map<String, String> bodyParams) {
    Request rObj = new Request(Utils.getScribeVerb(httpMethod), request);
    Utils.addBodyParams(rObj, bodyParams);
    return new ScribeResponseWrapper(rObj.send());
  }


}