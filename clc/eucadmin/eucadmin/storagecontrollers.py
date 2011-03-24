#!/usr/bin/env python
# Copyright (c) 2011, Eucalyptus Systems, Inc.
# All rights reserved.
#
# Redistribution and use of this software in source and binary forms, with or
# without modification, are permitted provided that the following conditions
# are met:
#
#   Redistributions of source code must retain the above
#   copyright notice, this list of conditions and the
#   following disclaimer.
#
#   Redistributions in binary form must reproduce the above
#   copyright notice, this list of conditions and the
#   following disclaimer in the documentation and/or other
#   materials provided with the distribution.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
# ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
# LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
# CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
# ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
# POSSIBILITY OF SUCH DAMAGE.
#
# Author: Mitch Garnaat mgarnaat@eucalyptus.com

import boto,sys,eucadmin,re
from boto.exception import EC2ResponseError
from eucadmin.generic import BooleanResponse
from eucadmin import EucaAdmin
from optparse import OptionParser

SERVICE_PATH = '/services/Configuration'

class StorageController():
  
  def __init__(self, storage_name=None, host_name=None,
               port=None, partition=None, state=None):
    self.storage_name = storage_name
    self.host_name = host_name
    self.port = port
    self.partition = partition
    self.state = state
    self.euca = EucaAdmin(path=SERVICE_PATH)
          
  def __repr__(self):
      return 'STORAGE\t%s\t%s\t%s\t%s\t%s' % (self.partition, self.storage_name,
                                      self.host_name, self.state, self.detail) 

  def startElement(self, name, attrs, connection):
      return None

  def endElement(self, name, value, connection):
    if name == 'euca:detail':
      self.detail = value
    elif name == 'euca:state':
      self.state = value
    elif name == 'euca:hostName':
      self.host_name = value
    elif name == 'euca:name':
      self.storage_name = value
    elif name == 'euca:partition':
      self.partition = value
    else:
      setattr(self, name, value)
  
  def get_describe_parser(self):
    parser = OptionParser("usage: %prog [NAME...]",
                          version="Eucalyptus %prog VERSION")
    return parser.parse_args()

  def cli_describe(self):
    (options, args) = self.get_describe_parser()
    self.describe(args)

  def describe(self,scs=None):
    params = {}
    if scs:
      self.euca.connection.build_list_params(params,groups,'Names')
    try:
      list = self.euca.connection.get_list('DescribeStorageControllers',
                                           params,
                                           [('euca:item', StorageController)])
      for i in list:
        print i
    except EC2ResponseError, ex:
      self.euca.handle_error(ex)

  def get_register_parser(self):
    parser = OptionParser("usage: %prog [options] NAME",
                          version="Eucalyptus %prog VERSION")
    parser.add_option("-H","--host",dest="host",
                      help="Hostname of the storage.")
    parser.add_option("-p","--port",dest="port",type="int",default=8773,
                      help="Port for the storage.")
    parser.add_option("-P","--partition",dest="partition",
                      help="Partition for the storage.")
    (options,args) = parser.parse_args()    
    if options.host == None:
      self.euca.handle_error("You must provide a hostname (-H or --host)")
    if len(args) != 1:
      print "ERROR  Required argument NAME is missing or malformed."
      parser.print_help()
      sys.exit(1)
    else:
      return (options,args)  

  def cli_register(self):
    (options, args) = self.get_register_parser()
    self.register(args[0], options.host,
                  options.port, options.partition)

  def register(self, name, host, port=8773, partition=None):
    params = {'Name':name,
              'Host':host,
              'Port':port}
    if partition:
      params['Partition'] = partition
    try:
      reply = self.euca.connection.get_object('RegisterStorageController',
                                              params, BooleanResponse)
      print reply
    except EC2ResponseError, ex:
      self.euca.handle_error(ex)

  def get_deregister_parser(self):
    parser = OptionParser("usage: %prog [options] NAME",
                          version="Eucalyptus %prog VERSION")
    parser.add_option("-P","--partition",dest="partition",
                      help="Partition for the storage.")                          
    (options,args) = parser.parse_args()    
    if len(args) != 1:
      print "ERROR  Required argument NAME is missing or malformed."
      parser.print_help()
      sys.exit(1)
    else:
      return (options,args)  

  def cli_deregister(self):
    (options, args) = self.get_deregister_parser()
    self.deregister(args[0])

  def deregister(self, name, partition=None):
    params = {'Name':name}
    if partition:
      params['Partition'] = partition
    try:
      reply = self.euca.connection.get_object('DeregisterStorageController',
                                              params,
                                              BooleanResponse)
      print reply
    except EC2ResponseError, ex:
      self.euca.handle_error(ex)
        
  def get_modify_parser(self):
    parser = OptionParser("usage: %prog [options]",
                          version="Eucalyptus %prog VERSION")
    parser.add_option("-p","--property",dest="props",
                      action="append",
                      help="Modify KEY to be VALUE.  Can be given multiple times.",
                      metavar="KEY=VALUE")
    parser.add_option("-P","--partition",dest="partition",
                      help="Partition for the storage.")                          
    (options,args) = parser.parse_args()    
    if len(args) != 1:
      print "ERROR  Required argument SCNAME is missing or malformed."
      parser.print_help()
      sys.exit(1)
    if not options.props:
      print "ERROR No options were specified."
      parser.print_help()
      sys.exit(1)
    for i in options.props:
      if not re.match("^[\w.]+=[\w\.]+$",i):
        print "ERROR Options must be of the form KEY=VALUE.  Illegally formatted option: %s" % i
        parser.print_help()
        sys.exit(1)
    return (options,args)

  def cli_modify(self):
    (options,args) = self.get_modify_parser()
    self.modify(options.partition,args[0],options.props)

  def modify(self,partition,name,modify_list):
    for entry in modify_list:
      key, value = entry.split("=")
      try:
        reply = self.euca.connection.get_object('ModifyStorageControllerAttribute',
                                                {'Partition' : partition, 'Name' : name, 'Attribute' : key,'Value' : value},
                                                BooleanResponse)
        print reply
      except EC2ResponseError, ex:
        self.euca.handle_error(ex)
