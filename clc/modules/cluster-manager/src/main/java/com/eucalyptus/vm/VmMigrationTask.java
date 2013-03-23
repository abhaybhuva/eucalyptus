/*************************************************************************
 * Copyright 2009-2012 Eucalyptus Systems, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Please contact Eucalyptus Systems, Inc., 6755 Hollister Ave., Goleta
 * CA 93117, USA or visit http://www.eucalyptus.com/licenses/ if you need
 * additional information or have any questions.
 *
 * This file may incorporate work covered under the following copyright
 * and permission notice:
 *
 *   Software License Agreement (BSD License)
 *
 *   Copyright (c) 2008, Regents of the University of California
 *   All rights reserved.
 *
 *   Redistribution and use of this software in source and binary forms,
 *   with or without modification, are permitted provided that the
 *   following conditions are met:
 *
 *     Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *     Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer
 *     in the documentation and/or other materials provided with the
 *     distribution.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *   "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *   FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *   COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *   INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *   BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *   CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *   LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *   ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *   POSSIBILITY OF SUCH DAMAGE. USERS OF THIS SOFTWARE ACKNOWLEDGE
 *   THE POSSIBLE PRESENCE OF OTHER OPEN SOURCE LICENSED MATERIAL,
 *   COPYRIGHTED MATERIAL OR PATENTED MATERIAL IN THIS SOFTWARE,
 *   AND IF ANY SUCH MATERIAL IS DISCOVERED THE PARTY DISCOVERING
 *   IT MAY INFORM DR. RICH WOLSKI AT THE UNIVERSITY OF CALIFORNIA,
 *   SANTA BARBARA WHO WILL THEN ASCERTAIN THE MOST APPROPRIATE REMEDY,
 *   WHICH IN THE REGENTS' DISCRETION MAY INCLUDE, WITHOUT LIMITATION,
 *   REPLACEMENT OF THE CODE SO IDENTIFIED, LICENSING OF THE CODE SO
 *   IDENTIFIED, OR WITHDRAWAL OF THE CODE CAPABILITY TO THE EXTENT
 *   NEEDED TO COMPLY WITH ANY SUCH LICENSES OR RIGHTS.
 ************************************************************************/

package com.eucalyptus.vm;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Parent;
import com.eucalyptus.component.Topology;
import com.eucalyptus.component.id.Eucalyptus;
import com.eucalyptus.util.async.AsyncRequests;
import com.google.common.base.Strings;
import edu.ucsb.eucalyptus.msgs.CreateTagsType;

/**
 * @todo doc
 * @author chris grzegorczyk <grze@eucalyptus.com>
 */
@Embeddable
public class VmMigrationTask {
  @Transient
  private static Logger  LOG = Logger.getLogger( VmMigrationTask.class );
  @Parent
  private VmInstance     vmInstance;
  @Enumerated( EnumType.STRING )
  @Column( name = "metadata_vm_migration_state" )
  private MigrationState state;
  @Column( name = "metadata_vm_migration_source_host" )
  private String         sourceHost;
  @Column( name = "metadata_vm_migration_dest_host" )
  private String         destinationHost;
  
  private VmMigrationTask( ) {}
  
  private VmMigrationTask( VmInstance vmInstance, MigrationState state, String sourceHost, String destinationHost ) {
    this.vmInstance = vmInstance;
    this.state = state;
    this.sourceHost = Strings.nullToEmpty( sourceHost );
    this.destinationHost = Strings.nullToEmpty( destinationHost );
  }
  
  private VmMigrationTask( VmInstance vmInstance, String state, String sourceHost, String destinationHost ) {
    this( vmInstance, MigrationState.defaultValueOf( state ), sourceHost, destinationHost );
  }
  
  public static VmMigrationTask create( VmInstance vmInstance ) {
    return new VmMigrationTask( vmInstance, MigrationState.none, null, null );
  }
  
  public static VmMigrationTask create( VmInstance vmInstance, String state, String sourceHost, String destinationHost ) {
    return new VmMigrationTask( vmInstance, state, sourceHost, destinationHost );
  }
  
  /**
   * Verify and update the local state, src and dest hosts.
   * 
   * @param state
   * @param sourceHost
   * @param destinationHost
   */
  void updateMigrationTask( String state, String sourceHost, String destinationHost ) {
    if ( !Strings.isNullOrEmpty( state ) ) {
      try {
        this.state = MigrationState.defaultValueOf( state );
        this.sourceHost = Strings.nullToEmpty( sourceHost );
        this.destinationHost = Strings.nullToEmpty( destinationHost );
      } catch ( IllegalArgumentException ex ) {
        LOG.error( ex );//should never happen; hello!!?
        this.state = MigrationState.none;
        this.sourceHost = null;
        this.destinationHost = null;
      }
    }
  }
  
  protected VmInstance getVmInstance( ) {
    return this.vmInstance;
  }
  
  protected void setVmInstance( VmInstance vmInstance ) {
    this.vmInstance = vmInstance;
  }
  
  protected MigrationState getState( ) {
    return this.state;
  }
  
  protected void setState( MigrationState state ) {
    this.state = state;
  }
  
  protected String getSourceHost( ) {
    return this.sourceHost;
  }
  
  protected void setSourceHost( String sourceHost ) {
    this.sourceHost = sourceHost;
  }
  
  protected String getDestinationHost( ) {
    return this.destinationHost;
  }
  
  protected void setDestinationHost( String destinationHost ) {
    this.destinationHost = destinationHost;
  }
}
