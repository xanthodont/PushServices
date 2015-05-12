package mvc.service;

import mvc.dao.PageList;
import mvc.model.PushMessage;

public interface IPushMessageService {

	PageList<PushMessage> getMessageList(int page, int size);

}
