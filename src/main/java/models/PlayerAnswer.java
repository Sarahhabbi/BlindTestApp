package models;

import java.io.Serializable;

public class PlayerAnswer implements Serializable {
    private String pseudoPlayer;
    private long answerTime ; //getTimeInMillis()
    private String answer;

    public PlayerAnswer(String pseudoPlayer, long answerTime, String answer) {
        this.pseudoPlayer = pseudoPlayer;
        this.answerTime = answerTime;
        this.answer = answer;
    }

    public String getPseudoPlayer() {
        return pseudoPlayer;
    }

    public void setPseudoPlayer(String pseudoPlayer) {
        this.pseudoPlayer = pseudoPlayer;
    }

    public long getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(long answerTime) {
        this.answerTime = answerTime;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
