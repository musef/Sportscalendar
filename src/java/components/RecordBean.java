/*
 * Copyright (C) fmsdevelopment.com author musef2904@gmail.com
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package components;


/**
 *
 * @author musef2904@gmail.com
 */

public class RecordBean {
    
    private long sportidx;
    private long activityidx;    

    private String thisday;
    private String nameact;
    private String distance;
    private String slope;
    private String duration;
    private String description;
    private String site;
    private String message;
    
    

    /**
     * @return the sportidx
     */
    public long getSportidx() {
        return sportidx;
    }

    /**
     * @param sportidx the sportidx to set
     */
    public void setSportidx(long sportidx) {
        this.sportidx = sportidx;
    }

    /**
     * @return the activityidx
     */
    public long getActivityidx() {
        return activityidx;
    }

    /**
     * @param activityidx the activityidx to set
     */
    public void setActivityidx(long activityidx) {
        this.activityidx = activityidx;
    }

    /**
     * @return the thisday
     */
    public String getThisday() {
        return thisday;
    }

    /**
     * @param thisday the thisday to set
     */
    public void setThisday(String thisday) {
        this.thisday = thisday;
    }

    /**
     * @return the nameact
     */
    public String getNameact() {
        return nameact;
    }

    /**
     * @param nameact the nameact to set
     */
    public void setNameact(String nameact) {
        this.nameact = nameact;
    }

    /**
     * @return the distance
     */
    public String getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(String distance) {
        this.distance = distance;
    }

    /**
     * @return the slope
     */
    public String getSlope() {
        return slope;
    }

    /**
     * @param slope the slope to set
     */
    public void setSlope(String slope) {
        this.slope = slope;
    }

    /**
     * @return the duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the site
     */
    public String getSite() {
        return site;
    }

    /**
     * @param site the site to set
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
}
