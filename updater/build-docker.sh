#!/bin/bash

# Do gradle build
cd ..

echo "# Building"
./gradlew build --no-daemon

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
pandoc ./../OPENSOURCELICENSES.md -f markdown -t html -s --metadata title="OPEN SOURCE LICENSES" --toc -H ./opensourcelicenses/style.html -A ./opensourcelicenses/footer.html -o ./web/opensourcelicenses.html

echo "# Generating update"
# shellcheck disable=SC2164
cp -r ForestDefense ./web/updater/ForestDefense