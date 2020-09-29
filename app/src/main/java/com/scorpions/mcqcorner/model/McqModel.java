package com.scorpions.mcqcorner.model;

public class McqModel {
    private String userId, question, optionA, optionB, optionC, optionD, answer, category;
    private long time;

    public McqModel() {

    }

    public McqModel(String userId, String question, String optionA, String optionB, String optionC, String optionD, String answer, String category, long time) {
        this.userId = userId;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
        this.category = category;
        this.time = time;
    }

    public String getUserId() {
        return userId;
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

    public long getTime() {
        return time;
    }
}