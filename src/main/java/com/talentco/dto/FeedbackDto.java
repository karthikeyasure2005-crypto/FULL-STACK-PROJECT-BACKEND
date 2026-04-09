package com.talentco.dto;

public class FeedbackDto {
    private Long professionalId;
    private Integer rating;
    private String comment;

    public FeedbackDto() {}

    public Long getProfessionalId() { return professionalId; }
    public void setProfessionalId(Long professionalId) { this.professionalId = professionalId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
