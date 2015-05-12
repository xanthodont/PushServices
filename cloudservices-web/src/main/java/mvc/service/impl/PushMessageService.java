package mvc.service.impl;

import java.util.List;

import mvc.dao.IPushMessageDao;
import mvc.dao.PageList;
import mvc.model.PushMessage;
import mvc.service.IPushMessageService;

public class PushMessageService implements IPushMessageService {
	private IPushMessageDao pushMessageDao;

	public IPushMessageDao getPushMessageDao() {
		return pushMessageDao;
	}

	public void setPushMessageDao(IPushMessageDao pushMessageDao) {
		this.pushMessageDao = pushMessageDao;
	}

	@Override
	public PageList<PushMessage> getMessageList(int page, int size) {
		// TODO Auto-generated method stub
		int firstFetch = (page-1)*size;
		PageList<PushMessage> list = new PageList<PushMessage>();
		List<PushMessage> rows = pushMessageDao.findAndOrderByProperty(firstFetch, size, "updateTime", false);
		int total = pushMessageDao.countAll();
		list.setRows(rows);
		list.setTotal(total);
		return list;
	}
	
	
}
