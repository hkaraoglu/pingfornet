package model;

import data.SQLite;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ValidatorUtils;
import static util.ValidatorUtils.isNumber;

/**
 *
 * @author hkaraoglu
 */
public class RequestModel
{
    private final int requestId;
    private final String protocol;
    private final String ipHost;
    private final int port;
    private final int interval;
    private final int timeout;
    private final String responseCodes;
    
    public RequestModel(int requestId, String protocol, String ipHost, int port, int requestInterval, int requestTimeout, String responseCodes)
    {
        this.requestId = requestId;
        this.protocol = protocol;
        this.ipHost = ipHost;
        this.port = port;
        this.interval = requestInterval;
        this.timeout = requestTimeout;
        this.responseCodes = responseCodes;
    }

    public int getRequestId()
    {
        return requestId;
    }
    
    public String getIpHost()
    {
        return ipHost;
    }

    public int getPort()
    {
        return port;
    }

    public int getInterval()
    {
        return interval;
    }
    
    public int getTimeout()
    {
        return timeout;
    }

    public String getProtocol()
    {
        return protocol;
    }
    
    public String getResponseCodes()
    {
        return responseCodes;
    }
        
    public static ArrayList<RequestLogModel> getList()
    {
        ArrayList<RequestLogModel> pingRequestList = new ArrayList<>();
        try
        {
            PreparedStatement ps = SQLite.getConnection().prepareStatement("SELECT * FROM request");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                RequestModel ping = new RequestModel(
                        rs.getInt("request_id"), 
                        rs.getString("protocol"), 
                        rs.getString("ip_host"), 
                        rs.getInt("port"), 
                        rs.getInt("interval"), 
                        rs.getInt("timeout"),
                        rs.getString("response_codes")
                );
                RequestLogModel pingRequest = new RequestLogModel(ping);
                pingRequestList.add(pingRequest);
            }
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pingRequestList;
    }
    
    public static ResultModel add(String protocol, String ipHost, String port, String interval, String timeout, String responseCodes)
    {
        ResultModel result = new ResultModel();
        ArrayList<Integer> responseCodeList = getResponseCodes(responseCodes);
        if(!ValidatorUtils.isIpAddress(ipHost) && !ValidatorUtils.isHostname(ipHost))
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
        else if(!ValidatorUtils.isNumber(timeout))
        { 
            result.message = "Invalid timeout!";
        }
        else if(responseCodeList.isEmpty())
        {
            result.message = "Invalid Response Code Pattern! (Ex:200 or 200,300 or 200-300)";
        }
        else
        {
            try
            {
                PreparedStatement ps = SQLite.getConnection().prepareStatement("INSERT INTO request(protocol, ip_host, port, interval, timeout, response_codes) VALUES(?, ?, ?, ?, ?, ?)");
                ps.setString(1, protocol);
                ps.setString(2, ipHost);
                ps.setInt(3, Integer.parseInt(port));
                ps.setInt(4, Integer.parseInt(interval));
                ps.setInt(5, Integer.parseInt(timeout));
                ps.setString(6, responseCodes);
                ps.execute();
                if(ps.getUpdateCount() > 0)
                {
                    result.success = true;
                }
                else
                {
                    result.message = "Kayıt eklenemedi!";
                }
                ResultSet rs = ps.getGeneratedKeys();
                int pingId = 0;
                if(rs.next()){
                    pingId = rs.getInt(1);
                }
                RequestModel pingModel = new RequestModel(pingId, protocol, ipHost, Integer.parseInt(port), Integer.parseInt(interval), Integer.parseInt(timeout), responseCodes);
                result.data = pingModel;
            } 
            catch (SQLException ex)
            {
                if(ex.getErrorCode() == 19)
                {
                    result.message = "Bu kayıt daha önceden eklenmiş!";
                }
                else
                {
                    result.message = ex.getMessage();
                }
            }
            finally
            {
                SQLite.closeConnection();
            }
        }
        return result;
    }
    
    public static ArrayList<Integer> getResponseCodes(String responseCodesText)
    {
        ArrayList<Integer> responseCodes = new ArrayList<>();
        if(ValidatorUtils.isNumber(responseCodesText))
        {
            responseCodes.add(Integer.parseInt(responseCodesText));
        }
        else
        {
            String[] numbers = responseCodesText.split("-");
            if(numbers.length == 2)
            {
                if(ValidatorUtils.isNumber(numbers[0]) && ValidatorUtils.isNumber(numbers[1]))
                {
                    int start = Integer.parseInt(numbers[0]);
                    int end   = Integer.parseInt(numbers[1]);
                    if(end < start)
                    {
                        return responseCodes;
                    }
                    for(int i = start; i <= end; i++)
                    {
                        responseCodes.add(i);
                    }
                    return responseCodes;
                }
            }
            else
            {
                numbers = responseCodesText.split(",");
                for(String number : numbers)
                {
                    if(!isNumber(number))
                    {
                        return new ArrayList<>();
                    }
                    else
                    {
                        responseCodes.add(Integer.parseInt(number));
                    }
                }
                return responseCodes;
            }
        }
        return responseCodes;
    }
    
    public void delete()
    {
        try
        {
            PreparedStatement ps = SQLite.getConnection().prepareStatement("DELETE FROM request WHERE request_id = ?");
            ps.setInt(1, requestId);
            ps.execute();
            
        } catch (SQLException ex)
        {
            Logger.getLogger(RequestModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
