#!/bin/bash
rm -r /web
mkdir /web
cp -r /web2/* /web
/app/bin/ForestDefense --server
