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

package com.expertiseandroid.lib.sociallib.model.linkedin;

/**
 * An enumeration of differents post types in LinkedIn
 * @see http://developer.linkedin.com/docs/DOC-1131
 * @author Expertise Android
 *
 */
public enum LinkedInPostType {

  ANSW("Answer Update"),
  APPS("Application Update"),
  CONN("Connection updates"),
  JOBS("Posted a job"),
  JGRP("Joined a group"),
  PICT("Changed a picture"),
  PRFX("Extended profile update"),
  RECU("Recommendations"),
  PRFU("Changed profile"),
  QSTN("Question update"),
  STAT("Status update"),
  UKWN("Unknown");
  
  public String name;
  
  private LinkedInPostType(String name){
    this.name = name;
  }
  
  /**
   * Gets a LinkedIn network update type
   * @param typeName the 4-letters acronym of the type
   * @see http://developer.linkedin.com/docs/DOC-1131
   * @return the network update type corresponding to the acronym
   */
  public static LinkedInPostType getTypeByName(String typeName){
    if(typeName.equals("ANSW")) return ANSW;
    else if(typeName.equals("APPS")||typeName.equals("APPM")) return APPS;
    else if(typeName.equals("CONN")||typeName.equals("NCON")||typeName.equals("CCEM")) return CONN;
    else if(typeName.equals("JOBS")||typeName.equals("JOBP")) return JOBS;
    else if(typeName.equals("JGRP")) return JGRP;
    else if(typeName.equals("PICT")||typeName.equals("PICU")) return PICT;
    else if(typeName.equals("PRFX")) return PRFX;
    else if(typeName.equals("RECU")||typeName.equals("PREC")) return RECU;
    else if(typeName.equals("PRFU")||typeName.equals("PROF")) return PRFU;
    else if(typeName.equals("QSTN")) return QSTN;
    else if(typeName.equals("STAT")) return STAT;
    else return UKWN;
  }
}