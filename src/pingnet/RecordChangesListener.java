/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingnet;

import model.PingModel;
import model.RequestModel;

/**
 *
 * @author hkaraoglu
 */
public interface RecordChangesListener
{
    void onNewRequestRecordAdded(RequestModel requestModel);
    void onNewPingRecordAdded(PingModel pingModel);
}
