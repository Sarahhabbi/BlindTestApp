package models;

public class Audio implements HasId {
    private String answer;
    private String url;

    public Audio(String answer, String url) {
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
