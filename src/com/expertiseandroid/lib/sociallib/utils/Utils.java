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

package com.expertiseandroid.lib.sociallib.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.scribe.http.Request;
import org.scribe.http.Request.Verb;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.expertiseandroid.lib.sociallib.messages.HttpResponseWrapper;
import com.expertiseandroid.lib.sociallib.messages.ReadableResponse;
import com.expertiseandroid.lib.sociallib.parser.DynamicJSONParser;
import com.expertiseandroid.lib.sociallib.parser.DynamicXmlParser;
import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

/**
 * Utility class providing methods to simplify some tasks
 * @author Expertise Android
 *
 */
public class Utils {


  public static final String SOCIALLIB = "SocialLib";
  private static final String TWITTER_VERIFY_CREDENTIALS = "https://api.twitter.com/1/account/verify_credentials.json";
  private static final String X_AUTH_SERVICE_PROVIDER = "X-Auth-Service-Provider";
  private static final String OAUTH_ECHO_AUTH = "X-Verify-Credentials-Authorization";
  private static final String TWITPIC_UPLOAD = "http://api.twitpic.com/2/upload.xml";
  private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final String ENCODING = "UTF-8";
  private static final String PUT = "PUT";
  private static final String POST = "POST";
  private static final String GET = "GET";
  private static final String DELETE = "DELETE";
  private static SAXParserFactory spf = SAXParserFactory.newInstance();


  /**
   * Parses an XML response
   * @param response the response to parse
   * @param rules the parsing rules to be used
   * @return the list of objects built
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */
  public static Object parseXML(ReadableResponse response, ParsingRules rules) throws SAXException, ParserConfigurationException, IOException{
    XMLReader reader = createXMLReader(new DynamicXmlParser(rules));
    String xml = response.getContents();
    InputSource is = new InputSource(new StringReader(xml));
    reader.parse(is);
    return rules.getContents();
  }

  /**
   * Parses a JSON response
   * @param response the response to parse
   * @param rules the parsing rules to be used
   * @return the list of object built
   * @throws JSONException
   */
  public static Object parseJSON(ReadableResponse response, ParsingRules rules) throws JSONException{
    DynamicJSONParser parser = new DynamicJSONParser(rules);
    parser.parse(response.getContents());
    return rules.getContents();
  }

  private static XMLReader createXMLReader(DefaultHandler handler) throws SAXException, ParserConfigurationException{
    XMLReader reader = spf.newSAXParser().getXMLReader();
    reader.setContentHandler(handler);
    return reader;
  }

  /**
   * Generates a flat XML structure
   * @param params a map where the key is the tag, and the value its value
   * @return an XML string
   */
  public static String generateXML(Map<String,String> params){
    StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    for(Iterator<String> it = params.keySet().iterator(); it.hasNext();){
      String key = it.next();
      sb.append('<').append(key).append('>');
      sb.append(params.get(key));
      sb.append("</").append(key).append('>');
    }
    return sb.toString();
  }

  /**
   * Generates a flat XML structure with a root node
   * @param params a map where the key is the tag, and the value its value
   * @param rootNode the name of the root node
   * @return an XML string
   */
  public static String generateXML(Map<String,String> params, String rootNode){
    StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    sb.append('<').append(rootNode).append('>');
    for(Iterator<String> it = params.keySet().iterator(); it.hasNext();){
      String key = it.next();
      sb.append('<').append(key).append('>');
      sb.append(params.get(key));
      sb.append("</").append(key).append('>');
    }
    sb.append("</").append(rootNode).append('>');
    return sb.toString();
  }

  /**
   * Gets a Scribe Verb by its name
   * @param verb the HTTP verb
   * @return the Scribe Verb
   */
  public static Verb getScribeVerb(String verb){
    if(verb.equals("POST")) return Verb.POST;
    if(verb.equals("GET")) return Verb.GET;
    if(verb.equals("PUT")) return Verb.PUT;
    if(verb.equals("DELETE")) return Verb.DELETE;
    else return null;
  }

