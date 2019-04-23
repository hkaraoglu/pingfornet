package model;


import com.jcabi.ssh.Shell;
import com.jcabi.ssh.Shell.Plain;
import com.jcabi.ssh.Ssh;
import data.SQLite;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DateUtils;


/**
 *
 * @author hasankaraoglu
 */
public class HardwareLogModel extends Thread
{
    private HardwareModel hardwareModel;
    private int hardwareLogId;
    private double cpuUsage = 0;
    private double ramUsage = 0;
    private double diskUsage = 0;
    private Plain terminal;
    private Timer timer;
    private String dateAdded = "";
    private String dateUpdated = "";
    private String dateEnded = "";
    private int cpuOverflowCount = 0;
    private int ramOverflowCount = 0;
    private int diskOverflowCount = 0;
    private HardwareLogListener listener;

    public HardwareLogModel(HardwareModel hardwareModel)
    {
        this.hardwareModel = hardwareModel;
    }
    
    public HardwareModel getHardwareModel()
    {
        return hardwareModel;
    }

    public int getHardwareLogId()
    {
        return hardwareLogId;
    }

    public double getCpuUsage()
    {
        return cpuUsage;
    }

    public double getRamUsage()
    {
        return ramUsage;
    }

    public double getDiskUsage()
    {
        return diskUsage;
    }
    
    SSHManager instance;

    
    @Override
    public synchronized void start()
    {
        try
        {
            PreparedStatement ps = SQLite.getConnection().prepareStatement("INSERT INTO hardware_log (hardware_id, date_added) VALUES(?, ?)");
            ps.setInt(1, hardwareModel.getHardwareId());
            dateAdded = DateUtils.getCurrentDateTimeAsString();
            ps.setString(2, dateAdded);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                hardwareLogId = rs.getInt(1);
            }
               instance = new SSHManager(hardwareModel.getUsername(), hardwareModel.getPassword(), hardwareModel.getIpHostname(), "");
                String errorMessage = instance.connect();
                


                if(errorMessage != null)
                {
                   System.out.println(errorMessage);

                   return;
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
      TimerTask timerTask = new TimerTask() 
      {
            @Override
            public void run() {
 
            try {
              String  ramUsageCommand = "echo `free -m | awk '/Mem:/ { printf(\"%3.1f\", $3/$2*100) }'`";
              String  hddUsageCommand = "echo `df -h / | awk '/\\// {print $4}'`";
              String  cpuUsageCommand = "echo \"`LC_ALL=C top -bn1 | grep \"Cpu(s)\" | sed \"s/.*, *\\([0-9.]*\\)%* id.*/\\1/\" | awk '{print $2}'`\"";
              String result = instance.sendCommand(ramUsageCommand);
              ramUsage = Double.parseDouble(result);
              result = instance.sendCommand(hddUsageCommand);
              diskUsage = Double.parseDouble(result.replace("%", ""));
              result = instance.sendCommand(cpuUsageCommand);
              cpuUsage = Double.parseDouble(result.replace("%us,", ""));
              listener.onHardwareLogUpdated(HardwareLogModel.this);
              //  System.out.println(result);

             } catch (Exception e) {
                 instance.connect();
                 e.printStackTrace();
             }
               
            }
        };
        timer = new Timer("MyTimer");
        timer.scheduleAtFixedRate(timerTask, 0, hardwareModel.getInterval() * 1000);
    }

       public void kill()
    {
        if(timer != null)
        {
            try {
                PreparedStatement ps = SQLite.getConnection().prepareStatement("UPDATE hardware_log SET date_ended = ? WHERE hardware_log_id = ?");
                dateEnded = DateUtils.getCurrentDateTimeAsString();
                ps.setString(1, dateEnded);
                ps.setInt(2, hardwareLogId);
                ps.execute();
            } catch (SQLException ex) {
                 System.out.println(ex);
            }
            instance.close();
            timer.cancel();
        }
        interrupt();
    }
    
    public void setListener(HardwareLogListener listener)
    {
        this.listener = listener;
    }
     
    public interface HardwareLogListener
    {
        void onHardwareLogUpdated(HardwareLogModel hardwareLogModel);
    }
    
    
    
}
