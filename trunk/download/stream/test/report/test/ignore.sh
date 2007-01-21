#!/bin/bash
if [ -f ignore ]; then
   rm ignore
fi
for TEST in `ls TEST-*`; do
   if [ "$TEST" != '' ] ; then
      echo $TEST >> ignore 
   fi
done
touch ignore
svn propset svn:ignore -F ignore .
rm ignore
