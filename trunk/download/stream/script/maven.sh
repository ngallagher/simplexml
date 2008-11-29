mvn clean
mkdir -p maven/org/simpleframework/simple-xml/@version@
scp -r -v maven niallg,simpleweb@web.sourceforge.net:/home/groups/s/si/simpleweb/htdocs/.
mvn deploy
svn update
