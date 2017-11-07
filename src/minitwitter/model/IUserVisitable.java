package minitwitter.model;

public interface IUserVisitable {

    public void accept(IUserVisitor visitor);
}
