package net.gillessed.tablemahjong.user;

public interface UserLogin {
	public boolean login(User user);
	public String getLoginFailure();
	public UserFactory getUserFactory();
}
