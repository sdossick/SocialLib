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
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.scribe.oauth.Token;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.expertiseandroid.lib.sociallib.connectors.interfaces.CommentedPostsSocialNetwork;
import com.expertiseandroid.lib.sociallib.connectors.interfaces.FriendsSocialNetwork;
import com.expertiseandroid.lib.sociallib.connectors.interfaces.LikedPostsSocialNetwork;
import com.expertiseandroid.lib.sociallib.connectors.interfaces.PhotoSocialNetwork;
import com.expertiseandroid.lib.sociallib.connectors.interfaces.SignedCustomRequestSocialNetwork;
import com.expertiseandroid.lib.sociallib.exceptions.NotAuthentifiedException;
import com.expertiseandroid.lib.sociallib.messages.ReadableResponse;
import com.expertiseandroid.lib.sociallib.messages.StringWrapper;
import com.expertiseandroid.lib.sociallib.model.Post;
import com.expertiseandroid.lib.sociallib.model.facebook.FacebookPost;
import com.expertiseandroid.lib.sociallib.model.facebook.FacebookUser;
import com.expertiseandroid.lib.sociallib.readers.FacebookReader;
import com.expertiseandroid.lib.sociallib.utils.Utils;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

/**
 * A connector to Facebook that wraps the Facebook SDK with more explicit
 * methods, and providing additional features like photo uploading.
 * 
 * @author ExpertiseAndroid
 * 
 */
