package com.scorpions.mcqcorner.model;

public class McqModel {
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String answer;
    private String category;

    public McqModel(String question, String optionA, String optionB, String optionC, String optionD, String answer, String category)
    {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }
    public String getOptionA() {
        return optionA;
    }
    public String getOptionB() {
        return optionB;
    }
    public String getOptionC() {
        return optionC;
    }
    public String getOptionD() {
        return optionD;
    }
    public String getAnswer() {
        return answer;
    }
    public String getCategory() {
        return category;
    }

}