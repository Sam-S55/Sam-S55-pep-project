package Service;

import Model.Account;
import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;

import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;
    private List<Integer> blacklist = new ArrayList<Integer>();

    public MessageService () {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public MessageService (MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int message_id) {
        if (blacklist.contains(message_id)) {
            return null;
        }
        return messageDAO.getMessageByID(message_id);
    }

    public Message createMessage (Message msg) {
        List<Account> accts = accountDAO.getAllAccounts();
        boolean exists = false;
        for (int i=0; i<accts.size(); i++) {
            if (msg.getPosted_by() == accts.get(i).getAccount_id()) {
                exists = true;
            }
        }
        if (exists && !msg.message_text.isEmpty() && msg.message_text.length()<=255) {
            return messageDAO.createMessage(msg);
        }
        return null;
    }

    public Message deleteMessageByID (int id) {
        Message msg = getMessageByID(id);
        List<Message> msgs = getAllMessages();
        boolean exists = false;
        for (int i=0; i<msgs.size(); i++) {
            if (id == msgs.get(i).getMessage_id()) {
                exists = true;
            }
        }
        if (exists) {
            messageDAO.deleteMessageByID(id);
            blacklist.add(id);
            return msg;
        }
        return null;
    }

    public Message updateMessage (int id, String update) {
        List<Message> msgs = getAllMessages();
        Message msg = getMessageByID(id);
        boolean exists = false;
        for (int i=0; i<msgs.size(); i++) {
            if (id == msgs.get(i).getMessage_id()) {
                exists = true;
            }
        }
        if (exists && !update.isBlank() && update.length()<=255) {
            messageDAO.updateMessage(id,update);
            msg = getMessageByID(id);
            return msg;
        } else {
            return null;
        }
    }

    public List<Message> getAllMessagesByUser (int posted_by) {
        return messageDAO.getAllMessagesByUser(posted_by);
    }

}
