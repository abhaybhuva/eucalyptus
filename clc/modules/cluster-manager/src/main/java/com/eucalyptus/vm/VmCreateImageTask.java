/*******************************************************************************
 * Copyright (c) 2009  Eucalyptus Systems, Inc.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, only version 3 of the License.
 * 
 * 
 *  This file is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 * 
 *  You should have received a copy of the GNU General Public License along
 *  with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *  Please contact Eucalyptus Systems, Inc., 130 Castilian
 *  Dr., Goleta, CA 93101 USA or visit <http://www.eucalyptus.com/licenses/>
 *  if you need additional information or have any questions.
 * 
 *  This file may incorporate work covered under the following copyright and
 *  permission notice:
 * 
 *    Software License Agreement (BSD License)
 * 
 *    Copyright (c) 2008, Regents of the University of California
 *    All rights reserved.
 * 
 *    Redistribution and use of this software in source and binary forms, with
 *    or without modification, are permitted provided that the following
 *    conditions are met:
 * 
 *      Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 * 
 *      Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 * 
 *    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 *    IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 *    TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *    PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 *    OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *    EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *    PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *    PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *    NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. USERS OF
 *    THIS SOFTWARE ACKNOWLEDGE THE POSSIBLE PRESENCE OF OTHER OPEN SOURCE
 *    LICENSED MATERIAL, COPYRIGHTED MATERIAL OR PATENTED MATERIAL IN THIS
 *    SOFTWARE, AND IF ANY SUCH MATERIAL IS DISCOVERED THE PARTY DISCOVERING
 *    IT MAY INFORM DR. RICH WOLSKI AT THE UNIVERSITY OF CALIFORNIA, SANTA
 *    BARBARA WHO WILL THEN ASCERTAIN THE MOST APPROPRIATE REMEDY, WHICH IN
 *    THE REGENTS’ DISCRETION MAY INCLUDE, WITHOUT LIMITATION, REPLACEMENT
 *    OF THE CODE SO IDENTIFIED, LICENSING OF THE CODE SO IDENTIFIED, OR
 *    WITHDRAWAL OF THE CODE CAPABILITY TO THE EXTENT NEEDED TO COMPLY WITH
 *    ANY SUCH LICENSES OR RIGHTS.
 *******************************************************************************
 * @author chris grzegorczyk <grze@eucalyptus.com>
 */

package com.eucalyptus.vm;

import java.util.Date;
import javax.persistence.Column;
import org.hibernate.annotations.Parent;

public class VmCreateImageTask {
  @Parent
  private VmInstance vmInstance;
  @Column( name = "metadata_vm_createimage_id" )
  private String     createImageId;
  @Column( name = "metadata_vm_createimage_state" )
  private String     state;
  @Column( name = "metadata_vm_createimage_start_time" )
  private Date       startTime;
  @Column( name = "metadata_vm_createimage_update_time" )
  private Date       updateTime;
  @Column( name = "metadata_vm_createimage_progress" )
  private String     progress;
  @Column( name = "metadata_vm_createimage_error_msg" )
  private String     errorMessage;
  @Column( name = "metadata_vm_createimage_error_code" )
  private String     errorCode;
  
  VmCreateImageTask( ) {
    super( );
  }
  
  VmCreateImageTask( final VmInstance vmInstance, final String createImageId, final String state, final Date startTime, final Date updateTime,
                     final String progress, final String errorMessage,
                     final String errorCode ) {
    super( );
    this.vmInstance = vmInstance;
    this.createImageId = createImageId;
    this.state = state;
    this.startTime = startTime;
    this.updateTime = updateTime;
    this.progress = progress;
    this.errorMessage = errorMessage;
    this.errorCode = errorCode;
  }
  
  private VmInstance getVmInstance( ) {
    return this.vmInstance;
  }
  
  private void setVmInstance( final VmInstance vmInstance ) {
    this.vmInstance = vmInstance;
  }
  
  String getCreateImageId( ) {
    return this.createImageId;
  }
  
  private void setCreateImageId( final String bundleId ) {
    this.createImageId = bundleId;
  }
  
  String getState( ) {
    return this.state;
  }
  
  void setState( final String state ) {
    this.state = state;
  }
  
  private Date getStartTime( ) {
    return this.startTime;
  }
  
  private void setStartTime( final Date startTime ) {
    this.startTime = startTime;
  }
  
  private Date getUpdateTime( ) {
    return this.updateTime;
  }
  
  void setUpdateTime( final Date updateTime ) {
    this.updateTime = updateTime;
  }
  
  private String getProgress( ) {
    return this.progress;
  }
  
  private void setProgress( final String progress ) {
    this.progress = progress;
  }
  
  private String getErrorMessage( ) {
    return this.errorMessage;
  }
  
  private void setErrorMessage( final String errorMessage ) {
    this.errorMessage = errorMessage;
  }
  
  private String getErrorCode( ) {
    return this.errorCode;
  }
  
  private void setErrorCode( final String errorCode ) {
    this.errorCode = errorCode;
  }
  
  @Override
  public int hashCode( ) {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( this.createImageId == null )
      ? 0
      : this.createImageId.hashCode( ) );
    return result;
  }
  
  @Override
  public boolean equals( final Object obj ) {
    if ( this == obj ) {
      return true;
    }
    if ( obj == null ) {
      return false;
    }
    if ( this.getClass( ) != obj.getClass( ) ) {
      return false;
    }
    final VmCreateImageTask other = ( VmCreateImageTask ) obj;
    if ( this.createImageId == null ) {
      if ( other.createImageId != null ) {
        return false;
      }
    } else if ( !this.createImageId.equals( other.createImageId ) ) {
      return false;
    }
    return true;
  }
  
}