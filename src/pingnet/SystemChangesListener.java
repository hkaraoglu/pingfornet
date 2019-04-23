/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingnet;

import model.HardwareModel;

/**
 *
 * @author hasankaraoglu
 */
public interface SystemChangesListener
{
    public void onNewSystemRecordAdded(HardwareModel hardwareModel);
}
