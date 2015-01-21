/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meteocal.web.beans.personal;

import com.meteocal.business.facade.UserFacade;
import com.meteocal.web.utility.SYSO_Testing;
import com.meteocal.web.utility.SessionUtility;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author USUARIO
 */
@ManagedBean
@RequestScoped
public class LoginBean {

    private String username;
    private String password;

    @Inject
    private SessionUtility sessionUtility;

    @EJB
    UserFacade uf;
    
    @PostConstruct
    public void init() {
    }

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String login() {
        if (sessionUtility != null)  {
            if (sessionUtility.isThereAnActiveSession()) {
                SYSO_Testing.syso("I redirect you to your active session");
                return "/protected/personal/HomeCalendarMonth";
            }
        }
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        SYSO_Testing.clean();
        SYSO_Testing.syso("Try to login user: " + this.username + " with psw: " + this.password + " !");

        if(uf.isUsernameInUse(username)){
            return "/Index.xhtml?used=yes";
        }
                
        try {
            request.login(this.username, this.password);
        }
        catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Login failed."));
            SYSO_Testing.syso("LoginBean. Login failed" + e.toString());
            return "/Error";
        }
        SYSO_Testing.syso("LoginBean. Login successful");
        sessionUtility.addUser(username);

        return "/protected/personal/HomeCalendarMonth";
    }

    public void logout() {
        SYSO_Testing.syso("LoginBean. starting logout!");

        String contextPath;

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        sessionUtility.sessionLogout();
        request.getSession().invalidate();
        try {
            contextPath = request.getContextPath();
            FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/Index.xhtml");
        }
        catch (IOException ex) {
        }
    }

}