  /**
   * Transforms a map to a Bundle
   * @param map
   * @return a Bundle with the map's contents
   */
  public static Bundle stringMapToBundle(Map<String, String> map){
    Bundle b = new Bundle();
    for(Iterator<String> it = map.keySet().iterator(); it.hasNext();){
      String key = it.next();
      b.putString(key, map.get(key));
    }
    return b;
  }

  /**
   * Adds body parameters to a Scribe Request
   * @param request the Scribe Request
   * @param bodyParams a map of the body parameters
   */
  public static void addBodyParams(Request request, Map<String,String> bodyParams){
    for(Iterator<String> it = bodyParams.keySet().iterator(); it.hasNext();){
      String key = it.next();
      request.addBodyParameter(key, bodyParams.get(key));
    }
  }

  /**
   * Sends a POST request with an attached object
   * @param url the url that should be opened
   * @param params the body parameters
   * @param attachment the object to be attached
   * @return the response
   * @throws IOException
   */
  public static String postWithAttachment(String url, Map<String, String> params, Object attachment) throws IOException{
    String boundary = generateBoundaryString(10);
    URL servUrl = new URL(url);
    HttpURLConnection conn = (HttpURLConnection)servUrl.openConnection();
    conn.setRequestProperty("User-Agent", System.getProperties().getProperty("http.agent") + SOCIALLIB);
    conn.setRequestMethod("POST");
    String contentType = "multipart/form-data; boundary=" + boundary;
    conn.setRequestProperty("Content-Type", contentType);

    byte[] body = generatePostBody(params, attachment, boundary);

    conn.setDoOutput(true);
    conn.connect();
    OutputStream out = conn.getOutputStream();
    out.write(body);
    InputStream is = null;
    try {
      is = conn.getInputStream();
    } catch (FileNotFoundException e) {
      is = conn.getErrorStream();
    }catch (Exception e){
      int statusCode = conn.getResponseCode();
      Log.e("Response code", ""+statusCode);
      return conn.getResponseMessage();
    }

    BufferedReader r = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String l;
    while((l=r.readLine())!=null) sb.append(l).append('\n');
    out.close();
    is.close();
    if(conn!=null) conn.disconnect();
    return sb.toString();
  }

  /**
   * Posts a photo to twitpic
   * @param attachment the picture
   * @param consumer the Oauth consumer
   * @param tpKey the twitpic key
   * @param message a caption associated with the picture
   * @return the server's response
   * @throws OAuthMessageSignerException
   * @throws OAuthExpectationFailedException
   * @throws OAuthCommunicationException
   * @throws UnsupportedEncodingException
   * @throws IOException
   */
  public static ReadableResponse postTwitpic(Bitmap attachment, CommonsHttpOAuthConsumer consumer, String tpKey, String message) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, UnsupportedEncodingException, IOException{
    String boundary = generateBoundaryString(20);
    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(TWITPIC_UPLOAD);
    post.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
    post.addHeader(OAUTH_ECHO_AUTH, getVerifyCredentialsAuthString(consumer));
    post.addHeader(X_AUTH_SERVICE_PROVIDER, TWITTER_VERIFY_CREDENTIALS);
    post.addHeader("Content-Type", "multipart/form-data; boundary="+boundary);
    
    Map<String, String> params = new HashMap<String, String>();
    params.put("key", tpKey);
    params.put("message", message);
    
    byte[] body = generatePostBody2(params, attachment, boundary);
    post.setEntity(new ByteArrayEntity(body));
    
    return new HttpResponseWrapper(client.execute(post));
  }
  
  private static String getVerifyCredentialsAuthString(CommonsHttpOAuthConsumer consumer) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException{
    HttpGet post = new HttpGet(TWITTER_VERIFY_CREDENTIALS);
    consumer.sign(post);
    String[] parts = post.getHeaders("Authorization")[0].getValue().split(" ");
    StringBuilder sb = new StringBuilder();
    sb.append("OAuth realm=\"http://api.twitter.com/\", ");
    for(String part : parts){
      if(!(part.contains("OAuth"))){
        sb.append(part).append(' ');
      }
    }
    return sb.toString();
  }

