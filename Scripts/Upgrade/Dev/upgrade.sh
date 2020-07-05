#!/bin/bash

localVersion=$(xml_grep --text_only /artifact-resolution/data/version /home/pi/OceanBox/updater/MetaLatestOceanBox.xml)

ansible-pull local.yml --url https://github.com/OceanStream-X-MiageSorbonne/Boitier.git --checkout dev -i localhost

wget -O /home/pi/OceanBox/updater/MetaLatestOceanBox.xml "http://176.158.51.172:8081/nexus/service/local/artifact/maven/resolve?g=oceanbox&a=OceanBox&v=LATEST&r=DevOceanBoxRelease&e=jar"

repoVersion=$(xml_grep --text_only /artifact-resolution/data/version /home/pi/OceanBox/updater/MetaLatestOceanBox.xml)

if [ "$repoVersion" != "$localVersion" ]
then
	wget -P /home/pi/OceanBox/bin "http://176.158.51.172:8081/nexus/service/local/artifact/maven/redirect?g=oceanbox&a=OceanBox&v=LATEST&r=DevOceanBoxRelease&e=jar" --content-disposition
	rm -f "/home/pi/OceanBox/bin/OceanBox-$localVersion.jar"
fi

