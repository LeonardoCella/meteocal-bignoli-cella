/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.entities;

import com.meteocal.business.entities.shared.NotificationType;
import com.meteocal.business.entities.shared.TableDictionary;
import com.meteocal.business.entities.shared.WeatherCondition;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Andrea Bignoli
 */
@Entity
@Table(name = TableDictionary.TABLE_NOTIFICATION)
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @PrimaryKeyJoinColumn(name = "EVENTID", referencedColumnName = "ID")
    private Event event;

    @ManyToMany
    @JoinTable(name = "NOTIFICATIONVIEW",
            joinColumns = {
                @JoinColumn(name = "notificationID",
                        referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "userID",
                        referencedColumnName = "id")})
    private List<User> notificatedUsers;
    
    @NotNull
    private NotificationType type;
    
    private boolean goodWeather;
    
    @NotNull
    private LocalDateTime generationDateTime;

    // TODO Add all other parameters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<User> getNotificatedUsers() {
        if(notificatedUsers == null)
            notificatedUsers = new ArrayList<User>();
        
        return notificatedUsers;
    }

    public void setNotificatedUsers(List<User> notificatedUsers) {
        this.notificatedUsers = notificatedUsers;
    }

    public void addNotificatedUser(User u) {
        List<User> notificatedUsers = this.getNotificatedUsers();

        if (!notificatedUsers.contains(u)) {
            notificatedUsers.add(u);
            u.getNotifications().add(this);
        }
    }

    public void removeNotificatedUser(User u) {
        List<User> notificatedUsers = this.getNotificatedUsers();

        // If u is in the participants list, he gets removed and the list of events he's participanting to gets updated
        if (notificatedUsers.remove(u)) {
            u.getNotifications().remove(this);
        }
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public boolean isGoodWeather() {
        return goodWeather;
    }

    public void setGoodWeather(boolean goodWeather) {
        this.goodWeather = goodWeather;
    }

    public LocalDateTime getGenerationDateTime() {
        return generationDateTime;
    }

    public void setGenerationDateTime(LocalDateTime generationDateTime) {
        this.generationDateTime = generationDateTime;
    }

    
    
}