  private static byte[] generatePostBody(Map<String, String> params, Object attachment, String boundary) throws UnsupportedEncodingException, IOException{

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    String bodyString = "--" + boundary + "\r\n";
    String endLine = "\r\n--" + boundary + "\r\n";

    os.write(bodyString.getBytes(ENCODING));

    for (Entry<String, String> entry : params.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();

      String cd = "Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n";

      os.write(cd.getBytes(ENCODING));
      os.write(value.getBytes(ENCODING));
      os.write(endLine.getBytes(ENCODING));
    }

    // write a bitmap value, if one exists
    if (attachment != null) {
      if (attachment instanceof Bitmap) {
        String cd = "Content-Disposition: form-data; filename=\"photo\"\r\n";
        String ct = "Content-Type: image/png\r\n\r\n";
        Bitmap image = (Bitmap) attachment;

        os.write(cd.getBytes(ENCODING));
        os.write(ct.getBytes(ENCODING));
        image.compress(Bitmap.CompressFormat.PNG, 0, os);
        os.write(endLine.getBytes(ENCODING));

      } else if (attachment instanceof byte[]) {
        String cd = "Content-Disposition: form-data; filename=\"data\"\r\n";
        String ct = "Content-Type: content/unknown\r\n\r\n";
        byte[] data = (byte[]) attachment;

        os.write(cd.getBytes(ENCODING));
        os.write(ct.getBytes(ENCODING));
        os.write(data);
        os.write(endLine.getBytes(ENCODING));
      }
    }

    return os.toByteArray();
  }

  private static byte[] generatePostBody2(Map<String, String> params, Object attachment, String boundary) throws UnsupportedEncodingException, IOException{

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    String bodyString = "--" + boundary + "\r\n";
    String endLine = "\r\n--" + boundary + "\r\n";

    os.write(bodyString.getBytes(ENCODING));

    for (Entry<String, String> entry : params.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();

      String cd = "Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n";

      os.write(cd.getBytes(ENCODING));
      os.write(value.getBytes(ENCODING));
      os.write(endLine.getBytes(ENCODING));
    }

    // write a bitmap value, if one exists
    if (attachment != null) {
      if (attachment instanceof Bitmap) {
        String cd = "Content-Disposition: form-data; name=\"media\"; filename=\"media\"\r\n";
        String ct = "Content-Type: image/png\r\n\r\n";
        Bitmap image = (Bitmap) attachment;

        os.write(cd.getBytes(ENCODING));
        os.write(ct.getBytes(ENCODING));
        image.compress(Bitmap.CompressFormat.PNG, 0, os);
        os.write(endLine.getBytes(ENCODING));

      } else if (attachment instanceof byte[]) {
        String cd = "Content-Disposition: form-data; filename=\"data\"\r\n";
        String ct = "Content-Type: content/unknown\r\n\r\n";
        byte[] data = (byte[]) attachment;

        os.write(cd.getBytes(ENCODING));
        os.write(ct.getBytes(ENCODING));
        os.write(data);
        os.write(endLine.getBytes(ENCODING));
      }
    }

    return os.toByteArray();
  }

  
  
  /**
   * Decodes a URI
   * @param s a string representation of the URI
   * @return a map of the URI parameters
   */
  public static Map<String, String> decodeUri(String s){
    String[] array = null;
    try{
      array = s.split("\\?");
    }catch(Exception e){
      Log.e("decodeUri", e.getMessage());
    }
    if(array.length==2) return decodeUrl(array[1]);
    else return null;
  }

  /**
   * Decodes a URL
   * @param s a string representation of the URL
   * @return a map of the URL parameters
   */
  public static Map<String, String> decodeUrl(String s) {
    Map<String, String> params = new HashMap<String, String>();
    if (s != null) {
      String array[] = s.split("&");
      for (String parameter : array) {
        String v[] = parameter.split("=");
        params.put(v[0], v[1]);
      }
    }
    return params;
  }

