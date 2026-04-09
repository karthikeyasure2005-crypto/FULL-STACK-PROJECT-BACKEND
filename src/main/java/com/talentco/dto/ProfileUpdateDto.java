package com.talentco.dto;

public class ProfileUpdateDto {
    private String bio;
    private String skills;
    private Double hourlyRate;
    private String location;
    private String availability;
    private String category;
    private Integer experienceYears;

    public ProfileUpdateDto() {}

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public Double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(Double hourlyRate) { this.hourlyRate = hourlyRate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
}
