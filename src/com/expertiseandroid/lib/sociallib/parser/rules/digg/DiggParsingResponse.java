package com.expertiseandroid.lib.sociallib.parser.rules.digg;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.util.Log;

import com.expertiseandroid.lib.sociallib.parser.rules.ParsingRules;

public class DiggParsingResponse extends DiggParsing implements ParsingRules{

  private boolean verite = true;
  
  public void enter(String tag, Attributes attributes, int lvl) {}

  public void leave(String tag, int lvl) {}

  public void text(String tag, String text, int lvl) {
    if(tag.equals(MESSAGE)){
      Log.e("SocialLib", text);
      verite = false;
    }
  }

  public List<?> getContents() {
    List<String> list = new ArrayList<String>();
    if (verite)
      list.add("TRUE");
    else
      list.add("FALSE");
    return list;
  }

}