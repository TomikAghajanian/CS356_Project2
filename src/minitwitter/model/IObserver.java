package minitwitter.model;

public interface IObserver {
    public void update(String userID, String tweet);
}
