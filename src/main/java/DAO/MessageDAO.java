package DAO;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

   public Message insertMessage(Message mes) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, mes.getPosted_by());
        preparedStatement.setString(2, mes.getMessage_text());
        preparedStatement.setLong(3, mes.getTime_posted_epoch());

        preparedStatement.executeUpdate();
        ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();

        if(pkeyResultSet.next()) {
            int generated_message_id = (int) pkeyResultSet.getLong(1);
            return new Message(generated_message_id, mes.getPosted_by(), mes.getMessage_text(), mes.getTime_posted_epoch());
        }

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return null;
   }

   public Message existingUser(Message mes) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "SELECT * FROM message WHERE posted_by = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, mes.getPosted_by());

        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
            rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            return message;
        }

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }

    return null;
   }

   public List<Message> getAllMessages () {
    Connection connection = ConnectionUtil.getConnection();
    List<Message> messages = new ArrayList<>();
    try {
        String sql = "SELECT * FROM message";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
            rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            messages.add(message);
        }

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return messages;
   }

   public Message getMessageById(int id) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "SELECT * FROM message WHERE message_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
            rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            return message;
        }

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
   
    return null;
   }


   public Message deleteMessageById(int id) {
    Connection connection = ConnectionUtil.getConnection();
    try {
           
        String selectSql = "SELECT * FROM message WHERE message_id = ?";
        String deleteSql = "DELETE FROM message WHERE message_id = ?";

        // First, attempt to select the message to be deleted
        PreparedStatement selectStatement = connection.prepareStatement(selectSql);
        selectStatement.setInt(1, id);
        ResultSet rs = selectStatement.executeQuery();

        if (rs.next()) {
            // Message with the specified ID exists; store it for later return
            Message deletedMessage = new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );

            // Now, proceed to delete the message
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setInt(1, id);
            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Message was deleted successfully
                return deletedMessage;
            }
        }

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return null; // Deletion was not successful
    }


    public Message updateMessage (int id, String message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            //Write SQL logic here
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message);
            preparedStatement.setInt(2, id);

            int rowsAffected = preparedStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                // The message was updated successfully; return the updated message
                Message updatedMessage = getMessageById(id);
                return updatedMessage; // You can use your existing getMessageById method
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> getAllMessagesByAccountId(int id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
    
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            ResultSet rs = preparedStatement.executeQuery();
    
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

}
