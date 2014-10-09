package net.gillessed.tablemahjong.user.local;

import java.util.HashSet;
import java.util.Set;

import net.gillessed.tablemahjong.server.TableMahjongServer;
import net.gillessed.tablemahjong.user.User;
import net.gillessed.tablemahjong.user.UserFactory;
import net.gillessed.tablemahjong.user.UserLogin;

public class LocalUserLogin implements UserLogin {
	
	private final TableMahjongServer tableMahjongServer;
	private final LocalUserFactory userFactory;
	private String loginFailure;

	public LocalUserLogin(TableMahjongServer tableMahjongServer) {
		this.tableMahjongServer = tableMahjongServer;
		userFactory = new LocalUserFactory();
	}
	
	@Override
	public boolean login(User user) {
		Set<String> usernames = new HashSet<String>();
		for(User u : tableMahjongServer.getMainThreadSpace().getUsers().values()) {
			usernames.add(u.getName());
		}
		if(usernames.contains(user.getName())) {
			loginFailure = "User " + user.getName() + " already exists";
			return false;
		}
		loginFailure = "";
		return true;
	}
	
	@Override
	public String getLoginFailure() {
		return loginFailure;
	}

	@Override
	public UserFactory getUserFactory() {
		return userFactory;
	}
}
