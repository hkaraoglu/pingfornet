package model;

import data.SQLite;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DateUtils;

/**
 *
 * @author hkaraoglu
 */
public class RequestLogModel extends Thread 
{
    private final RequestModel pingModel;
    private PingRequestListener listener;
    private int pingRequestId;
    private int successCount = 0;
    private int failCount = 0;
    private String dateAdded = "";
    private String dateUpdated = "";
    private String dateEnded = "";
    private Boolean currentStatus = null; 
    private Timer timer;
    private int requestCounter = 0;
        
    public int getSuccessCount()
    {
        return successCount;
    }

    public int getFailCount()
    {
        return failCount;
    }

    public String getDateAdded()
    {
        return dateAdded;
    }

    public String getDateUpdated()
    {
        return dateUpdated;
    }

    public String getDateEnded()
    {
        return dateEnded;
    }

    public RequestModel getPingModel()
    {
        return pingModel;
    }
    
    public RequestLogModel(RequestModel pingModel)
    {
        this.pingModel = pingModel;
    }
    
    public Boolean getCurrentStatus()
    {
        return currentStatus;
    }

    @Override
    public synchronized void start()
    {
        try
        {
            PreparedStatement ps = SQLite.getConnection().prepareStatement("INSERT INTO request_log(request_id, date_added) VALUES(?, ?)");
            ps.setInt(1, pingModel.getRequestId());
            dateAdded = DateUtils.getCurrentDateTimeAsString();
            ps.setString(2, dateAdded);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                pingRequestId = rs.getInt(1);
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
                    PreparedStatement ps = SQLite.getConnection().prepareStatement("UPDATE request_log SET date_modified = ?, success_count = ?, fail_count = ? WHERE request_log_id = ?");
                    dateUpdated = DateUtils.getCurrentDateTimeAsString();
                    ps.setString(1, dateUpdated);
                    ps.setInt(2, successCount);
                    ps.setInt(3, failCount);
                    ps.setInt(4, pingRequestId);
                    ps.execute();
                    listener.onRequestUpdated(RequestLogModel.this);
                } 
                catch (SQLException ex)
                {
                    Logger.getLogger(RequestLogModel.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            }
        };
        timer = new Timer("MyTimer");
        timer.scheduleAtFixedRate(timerTask, 0, pingModel.getInterval() * 1000);
    }
    
    private void pingIpHost()
    {
        String host = "";
        int status = 0;
        Boolean isReachable = false;
        try
        {
            host = pingModel.getIpHost();
            URL url = new URL(pingModel.getProtocol()+ "://" + pingModel.getIpHost() + ":" + pingModel.getPort());
            HttpURLConnection con = (HttpURLConnection) url.openConnection(); 
            con.setRequestMethod("GET");
            con.setReadTimeout(pingModel.getTimeout());
            System.out.println("Sending HTTP Request to " + pingModel.getIpHost());
            status = con.getResponseCode();
            System.out.println("Response Code: " + status);
            
            if (status < 500)
            {
               isReachable = true;
            }
            if(!RequestModel.getResponseCodes(pingModel.getResponseCodes()).contains(status))
            {
               isReachable = false;
            }
        } 
        catch (IOException ex)
        {
            
        } 
        finally 
        {
            if(isReachable)
            {
                successCount++;
                currentStatus = true;
            }
            else
            {
                if(requestCounter >= 3)
                {
                    requestCounter = 0;
                    failCount++;
                    currentStatus = false;
                    System.out.println("Failed to connect to " + host);
                    MailerModel.sendToAllMailList("Alert From PingNet(" + host + ")", "Response code for " + host + " is " + status);
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
    
    public interface PingRequestListener
    {
        void onRequestUpdated(RequestLogModel pingRequest);
    }

    public void setListener(PingRequestListener listener)
    {
        this.listener = listener;
    }
    
    public void kill()
    {
        if(timer != null)
        {
            try {
                PreparedStatement ps = SQLite.getConnection().prepareStatement("UPDATE request_log SET date_ended = ? WHERE request_log_id = ?");
                dateEnded = DateUtils.getCurrentDateTimeAsString();
                ps.setString(1, dateEnded);
                ps.setInt(2, pingRequestId);
                ps.execute();
            } catch (SQLException ex) {
                 System.out.println(ex);
            }
            timer.cancel();
        }
        interrupt();
    }
    
}
