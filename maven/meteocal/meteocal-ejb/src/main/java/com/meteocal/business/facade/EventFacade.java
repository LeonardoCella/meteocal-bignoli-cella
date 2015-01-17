/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.business.facade;

import com.meteocal.business.entities.Event;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.shared.security.UserEventVisibility;

/**
 *
 * @author USUARIO
 */

public interface EventFacade {
    
    public abstract Event create(Event e);
    
    public abstract Event find(int eventID);
    
    public abstract void addParticipant(int eventID, int userID) throws BusinessException;
    
    public abstract void removeParticipant(int eventID, int userID) throws BusinessException;
    
    public abstract void toggleParticipant(int eventID, int userID) throws BusinessException;
    
    public abstract void updateScheduling(int eventID, int start, int end) throws BusinessException;
    
    public abstract void updateData(Event e) throws BusinessException;
    
    /**
     * Gets the type of visibility that the user identified by userID has over the event identified by eventID.
     * 
     * @param userID
     * @param eventID
     * @return
     * @throws NotFoundException 
     * If the requested userID or eventID doesn't exist
     */
    public UserEventVisibility getVisibilityOnEvent(int userID, int eventID) throws NotFoundException;
    
    
}