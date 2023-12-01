package com.cse360;


public class TableInformation {

    private String project;
    private String elapsedTime;
    private String lifeCycleStep;
    private String effortCategory;
    private String plan;

    public TableInformation() {
        this.project = "";
        this.elapsedTime = "";
        this.lifeCycleStep = "";
        this.effortCategory = "";
        this.plan = "";   
    }

    public TableInformation(String project, String elapsedTime, String lifeCycleStep, String effortCategory, String plan) {
        this.project = project;
        this.elapsedTime = elapsedTime;
        this.lifeCycleStep = lifeCycleStep;
        this.effortCategory = effortCategory;
        this.plan = plan;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getLifeCycleStep() {
        return lifeCycleStep;
    }

    public void setLifeCycleStep(String lifeCycleStep) {
        this.lifeCycleStep = lifeCycleStep;
    }

    public String getEffortCategory() {
        return effortCategory;
    }

    public void setEffortCategory(String effortCategory) {
        this.effortCategory = effortCategory;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

}