package Service;
import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService( MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message createMessage (Message mes) {
        return messageDAO.insertMessage(mes);
    }

    public Message existingUser (Message mes) {
        return messageDAO.existingUser(mes);
    }

    public List<Message> getAllMessages () {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessage(int id) {
        return messageDAO.deleteMessageById(id);
    }
}
