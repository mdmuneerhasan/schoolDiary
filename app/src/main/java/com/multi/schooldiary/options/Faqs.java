package com.multi.schooldiary.options;

class Faqs {
    String question,asker,answer,responser,uid,key;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAsker() {
        return asker;
    }

    public void setAsker(String asker) {
        this.asker = asker;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getResponser() {
        return responser;
    }

    public void setResponser(String responser) {
        this.responser = responser;
    }

    public Faqs() {
    }

    public Faqs(String question, String asker, String answer, String responser) {
        this.question = question;
        this.asker = asker;
        this.answer = answer;
        this.responser = responser;
    }
}
