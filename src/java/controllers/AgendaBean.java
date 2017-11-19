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
package controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author musef2904@gmail.com
 */
@ManagedBean
@RequestScoped
public class AgendaBean {

    private long sportidx;
    private long activityidx;

    private String timming;
    private String distance;
    private String slope;
    private String comments;
    
    public AgendaBean() {
            
        
        
    }
    
    
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
     * @return the timming
     */
    public String getTimming() {
        return timming;
    }

    /**
     * @param timming the timming to set
     */
    public void setTimming(String timming) {
        this.timming = timming;
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
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }


    
}
