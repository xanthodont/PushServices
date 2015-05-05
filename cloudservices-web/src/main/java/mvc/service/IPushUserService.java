package mvc.service;

import java.util.List;

import mvc.dao.PageList;
import mvc.model.PushUser;

public interface IPushUserService {

	PageList<PushUser> getUserList(int page, int size);

	int getTotalOnline();
	
}