public class FacebookConnector implements FriendsSocialNetwork,
    CommentedPostsSocialNetwork, LikedPostsSocialNetwork,
    SignedCustomRequestSocialNetwork, PhotoSocialNetwork {

  private static final String ACCESS_TOKEN = "access_token";
  private static final String ENCODING = "UTF-8";
  private static final String MESSAGE = "message";
  private static final String POST = "POST";
  private static final String CONNECTOR = "FacebookConnector";
  private static final String NETWORK = "Facebook";
  private static final String STREAM_PUBLISH = "stream.publish";
  private static final String GRAPH_URL = "https://graph.facebook.com/";
  private static final String ME = "me";
  private static final String GET_FRIENDS = "/friends";
  private static final String GET_FEED = "/feed";
  private static final String GET_HOME = "/home";
  private static final String GET_PHOTOS = "/photos";
  private static final String GET_LIKES = "/likes";
  private static final String GET_COMMENTS = "/comments";

  public FacebookReader reader;
  private String appId;
  public String[] permissions;
  private Facebook facebook;
  public Facebook.DialogListener fbListener;

  protected FacebookConnector(String appId, String[] permissions) {
    this.facebook = new Facebook();
    this.appId = appId;
    this.permissions = permissions;
    this.reader = new FacebookReader();
    this.fbListener = new EmptyListener();
  }

  protected FacebookConnector(String appId, String[] permissions,
      Facebook.DialogListener fbListener) {
    this.facebook = new Facebook();
    this.appId = appId;
    this.permissions = permissions;
    this.reader = new FacebookReader();
    this.fbListener = fbListener;
  }

  public List<FacebookUser> getFriends() throws MalformedURLException,
      IOException, JSONException, NotAuthentifiedException {
    if (!isAuthentified())
      throw new NotAuthentifiedException(NETWORK);
    StringWrapper response = new StringWrapper(facebook.request(ME + GET_FRIENDS));
    return reader.readUsers(response);
  }

  public boolean authentify(Token accessToken) {
    facebook.setAccessToken(accessToken.getToken());
    return facebook.isSessionValid();
  }

  public void authorize(Activity ctx) {
    Log.w(CONNECTOR,"FacebookConnector doesn't need the authorize step to be explicitly called");
  }

  public FacebookUser getUser() throws ClientProtocolException, IOException,
      JSONException, NotAuthentifiedException {
    if (!isAuthentified())
      throw new NotAuthentifiedException(NETWORK);
    return getUser(ME);
  }

  /**
   * Gets any user on facebook
   * 
   * @param identifier the id or screenname of the target user
   * @return a FacebookUser representing the target user
   * @throws MalformedURLException
   * @throws IOException
   * @throws JSONException
   */
  public FacebookUser getUser(String identifier)
      throws MalformedURLException, IOException, JSONException {
    StringWrapper response = new StringWrapper(facebook.request(identifier));
    return reader.readUser(response);
  }

  public boolean isAuthentified() {
    return facebook.isSessionValid();
  }

  public boolean logout(Context ctx) throws MalformedURLException,
      IOException, JSONException {
    return reader.readResponse(new StringWrapper(facebook.logout(ctx)));
  }

  public void requestAuthorization(Context ctx) {
    facebook.authorize(ctx, appId, permissions, fbListener);
  }

  public boolean comment(Post post, Post comment)
      throws FileNotFoundException, MalformedURLException, IOException,
      NotAuthentifiedException, JSONException {
    return comment(post, comment.getContents());
  }

  public boolean comment(Post post, String comment)
      throws FileNotFoundException, MalformedURLException, IOException,
      NotAuthentifiedException, JSONException {
    if (!isAuthentified())
      throw new NotAuthentifiedException(NETWORK);
    String id = post.getId();
    Bundle params = new Bundle();
    params.putString(MESSAGE, comment);
    StringWrapper response = new StringWrapper(facebook.request(id + GET_COMMENTS, params, POST));
    return reader.readResponse(response);
  }

  public List<FacebookPost> getWallPosts() throws MalformedURLException,
      IOException, JSONException, NotAuthentifiedException {
    if (!isAuthentified())
      throw new NotAuthentifiedException(NETWORK);
    StringWrapper response = new StringWrapper(facebook.request(ME + GET_FEED));
    return reader.readPosts(response);
  }

  /**
   * Gets the latest posts of the user's home
   * @return a list of FacebookPosts
   * @throws MalformedURLException
   * @throws IOException
   * @throws JSONException
   * @throws NotAuthentifiedException
   */
  public List<FacebookPost> getHomePosts() throws MalformedURLException,
      IOException, JSONException, NotAuthentifiedException {
    if (!isAuthentified())
      throw new NotAuthentifiedException(NETWORK);
    StringWrapper response = new StringWrapper(facebook.request(ME + GET_HOME));
    return reader.readPosts(response);
  }

  /**
   * Gets the latest posts of any user's wall
   * @param user the target user
   * @return a list of FacebookPosts
   * @throws MalformedURLException
   * @throws IOException
   * @throws JSONException
   */
  public List<FacebookPost> getWallPosts(FacebookUser user)
      throws MalformedURLException, IOException, JSONException {
    StringWrapper response = new StringWrapper(facebook.request(String.valueOf(user.id) + GET_FEED));
    return reader.readPosts(response);
  }

  public boolean post(Post content) throws FileNotFoundException,
      MalformedURLException, IOException, NotAuthentifiedException, JSONException {
    if (!isAuthentified())
      throw new NotAuthentifiedException(NETWORK);
    Bundle params = new Bundle();
    params.putString(MESSAGE, content.getContents());
    StringWrapper response = new StringWrapper(facebook.request(ME + GET_FEED, params, POST));
    return reader.readResponse(response);
  }

  /**
   * Submit a post on any user's wall
   * @param content the item to post
   * @param user the target user
   * @return true if the operation was successful
   * @throws FileNotFoundException
   * @throws MalformedURLException
   * @throws IOException
   * @throws NotAuthentifiedException
   * @throws JSONException 
   */
  public boolean post(Post content, FacebookUser user)
      throws FileNotFoundException, MalformedURLException, IOException,
      NotAuthentifiedException, JSONException {
    if (!isAuthentified())
      throw new NotAuthentifiedException(NETWORK);
    Bundle params = new Bundle();
    params.putString(MESSAGE, content.getContents());
    StringWrapper response = new StringWrapper(facebook.request(String.valueOf(user.id) + GET_FEED, params, POST));
    return reader.readResponse(response);
  }

  public boolean like(Post post) throws FileNotFoundException,
      MalformedURLException, IOException, NotAuthentifiedException, JSONException {
    if (!isAuthentified())
      throw new NotAuthentifiedException(NETWORK);
    String id = post.getId();
    StringWrapper response = new StringWrapper(facebook.request(id + GET_LIKES, new Bundle(), POST));
    return reader.readResponse(response);
  }

  /**
   * Displays a dialog for the request action in the given context
   * @param ctx the context in which the dialog will be generated
   * @param action String representation of the desired method
   */
  public void publishDialog(Context ctx, String action) {
    facebook.dialog(ctx, action, fbListener);
  }
  
  /**
   * Displays a dialog for the request action in the given context
   * @see http://developers.facebook.com/docs/reference/rest/stream.publish
   * @param ctx ctx the context in which the dialog will be generated
   * @param action String representation of the desired method
   * @param parameters actions parameters
   */
  public void publishDialog(Context ctx, String action, Bundle parameters){
    facebook.dialog(ctx, action, parameters, fbListener);
  }

  /**
   * Displays a dialog to publish something on the user's wall
   * @param ctx the context in which the dialog will be generated
   */
  public void streamPublish(Context ctx) {
    publishDialog(ctx, STREAM_PUBLISH);
  }

  public Token getAccessToken() {
    return new Token(facebook.getAccessToken(), "");
  }

  public ReadableResponse signedCustomRequest(String httpMethod,
      String request) throws FileNotFoundException,
      MalformedURLException, IOException, NotAuthentifiedException {
    return signedCustomRequest(httpMethod, request,
        new HashMap<String, String>());
  }

  public ReadableResponse signedCustomRequest(String httpMethod,
      String request, Map<String, String> bodyParams)
      throws FileNotFoundException, MalformedURLException, IOException,
      NotAuthentifiedException {
    if (!isAuthentified())
      throw new NotAuthentifiedException(NETWORK);
    return new StringWrapper(facebook.request(request, Utils.stringMapToBundle(bodyParams), httpMethod));
  }

  public ReadableResponse customRequest(String httpMethod, String request) throws FileNotFoundException, MalformedURLException, IOException {
    return customRequest(httpMethod, request, new HashMap<String, String>());
  }

  public ReadableResponse customRequest(String httpMethod, String request,
      Map<String, String> bodyParams) throws FileNotFoundException, MalformedURLException, IOException {
    return new StringWrapper(facebook.request(request, Utils.stringMapToBundle(bodyParams), httpMethod));
  }

  public boolean sendPhoto(String filePath, String caption)
      throws IOException, JSONException {
    return sendPhoto(filePath, caption, ME);
  }

  /**
   * The albumId could be any facebook identifier that supports a photos
   * connection
   * @throws JSONException 
   * 
   * @see Graph API
   */
  public boolean sendPhoto(String filePath, String caption, String albumId)
      throws IOException, JSONException {
    Bitmap image = BitmapFactory.decodeFile(filePath);

    Map<String, String> params = new HashMap<String, String>();
    params.put(ACCESS_TOKEN, URLDecoder.decode(facebook.getAccessToken(),
        ENCODING));
    params.put(MESSAGE, caption);

    String url = GRAPH_URL + albumId + GET_PHOTOS;
    ReadableResponse response = new StringWrapper(Utils.postWithAttachment(
        url, params, image));
    return reader.readResponse(response);
  }
  
  /**
   * A Facebook Dialog Listener that prints debug messages
   * @author ExpertiseAndroid
   *
   */
  private class EmptyListener implements Facebook.DialogListener {
    public void onCancel() {
      Log.d("EmptyListener", "Facebook Cancel");
    }
    
    public void onComplete(Bundle values) {
      Log.d("EmptyListener", "Facebook Complete");
    }
    
    public void onError(DialogError e) {
      Log.d("EmptyListener", "Error:".concat(e.getMessage()));
    }
    
    public void onFacebookError(FacebookError e) {
      Log.d("EmptyListener", "Facebook Error:".concat(e.getMessage()));
    }
  }
}