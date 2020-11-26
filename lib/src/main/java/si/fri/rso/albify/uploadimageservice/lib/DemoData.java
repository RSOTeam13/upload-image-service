package si.fri.rso.albify.uploadimageservice.lib;

import java.util.Date;

public class DemoData {

    private String[] clani;
    private String opis_projekta;
    private String[] mikrostoritve;
    private String[] github;
    private String[] travis;
    private String[] dockerhub;

    public String[] getClani() {
        return clani;
    }

    public void setClani(String[] clani) {
        this.clani = clani;
    }

    public String getOpis_projekta() {
        return opis_projekta;
    }

    public void setOpis_projekta(String opis_projekta) {
        this.opis_projekta = opis_projekta;
    }

    public String[] getMikrostoritve() {
        return mikrostoritve;
    }

    public void setMikrostoritve(String[] mikrostoritve) {
        this.mikrostoritve = mikrostoritve;
    }

    public String[] getGithub() {
        return github;
    }

    public void setGithub(String[] github) {
        this.github = github;
    }

    public String[] getTravis() {
        return travis;
    }

    public void setTravis(String[] travis) {
        this.travis = travis;
    }

    public String[] getDockerhub() {
        return dockerhub;
    }

    public void setDockerhub(String[] dockerhub) {
        this.dockerhub = dockerhub;
    }
}
