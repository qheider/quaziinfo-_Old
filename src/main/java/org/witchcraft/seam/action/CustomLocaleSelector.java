package org.witchcraft.users.action;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.LocaleSelector;

@Name("customLocaleSelector")
@Scope(ScopeType.SESSION)
public class CustomLocaleSelector implements Serializable{
    //@In(value = "userprofile", create = true)
    //private Profile profile;

    @In
    private LocaleSelector localeSelector;

    //@In
    //private ProfileDao profileDao;
    public String select() {
        Locale locale = localeSelector.getLocale();
        /*
         * if (profile != null) { profile.setLocale(locale);
         * profile.setLanguageCode(locale.getLanguage());
         * profileDao.merge(profile); }
         */
        localeSelector.select();
        return "";
    }

    public String selectFrench() {

        FacesContext context = FacesContext.getCurrentInstance();

        context.getViewRoot().setLocale(Locale.CANADA_FRENCH);


        return "";

    }

    public String selectEnglish() {

        FacesContext context = FacesContext.getCurrentInstance();

        context.getViewRoot().setLocale(Locale.ENGLISH);


        return "";

    }
    public Locale getLocale(){
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }
}
