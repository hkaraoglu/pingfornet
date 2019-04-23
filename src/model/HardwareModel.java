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
public class HardwareModel
{
    private final int hardwareId;
    private final String ipHostname;
    private final String username;
    private final String password;
    private final Boolean isCpuChecked;
    private final Boolean isRamChecked;
    private final Boolean isDiskChecked;
    private final double cpuPercent;
    private final double ramPercent;
    private final double diskPercent;
    private final int interval;

    public HardwareModel(int hardwareId, String ipHostname, String username, String password, 
            Boolean isCpuChecked, Boolean isRamChecked, Boolean isDiskChecked, double cpuPercent, double ramPercent, double diskPercent,
            int interval)
    {
        this.hardwareId = hardwareId;
        this.ipHostname = ipHostname;
        this.username = username;
        this.password = password;
        this.isCpuChecked = isCpuChecked;
        this.isRamChecked = isRamChecked;
        this.isDiskChecked = isDiskChecked;
        this.cpuPercent = cpuPercent;
        this.ramPercent = ramPercent;
        this.diskPercent = diskPercent;
        this.interval = interval;
    }

    public int getHardwareId()
    {
        return hardwareId;
    }
    public String getIpHostname()
    {
        return ipHostname;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public Boolean getIsCpuChecked()
    {
        return isCpuChecked;
    }

    public Boolean getIsRamChecked()
    {
        return isRamChecked;
    }

    public Boolean getIsDiskChecked()
    {
        return isDiskChecked;
    }

    public double getCpuPercent()
    {
        return cpuPercent;
    }

    public double getRamPercent()
    {
        return ramPercent;
    }

    public double getDiskPercent()
    {
        return diskPercent;
    }

    public int getInterval()
    {
        return interval;
    }
    
    public static List<HardwareLogModel> getList()
    {
        ArrayList<HardwareLogModel> hardwareList = new ArrayList<>();
        try
        {
            PreparedStatement ps = SQLite.getConnection().prepareStatement("SELECT * FROM hardware");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                HardwareModel hardware = new HardwareModel(
                        rs.getInt("hardware_id"), 
                        rs.getString("ip_hostname"), 
                        rs.getString("username"), 
                        rs.getString("password"),
                        rs.getBoolean("is_cpu_checked"),
                        rs.getBoolean("is_ram_checked"),
                        rs.getBoolean("is_disk_checked"),
                        rs.getDouble("cpu_warn_percent"),
                        rs.getDouble("ram_warn_percent"),
                        rs.getDouble("disk_warn_percent"),
                        rs.getInt("interval")
                );
                HardwareLogModel hardwareLogModel = new HardwareLogModel(hardware);
                hardwareList.add(hardwareLogModel);
            }
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hardwareList;
    }

    public static ResultModel insert(String ipHostname, String username, String password, 
                  Boolean isCpuChecked, Boolean isRamChecked, 
                  Boolean isDiskChecked, String cpuWarnPercent, String ramWarnPercent, String diskWarnPercent,
                  String interval)
    {
        ResultModel result = new ResultModel();
        if(!ValidatorUtils.isIpAddress(ipHostname) && !ValidatorUtils.isHostname(ipHostname))
        {
            result.message = "Invalid ip or hostname!";
        }
        else if(!ValidatorUtils.isNumber(interval))
        {
            result.message = "Invalid interval value";
        }
        else if(Integer.parseInt(interval) < 15)
        {
            result.message = "Interval value must not be lower than 15";
        }
        else
        {
            try
            {
                PreparedStatement ps = SQLite.getConnection()
                                       .prepareStatement("INSERT INTO hardware(ip_hostname, username, password, is_cpu_checked, is_ram_checked, is_disk_checked, cpu_warn_percent, ram_warn_percent, disk_warn_percent, interval) "
                        + "                               VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, ipHostname);
                ps.setString(2, username);
                ps.setString(3, password);
                ps.setBoolean(4, isCpuChecked);
                ps.setBoolean(5, isRamChecked);
                ps.setBoolean(6, isDiskChecked);
                ps.setDouble(7, Double.parseDouble(cpuWarnPercent));
                ps.setDouble(8, Double.parseDouble(ramWarnPercent));
                ps.setDouble(9, Double.parseDouble(diskWarnPercent));
                ps.setInt(10, Integer.parseInt(interval));
                ps.execute();
                if(ps.getUpdateCount() > 0)
                {
                    result.success = true;
                    ResultSet rs = ps.getGeneratedKeys();
                    int hardwareId = 0;
                    if(rs.next()){
                        hardwareId = rs.getInt(1);
                    }
                    HardwareModel hardwareModel = new HardwareModel(hardwareId, ipHostname, username, 
                                                                    password, isCpuChecked, isRamChecked, 
                                                                    isDiskChecked, Double.parseDouble(cpuWarnPercent),
                                                                    Double.parseDouble(ramWarnPercent), Double.parseDouble(diskWarnPercent),
                                                                    Integer.parseInt(interval));
                    result.data = hardwareModel;
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
            PreparedStatement ps = SQLite.getConnection().prepareStatement("DELETE FROM hardware WHERE hardware_id = ?");
            ps.setInt(1, hardwareId);
            ps.execute();
            
        } catch (SQLException ex)
        {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
