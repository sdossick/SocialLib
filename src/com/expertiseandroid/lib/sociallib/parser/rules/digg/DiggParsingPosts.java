package com.expertiseandroid.lib.sociallib.parser.rules.digg;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import com.expertiseandroid.lib.sociallib.model.digg.DiggPost;
import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

public class DiggParsingPosts extends DiggParsing implements ParsingRules {

  private List<DiggPost> list;
  private DiggPost currentPost;
  
  public DiggParsingPosts(){
    list = new ArrayList<DiggPost>();
  }
  
  public void enter(String tag, Attributes attributes, int lvl) {
    if(tag.equals(STORIES)) addPost();
  }

  public void leave(String tag, int lvl) {}

  public void text(String tag, String text, int lvl) {
    if(tag.equals(PERMALINK)) setPermalink(text);
    else if(tag.equals(DESCRIPTION)) setDescription(text);
    else if(tag.equals(TITLE)) setTitle(text);
    else if(tag.equals(STORY_ID)) setId(text);
    else if(tag.equals(DIGGS)) setDiggs(text);
  }

  private void addPost() {
    DiggPost dp = new DiggPost();
    list.add(dp);
    currentPost = dp;
  }
  
  private void setPermalink(String text) {
    currentPost.permalink = text;
  }

  private void setDescription(String text) {
    currentPost.description = text;
  }

  private void setTitle(String text) {
    currentPost.title = text;
  }

  private void setId(String text) {
    currentPost.id = text;
  }

  private void setDiggs(String text) {
    currentPost.diggs = new Integer(text);
  }

  public List<DiggPost> getContents() {
    return list;
  }

}