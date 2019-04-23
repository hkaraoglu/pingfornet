package model;

import data.SQLite;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ValidatorUtils;

/**
 *
 * @author hasankaraoglu
 */
public class PingModel
{
    private final int pingId; 
    private final String ipHostname;
    private final int port;
    private final int interval;

    public PingModel(int pingId, String ipHostname, int port, int interval)
    {
        this.pingId = pingId;
        this.ipHostname = ipHostname;
        this.port = port;
        this.interval = interval;
    }

    public int getPingId()
    {
        return pingId;
    }

    public String getIpHostname()
    {
        return ipHostname;
    }

    public int getInterval()
    {
        return interval;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public static List<PingLogModel> getList()
    {
        ArrayList<PingLogModel> pingLogList = new ArrayList<>();
        try
        {
            PreparedStatement ps = SQLite.getConnection().prepareStatement("SELECT * FROM ping");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                PingModel ping = new PingModel(
                        rs.getInt("ping_id"), 
                        rs.getString("ip_hostname"), 
                        rs.getInt("port"), 
                        rs.getInt("interval")
                );
                PingLogModel pingRequest = new PingLogModel(ping);
                pingLogList.add(pingRequest);
            }
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pingLogList;
    }
    
    public static ResultModel insert(String ipHostname, String port, String interval)
    {
        ResultModel result = new ResultModel();
        if(!ValidatorUtils.isIpAddress(ipHostname) && !ValidatorUtils.isHostname(ipHostname))
        {
            result.message = "Invalid ip or hostname!";
        }
        else if(!ValidatorUtils.isPort(port))
        {
            result.message = "Invalid port!";
        }
        else if(!ValidatorUtils.isNumber(interval))
        {
            result.message = "Invalid interval!";
        }
        else
        {
            try
            {
                PreparedStatement ps = SQLite.getConnection().prepareStatement("INSERT INTO ping(ip_hostname, port, interval) VALUES(?, ?, ?)");
                ps.setString(1, ipHostname);
                ps.setInt(2, Integer.parseInt(port));
                ps.setInt(3, Integer.parseInt(interval));
                ps.execute();
                if(ps.getUpdateCount() > 0)
                {
                    result.success = true;
                    ResultSet rs = ps.getGeneratedKeys();
                    int pingId = 0;
                    if(rs.next()){
                        pingId = rs.getInt(1);
                    }
                    PingModel pingModel = new PingModel(pingId, ipHostname, Integer.parseInt(port), Integer.parseInt(interval));
                    result.data = pingModel;
                }
                else
                {
                    result.message = "Record couldn't be added!";
                }
            } catch (SQLException ex)
            {
                result.message = "Record couldn't be added! (" + ex.getMessage() + ")";
            }
        }
        return result;
    }
    
    public void delete()
    {
        try
        {
            PreparedStatement ps = SQLite.getConnection().prepareStatement("DELETE FROM ping WHERE ping_id = ?");
            ps.setInt(1, pingId);
            ps.execute();
            
        } catch (SQLException ex)
        {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
