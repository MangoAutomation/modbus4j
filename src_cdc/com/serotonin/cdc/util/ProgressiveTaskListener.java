/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.

    This program is free software; you can redistribute it and/or modify
    it under the terms of version 2 of the GNU General Public License as 
    published by the Free Software Foundation and additional terms as 
    specified by Serotonin Software Technologies Inc.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA

 	@author Matthew Lohbihler
 */

package com.serotonin.cdc.util;

/**
 * @author Matthew Lohbihler
 *
 */
public interface ProgressiveTaskListener {
    /**
     * Optionally called occasionally by the task to declare the progress that has been made.
     * @param progress float between 0 and 1 where 0 is no progress and 1 is completed.
     */
    void progressUpdate(float progress);
    
    /**
     * Notification that the task has been cancelled. Should only be called once for the task.
     */
    void taskCancelled();
    
    /**
     * Notification that the task has been completed. Should only be called once for the task.
     */
    void taskCompleted();
}
