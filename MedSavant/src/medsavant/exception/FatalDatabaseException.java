/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package medsavant.exception;

/**
 *
 * @author mfiume
 */
public class FatalDatabaseException extends RuntimeException {

    public FatalDatabaseException(String msg) {
        super(msg);
    }

}