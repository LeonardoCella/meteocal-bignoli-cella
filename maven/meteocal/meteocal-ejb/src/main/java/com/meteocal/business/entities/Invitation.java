/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.entities;

import com.meteocal.business.entities.keys.InvitationID;
import javax.persistence.*;

/**
 *
 * @author Andrea Bignoli
 */

@Entity
@IdClass(InvitationID.class)
@Table(name = "INVITATION")
public class Invitation {
    
    @Id
    private int userID;
    
    @Id
    private int eventID;
    
    @Column(columnDefinition="boolean default false")
    private boolean seen;

    @ManyToOne(fetch=FetchType.LAZY)
    @PrimaryKeyJoinColumn(name="USERID", referencedColumnName = "ID")
    private User user;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @PrimaryKeyJoinColumn(name="EVENTID", referencedColumnName = "ID")
    private Event event;
    
    
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
    
    
}