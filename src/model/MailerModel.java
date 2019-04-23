/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import data.SQLite;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import util.ValidatorUtils;

/**
 *
 * @author hkaraoglu
 */
public class MailerModel
{
    private final int itemId;
    private final String from;
    private final String to;
    private final String smtp;
    private final String username;
    private final String password;

    public MailerModel(int itemId, String from, String to, String smtp, String username, String password)
    {
        this.from = from;
        this.to = to;
        this.smtp = smtp;
        this.username = username;
        this.password = password;
        this.itemId = itemId;
    }

    public String getFrom()
    {
        return from;
    }

    public String getTo()
    {
        return to;
    }

    public String getSmtp()
    {
        return smtp;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public void send(String subject, String text)
    {
        try 
        {
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", smtp);
            Session session = Session.getDefaultInstance(properties);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } 
        catch (MessagingException mex) {
         mex.printStackTrace();
      } 
    }
    
    private static Boolean isTestConnectionSuccess(String smtp, int port, String username, String password)
    {
        try {
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", smtp);
            Session session = Session.getDefaultInstance(props);
            Transport transport = session.getTransport("smtp");
            transport.connect(username, password);
            transport.close();
            System.out.println("success");
            return true;
        } 
        catch(AuthenticationFailedException e) {
              System.out.println("AuthenticationFailedException - for authentication failures");
              e.printStackTrace();
              return false;
        }
        catch(MessagingException e) {
              System.out.println("for other failures");
              e.printStackTrace();
              return false;
        }
    }
    
    public static void sendToAllMailList(String subject, String text)
    {
        for(MailerModel mail : getList())
        {
            mail.send(subject, text);
        }
    }
    
    public static List<MailerModel> getList()
    {
        ArrayList<MailerModel> mailList = new ArrayList<>();
        try
        {
            PreparedStatement ps = SQLite.getConnection().prepareStatement("SELECT * FROM mail_list");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                MailerModel mail = new MailerModel(
                        rs.getInt("item_id"), 
                        rs.getString("from"), 
                        rs.getString("to"), 
                        rs.getString("smtp"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                mailList.add(mail);
            }
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mailList;
    }
    
    public static ResultModel add(String smtp, String port, String from, String to, String username, String password)
    {
        ResultModel result = new ResultModel();
        if(!ValidatorUtils.isPort(port))
        {
            result.message = "Invalid port number!";
        }
        else if(!isTestConnectionSuccess(smtp, Integer.parseInt(port), username, password))
        {
            result.message = "Couldn't connect to smtp server!";
        }
        else
        {
            try
            {
                PreparedStatement ps = SQLite.getConnection().prepareStatement("INSERT INTO mail_list(`from`, `to`, `smtp`, `port`, `username`, `password`) VALUES(?, ?, ?, ?, ?, ?)");
                ps.setString(1, from);
                ps.setString(2, to);
                ps.setString(3, smtp);
                ps.setInt(4, Integer.parseInt(port));
                ps.setString(5, username);
                ps.setString(6, password);
                ps.execute();
                if(ps.getUpdateCount() > 0)
                {
                    result.success = true;
                }
                else
                {
                    result.message = "Record is couldn't be added";
                    return result;
                }
                ResultSet rs = ps.getGeneratedKeys();
                int itemId = 0;
                if(rs.next()){
                    itemId = rs.getInt(1);
                }
                MailerModel mailModel = new MailerModel(itemId, from, to, smtp, username, password);
                result.data = mailModel;

            } catch (SQLException ex)
            {
                Logger.getLogger(MailerModel.class.getName()).log(Level.SEVERE, null, ex);
                result.message = "Record is couldn't be added(" + ex.getMessage() + " )";
            }
        }
        
        
        return result;
    }

    public void delete()
    {
        try
        {
            PreparedStatement ps = SQLite.getConnection().prepareStatement("DELETE FROM mail_list WHERE item_id = ?");
            ps.setInt(1, itemId);
            ps.execute();
            
        } catch (SQLException ex)
        {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
