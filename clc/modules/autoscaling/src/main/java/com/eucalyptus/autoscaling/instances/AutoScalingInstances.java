/*************************************************************************
 * Copyright 2009-2013 Eucalyptus Systems, Inc.
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
 ************************************************************************/
package com.eucalyptus.autoscaling.instances;

import java.util.List;
import com.eucalyptus.autoscaling.metadata.AutoScalingMetadataException;
import com.eucalyptus.util.Callback;
import com.eucalyptus.util.OwnerFullName;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 *
 */
public abstract class AutoScalingInstances {
  public abstract List<AutoScalingInstance> list( OwnerFullName ownerFullName ) throws AutoScalingMetadataException;

  public abstract List<AutoScalingInstance> list( OwnerFullName ownerFullName,
                                                  Predicate<? super AutoScalingInstance> filter ) throws AutoScalingMetadataException;

  public abstract List<AutoScalingInstance> listByGroup( OwnerFullName ownerFullName,
                                                         String groupName ) throws AutoScalingMetadataException;

  public abstract AutoScalingInstance lookup( OwnerFullName ownerFullName,
                                              String instanceId ) throws AutoScalingMetadataException;

  public abstract AutoScalingInstance update( OwnerFullName ownerFullName,
                                              String instanceId,
                                              Callback<AutoScalingInstance> instanceUpdateCallback ) throws AutoScalingMetadataException;

  public abstract boolean delete( AutoScalingInstance autoScalingInstance ) throws AutoScalingMetadataException;

  public abstract AutoScalingInstance save( AutoScalingInstance autoScalingInstance ) throws AutoScalingMetadataException;

  public static Function<AutoScalingInstance,String> launchConfigurationName() {
    return AutoScalingInstanceProperties.LAUNCH_CONFIGURATION_NAME;  
  }
  
  private enum AutoScalingInstanceProperties implements Function<AutoScalingInstance,String> {
    LAUNCH_CONFIGURATION_NAME {
      @Override
      public String apply( final AutoScalingInstance autoScalingInstance ) {
        return autoScalingInstance.getLaunchConfigurationName();
      }
    }
  }
}
