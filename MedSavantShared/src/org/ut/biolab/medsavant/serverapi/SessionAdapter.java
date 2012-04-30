/*
 *    Copyright 2011-2012 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.ut.biolab.medsavant.serverapi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import org.ut.biolab.medsavant.clientapi.ClientCallbackAdapter;


/**
 *
 * @author mfiume
 */
public interface SessionAdapter extends Remote {

    String registerNewSession(String uname, String pw, String dbname) throws RemoteException, SQLException;
    void unregisterSession(String sessionId) throws RemoteException, SQLException;
    boolean testConnection(String sessionId) throws RemoteException, SQLException;    
    void registerCallback(String sessionId, ClientCallbackAdapter cca) throws RemoteException;
    
}