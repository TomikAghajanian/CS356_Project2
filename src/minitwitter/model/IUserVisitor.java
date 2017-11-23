package minitwitter.model;

import minitwitter.model.User;
import minitwitter.model.UserGroup;


public interface IUserVisitor {
	public int getUserInfo(User user);

	public void getGroupInfo(UserGroup group);
         public String lastUpdateTimeUser(UserGroup userGroup);
}