  /**
   * Sends a signed POST request using the Signpost library
   * @param request the request's URL
   * @param bodyParams the body parameters
   * @param consumer the Signpost consumer object
   * @param contentType the message's content type
   * @return the response
   * @throws OAuthMessageSignerException
   * @throws OAuthExpectationFailedException
   * @throws OAuthCommunicationException
   * @throws ClientProtocolException
   * @throws IOException
   */
  public static ReadableResponse signedPostRequest(String request, List<NameValuePair> bodyParams, CommonsHttpOAuthConsumer consumer, String contentType) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException{
    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(request);
    post.setEntity(new UrlEncodedFormEntity(bodyParams, HTTP.UTF_8));
    post.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
    consumer.sign(post);
    post.addHeader("Content-Type", contentType);
    HttpResponse response = client.execute(post);
    return new HttpResponseWrapper(response);
  }

  /**
   * Sends a signed POST request using the Signpost library
   * @param request the request's URL
   * @param entity the message's contents
   * @param consumer the Signpost consumer object
   * @param contentType the message's content type
   * @return the response
   * @throws OAuthMessageSignerException
   * @throws OAuthExpectationFailedException
   * @throws OAuthCommunicationException
   * @throws ClientProtocolException
   * @throws IOException
   */
  public static ReadableResponse signedPostRequest(String request, String entity, CommonsHttpOAuthConsumer consumer, String contentType) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException{
    HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost(request);
    post.setEntity(new StringEntity(entity));
    post.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
    consumer.sign(post);
    post.addHeader("Content-Type", contentType);
    HttpResponse response = client.execute(post);
    return new HttpResponseWrapper(response);
  }

  /**
   * Sends a signed PUT request using the Signpost library
   * @param request the request's URL
   * @param entity the message's contents
   * @param consumer the Signpost consumer object
   * @param contentType the message's content type
   * @return the response
   * @throws OAuthMessageSignerException
   * @throws OAuthExpectationFailedException
   * @throws OAuthCommunicationException
   * @throws ClientProtocolException
   * @throws IOException
   */
  public static ReadableResponse signedPutRequest(String request, String entity, CommonsHttpOAuthConsumer consumer, String contentType) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException{
    HttpClient client = new DefaultHttpClient();
    HttpPut put = new HttpPut(request);
    put.setEntity(new StringEntity(entity));
    put.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
    consumer.sign(put);
    put.addHeader("Content-Type", contentType);
    HttpResponse response = client.execute(put);
    return new HttpResponseWrapper(response);
  }

  /**
   * Sends a signed GET request using the Signpost library
   * @param request the request's URL
   * @param consumer the Signpost consumer object
   * @param contentType the message's content type
   * @return the response
   * @throws OAuthMessageSignerException
   * @throws OAuthExpectationFailedException
   * @throws OAuthCommunicationException
   * @throws ClientProtocolException
   * @throws IOException
   */
  public static ReadableResponse signedGetRequest(String request, CommonsHttpOAuthConsumer consumer, String contentType) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException{
    HttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet(request);
    consumer.sign(get);
    get.addHeader("Content-Type", contentType);
    HttpResponse response = client.execute(get);
    return new HttpResponseWrapper(response);
  }
  
  /**
   * Sends a GET request
   * @param request the endpoint's URL
   * @return the response
   * @throws ClientProtocolException
   * @throws IOException
   */
  public static ReadableResponse unsignedGetRequest(String request) throws ClientProtocolException, IOException{
    HttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet(request);
    HttpResponse response = client.execute(get);
    return new HttpResponseWrapper(response);
  }

  /**
   * Sends a signed HTTP request using the Signpost library
   * @param verb the HTTP verb to be used
   * @param request the request's URL
   * @param consumer the Signpost consumer object
   * @param contentType the message's content type
   * @return the response
   * @throws OAuthMessageSignerException
   * @throws OAuthExpectationFailedException
   * @throws OAuthCommunicationException
   * @throws ClientProtocolException
   * @throws IOException
   */
  public static ReadableResponse signedRequest(String verb, String request, CommonsHttpOAuthConsumer consumer, String contentType) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException{
    return signedRequest(verb, request, consumer, contentType, "");
  }

