#!/bin/bash

echo "# Cleaning up"
rm -r tmp/
rm -r ForestDefense/lib
rm -r ForestDefense/bin
rm -r ForestDefense/ForestDefense-app.jar
rm -r ForestDefense/digest.txt
rm -r ForestDefense/digest2.txt
rm ForestDefense/OPENSOURCELICENSES.md

# Do gradle build
cd ..

echo "# Building"
./gradlew clean build

mkdir updater/tmp

echo 'Copying stuff'
cp build/distributions/ForestDefense-app.zip updater/tmp/ForestDefense-app.zip
# shellcheck disable=SC2164
cd updater/tmp/
unzip ForestDefense-app.zip
# shellcheck disable=SC2103
cd ..
cp -r tmp/ForestDefense-app/lib ForestDefense
cp -r tmp/ForestDefense-app/bin ForestDefense
cp ../OPENSOURCELICENSES.md ./ForestDefense/OPENSOURCELICENSES.md
#cp tmp/ForestDefense-app/lib/ForestDefense-app.jar ForestDefense/ForestDefense-app.jar

echo '# Generating digest'
java -classpath getdown-core-1.8.6.jar com.threerings.getdown.tools.Digester ForestDefense

echo '# Generating open source licenses'
cp ../OPENSOURCELICENSES.md ./ForestDefense/OPENSOURCELICENSES.md
pandoc ./../OPENSOURCELICENSES.md -f markdown -t html -s --metadata title="OPEN SOURCE LICENSES" --toc -H ./opensourcelicenses/style.html -A ./opensourcelicenses/footer.html -o ./../../web/opensourcelicenses.html

echo "# Deploying update via git"
# shellcheck disable=SC2164
rm -r ../../web/updater/ForestDefense
cp -r ForestDefense ../../web/updater/ForestDefense
(cd ../../web && git add -A && git commit -m "Eine neue Version ist Verfügbar!"&& git push)

echo "# Cleaning up"
rm -r tmp/
#exit
rm -r ForestDefense/lib
rm -r ForestDefense/bin
rm -r ForestDefense/ForestDefense-app.jar
rm -r ForestDefense/digest.txt
rm -r ForestDefense/digest2.txt
rm ForestDefense/OPENSOURCELICENSES.md