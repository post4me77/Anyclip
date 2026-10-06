#!/usr/bin/env python3
import os
from pathlib import Path
import subprocess
import zipfile
import xml.etree.ElementTree as ET
root = Path(__file__).resolve().parent
idea = Path('/Applications/IntelliJ IDEA.app/Contents')
jdk = idea / 'jbr/Contents/Home/bin'
subprocess.run(['python3', str(root / 'build.py')], check=True)
classpath = os.pathsep.join([str(root / 'build/classes'), *map(str, (idea / 'lib').rglob('*.jar'))])
subprocess.run([str(jdk / 'javac'), '--release', '21', '-proc:none', '-encoding', 'UTF-8', '-cp', classpath,
               '-d', str(root / 'build/test-classes'), str(root / 'src/test/java/com/anyclip/review/ClientContractTest.java')], check=True)
subprocess.run([str(jdk / 'java'), '-cp', os.pathsep.join([str(root / 'build/test-classes'), classpath]),
               'com.anyclip.review.ClientContractTest'], check=True)
with zipfile.ZipFile(root / 'build/cloud-test-review.jar') as jar:
    descriptor = ET.fromstring(jar.read('META-INF/plugin.xml'))
    assert descriptor.find('id').text == 'com.anyclip.cloud-test-review'
    assert 'com/anyclip/review/ReviewAction.class' in jar.namelist()
with zipfile.ZipFile(root / 'build/cloud-test-review-1.0.0.zip') as archive:
    assert archive.namelist() == ['cloud-test-review/lib/cloud-test-review.jar']
print('Plugin packaging checks passed')
