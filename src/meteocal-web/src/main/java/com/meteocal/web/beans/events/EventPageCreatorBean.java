/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.events;

import com.meteocal.business.entities.Event;
import com.meteocal.business.entities.WeatherForecast;
import com.meteocal.business.entities.shared.WeatherCondition;
import com.meteocal.business.exceptions.BusinessException;
import com.meteocal.business.exceptions.NotFoundException;
import com.meteocal.business.facade.EventFacade;
import com.meteocal.business.facade.UserFacade;
import com.meteocal.web.exceptions.NotValidParameter;
import com.meteocal.web.utility.SessionUtility;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Leonardo Cella
 */
@ManagedBean
@RequestScoped
public class EventPageCreatorBean implements Serializable {

    private String newParticipant;
    private final String errorOutcome = "Error";
    private Event referredEvent, tmpEv;
    private final HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    private int eventID;

    @Inject
    private SessionUtility sessionUtility;

    @EJB
    UserFacade uf;

    @EJB
    EventFacade ef;

    @PostConstruct
    public void init() {

        FacesContext fc = FacesContext.getCurrentInstance();

        try {
            eventID = this.getID();
            tmpEv = ef.find(eventID);
            if (tmpEv == null)
                fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorOutcome);
        }
        catch (NotValidParameter ex) {
                fc.getApplication().getNavigationHandler().handleNavigation(fc, null, errorOutcome);
        }

        setReferredEvent(tmpEv);
    }

    public String addParticipant() {
        try {
            ef.addInvited(eventID, newParticipant);
        }
        catch (BusinessException ex) {
            Logger.getLogger(EventPageCreatorBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Info!", "Participant not added!"));

            return "/protected/event/EventPageCreator?faces-redirect=true";
        }
        sessionUtility.setParameter(eventID);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", "Participant addedd!"));

        return "/protected/event/EventPageCreator?faces-redirect=true";
    }

    private int getID() throws NotValidParameter {
        String strID;

        if (!sessionUtility.isAParameter()) {
            strID = request.getParameter("eventID");
            try {
                eventID = Integer.parseInt(strID);
            }
            catch (NumberFormatException e) {
                eventID = sessionUtility.getParameterAsClient();
            }
        }
        else
            eventID = sessionUtility.getParameterAsClient();
        
        

        return eventID;
    }

    public void setReferredEvent(Event event) {
        referredEvent = event;
    }

    public Event getReferredEvent() {
        return referredEvent;
    }

    public String getName() {
        return referredEvent.getName();
    }

    public String getAddress() {
        return referredEvent.getAddress();
    }

    public String getCity() {
        return referredEvent.getCity();
    }

    public String getCountry() {
        return referredEvent.getCountry();
    }

    public String getDescription() {
        return referredEvent.getDescription();
    }
    
    public String getStart() {
        return referredEvent.getStart().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }
    
    public String getEnd() {
        return referredEvent.getEnd().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }
    
    public String getWeatherConditionsToAvoid() {
        String result = "";
        
        for(WeatherCondition wc: referredEvent.getAdverseConditions()) {
            result += wc.toString() + " ";
        }
        
        return result;
    }

    public List<WeatherForecast> getWeatherForecasts() {
        return referredEvent.getWeatherForecasts();
    }

    public String getInvited() {
        return referredEvent.getInvitedAsString();
    }

    public String getParticipant() {
        return newParticipant;
    }

    public void setParticipant(String participant) {
        newParticipant = participant;
    }

    public boolean getIndoor() {
        return referredEvent.isIndoor();
    }

    public boolean getPrivate() {
        return referredEvent.isPrivateEvent();
    }

    public String getParticipants() {
        return referredEvent.getParticipantsAsString();
    }

    public boolean getRender() throws NotFoundException {
        return ef.isSuggestedChangeAvailable(eventID);
    }
}
