#!/bin/bash
if [ -f ignore ]; then
   rm ignore
fi
echo ssh.properties >> ignore 
echo .settings >> ignore
echo jar >> ignore
echo target >> ignore
echo bin >> ignore
echo output >> ignore
touch ignore
svn propset svn:ignore -F ignore .
rm ignore
