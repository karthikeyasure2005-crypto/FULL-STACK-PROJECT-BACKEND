package com.talentco.dto;

public class HiringRequestDto {
    private Long professionalId;
    private String projectTitle;
    private String message;
    private Double budget;

    public HiringRequestDto() {}

    public Long getProfessionalId() { return professionalId; }
    public void setProfessionalId(Long professionalId) { this.professionalId = professionalId; }

    public String getProjectTitle() { return projectTitle; }
    public void setProjectTitle(String projectTitle) { this.projectTitle = projectTitle; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }
}
