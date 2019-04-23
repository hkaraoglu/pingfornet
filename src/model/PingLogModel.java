package model;

import data.SQLite;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;
import util.DateUtils;

/**
 *
 * @author hasankaraoglu
 */
public class PingLogModel extends Thread
{
    private final PingModel pingModel;
    private int pingLogId;
    private String dateAdded;
    private String dateModified;
    private String dateEnded;
    private int successCount = 0;
    private int failCount = 0;
    private Boolean currentStatus = null; 
    private Timer timer;
    private PingLogListener listener;
    private int requestCounter = 0;
    
    public PingLogModel(PingModel pingModel)
    {
        this.pingModel = pingModel;
    }

    public PingModel getPingModel()
    {
        return pingModel;
    }

    public int getPingLogId()
    {
        return pingLogId;
    }

    public String getDateAdded()
    {
        return dateAdded;
    }

    public String getDateModified()
    {
        return dateModified;
    }

    public String getDateEnded()
    {
        return dateEnded;
    }

    public int getSuccessCount()
    {
        return successCount;
    }

    public int getFailCount()
    {
        return failCount;
    }

    public Boolean getCurrentStatus()
    {
        return currentStatus;
    }

    public Timer getTimer()
    {
        return timer;
    }
    
    @Override
    public synchronized void start()
    {
        try
        {
            PreparedStatement ps = SQLite.getConnection().prepareStatement("INSERT INTO ping_log (ping_id, date_added) VALUES(?, ?)");
            ps.setInt(1, pingModel.getPingId());
            dateAdded = DateUtils.getCurrentDateTimeAsString();
            ps.setString(2, dateAdded);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                pingLogId = rs.getInt(1);
            }
            super.start();
        } catch (SQLException ex)
        {
            Logger.getLogger(RequestLogModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run()
    {
      TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
 
                try 
                {
                    pingIpHost();
                    PreparedStatement ps = SQLite.getConnection().prepareStatement("UPDATE ping_log SET date_modified = ?, success_count = ?, fail_count = ? WHERE ping_log_id = ?");
                    dateModified = DateUtils.getCurrentDateTimeAsString();
                    ps.setString(1, dateModified);
                    ps.setInt(2, successCount);
                    ps.setInt(3, failCount);
                    ps.setInt(4, pingLogId);
                    ps.execute();
                    listener.onPingLogUpdated(PingLogModel.this);
                } 
                catch (SQLException ex)
                {
                     System.out.println(ex);
                }
               
            }
        };
        timer = new Timer("PingLogTimer");
        timer.scheduleAtFixedRate(timerTask, 0, pingModel.getInterval() * 1000);
    }
    
     public void kill()
    {
        if(timer != null)
        {
            try {
                PreparedStatement ps = SQLite.getConnection().prepareStatement("UPDATE ping_log SET date_ended = ? WHERE ping_log_id = ?");
                dateEnded = DateUtils.getCurrentDateTimeAsString();
                ps.setString(1, dateEnded);
                ps.setInt(2, pingLogId);
                ps.execute();
                
            } catch (SQLException ex) {
                 System.out.println(ex);
            }
            timer.cancel();
        }
        interrupt();
    }
     
     public void pingIpHost()
     {
        Boolean isReachable = true;
        try
        {
            final IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest ();
            request.setHost (pingModel.getIpHostname());
            final IcmpPingResponse response = IcmpPingUtil.executePingRequest (request);
            final String formattedResponse = IcmpPingUtil.formatResponse (response);
            if(!formattedResponse.startsWith("Error:"))
            {               
                isReachable = true;
            }
            else
            {
                System.out.println("Socket is success " + pingModel.getIpHostname() + ":" + pingModel.getPort());
                isReachable = false;
                failCount++;
                currentStatus = false;
            }
        }
        catch(Exception e)
        {
            isReachable = false;
        }
        finally
        {
            if(isReachable)
            {
                successCount++;
                currentStatus = true;
                System.out.println("Socket is success " + pingModel.getIpHostname() + ":" + pingModel.getPort());
            }
            else
            {
                if(requestCounter >= 3)
                {
                    requestCounter = 0;
                    failCount++;
                    currentStatus = false;
                    System.out.println("Failed to connect to " + pingModel.getIpHostname());
                    MailerModel.sendToAllMailList("Alert From PingNet(" + pingModel.getIpHostname() + ")", "Ping is failed " + pingModel.getIpHostname() + ":" + pingModel.getPort());
                }
                else
                {
                    System.out.println("Failed retrying...");
                    requestCounter++;
                    try
                    {
                        sleep(1000);
                    } catch (InterruptedException ex)
                    {
                        Logger.getLogger(RequestLogModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    pingIpHost();
                }
            }
        }
     }
     
     public void setListener(PingLogListener listener)
    {
        this.listener = listener;
    }
     
    public interface PingLogListener
    {
        void onPingLogUpdated(PingLogModel pingLogModel);
    }
    
    
    
}
