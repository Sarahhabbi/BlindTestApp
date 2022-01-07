package models;

public class Audio implements HasId {
    private String answer;
    private String url;

    public Audio(String url, String answer) {
        this.answer = answer;
        this.url = url;
    }

    @Override
    public String getId() {
        return url;
    }

    public String getAnswer() {
        return answer;
    }
}