  /**
   * Sends a signed HTTP request using the Signpost library
   * @param verb the HTTP verb to be used
   * @param request the request's URL
   * @param consumer the Signpost consumer object
   * @param contentType contentType the message's content type
   * @param entity the message's contents
   * @return the response
   * @throws OAuthMessageSignerException
   * @throws OAuthExpectationFailedException
   * @throws OAuthCommunicationException
   * @throws ClientProtocolException
   * @throws IOException
   */
  public static ReadableResponse signedRequest(String verb, String request, CommonsHttpOAuthConsumer consumer, String contentType, String entity) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException{
    HttpClient client = new DefaultHttpClient();
    HttpRequestBase reqObject = getRequestBaseByVerbEntity(verb, request, entity);
    consumer.sign(reqObject);
    reqObject.addHeader("Content-Type", contentType);
    HttpResponse response = client.execute(reqObject);
    return new HttpResponseWrapper(response);
  }

  /**
   * Sends a signed HTTP request using the Signpost library
   * @param verb HTTP verb
   * @param request request URI
   * @param consumer the consumer used to sign the request
   * @param contentType the content-type of the request
   * @param bodyParams the body parameters of the request
   * @return a ReadableResponse to the request
   * @throws OAuthMessageSignerException
   * @throws OAuthExpectationFailedException
   * @throws OAuthCommunicationException
   * @throws ClientProtocolException
   * @throws IOException
   */
  public static ReadableResponse signedRequest(String verb, String request, CommonsHttpOAuthConsumer consumer, String contentType, List<NameValuePair> bodyParams) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException{
    HttpClient client = new DefaultHttpClient();
    HttpRequestBase reqObject = getRequestBaseByVerbParams(verb, request, bodyParams);
    consumer.sign(reqObject);
    reqObject.addHeader("Content-Type", contentType);
    HttpResponse response = client.execute(reqObject);
    return new HttpResponseWrapper(response);
  }

  private static HttpRequestBase getRequestBaseByVerbEntity(String verb, String request, String entity) throws UnsupportedEncodingException {
    if(verb.equals(POST)){HttpPost post = new HttpPost(request); post.setEntity(new StringEntity(entity)); return post;}
    else if(verb.equals(GET)) return new HttpGet(request);
    else if(verb.equals(PUT)) {HttpPut put = new HttpPut(request); put.setEntity(new StringEntity(entity)); return put;}
    else if(verb.equals(DELETE)) return new HttpDelete(request);
    else Log.e("Utils", "Cannot reckognize the verb " + verb);
    return null;
  }

  private static HttpRequestBase getRequestBaseByVerbParams(String verb, String request, List<NameValuePair> bodyParams) throws UnsupportedEncodingException {
    if(verb.equals(POST)){HttpPost post = new HttpPost(request); post.setEntity(new UrlEncodedFormEntity(bodyParams, HTTP.UTF_8)); setParams(post); return post;}
    else if(verb.equals(GET)) return new HttpGet(request);
    else if(verb.equals(PUT)) {HttpPut put = new HttpPut(request); put.setEntity(new UrlEncodedFormEntity(bodyParams, HTTP.UTF_8)); setParams(put); return put;}
    else if(verb.equals(DELETE)) return new HttpDelete(request);
    else Log.e("Utils", "Cannot reckognize the verb " + verb);
    return null;
  }

  private static void setParams(HttpEntityEnclosingRequestBase req){
    req.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
  }

  private static String generateBoundaryString(int length) {
    StringBuffer buffer=new StringBuffer(length);
    for (int i=0;i<length;i++) {
      buffer.append(ALLOWED_CHARS.charAt((int)(Math.random()*ALLOWED_CHARS.length())));
    }

    return buffer.toString();
  }
}