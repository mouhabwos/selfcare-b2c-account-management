package sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.audit;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author mbbsow
 */
public class SelfcareLog implements Serializable {

    //  public static final String PERIMETRE = "Selfcare";
    public static final String TYPE = "Audit";

    private transient Object[] params;
    private String browser;
    private String ip;
    private String description;
    private String userName;
    private String typeAction;
    private String perimetre;
    private SelfcareLogException selfcareLogException;


    public SelfcareLog() {
        this.params = new Object[0];
        this.userName = "";
        this.browser = "";
        this.ip = "";
        this.description = "";
        this.userName = "";
        this.typeAction = "";
        this.perimetre = "";
        this.selfcareLogException = null;
    }

    private static String getCurrentDate() {
        return DateFormat.getDateTimeInstance ( DateFormat.MEDIUM, DateFormat.MEDIUM ).format ( new Date () );
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String userAgent) {
        this.browser = userAgent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String user) {
        this.userName = user;
    }

    public SelfcareLogException getSelfcareLogException() {

        return selfcareLogException;
    }

    public void setSelfcareLogException(SelfcareLogException selfcareLogException) {
        this.selfcareLogException = selfcareLogException;
    }

    public String getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(String typeAction) {
        this.typeAction = typeAction;
    }

    public String getPerimetre() {
        return perimetre;
    }

    public void setPerimetre(String perimetre) {
        this.perimetre = perimetre;
    }

    @Override
    public String toString() {
        setUserName ( null == userName ? " unknown " : userName.replaceAll ( "]", "" ).replaceAll ( "\\[", "" ) );

        StringBuilder builder = new StringBuilder ();
        builder.append ( "\n============================================================================\n" )
            .append ( "Date='" + getCurrentDate () + "\n" )
            .append ( "Utilisateur='" + userName + "\n" )
            .append ( "Adresse IP='" + ip + "\n" )
            .append ( "Navigateur='" + browser + "\n" )
            .append ( "Action=" + description + "\n" )
            .append ( "Paramètres=" + Arrays.toString ( params ) + "\n" )
            .append ( "Perimetre=" + perimetre + "\n" )
            .append ( "Type Action=" + typeAction + "\n" )
            .append ( selfcareLogException != null ? "Selfcare Exception=" + selfcareLogException + "\n" : "" )
            .append ( "===========================================================================\n" );

        return builder.toString ();
    }
}
