/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ut.biolab.medsavant.db.exception;

/**
 *
 * @author mfiume
 */
public class FatalDatabaseException extends RuntimeException {

    public FatalDatabaseException(String msg) {
        super(msg);
    }

}
