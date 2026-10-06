#!/usr/bin/env python3
"""Build installable plugin ZIP using an installed IntelliJ IDEA SDK, without downloads."""
import argparse
import os
from pathlib import Path
import shutil
import subprocess
import zipfile

parser = argparse.ArgumentParser()
parser.add_argument('--idea-home', default='/Applications/IntelliJ IDEA.app/Contents')
args = parser.parse_args()
root = Path(__file__).resolve().parent
idea = Path(args.idea_home).resolve()
javac = idea / 'jbr/Contents/Home/bin/javac'
if not javac.exists():
    javac = idea / 'jbr/bin/javac'
if not javac.exists():
    raise SystemExit('JDK not found inside IDEA. Pass --idea-home with the IDE installation path.')
build = root / 'build'
classes = build / 'classes'
if classes.exists(): shutil.rmtree(classes)
classes.mkdir(parents=True)
classpath = os.pathsep.join(str(p) for p in sorted((idea / 'lib').rglob('*.jar')))
sources = sorted((root / 'src/main/java').rglob('*.java'))
subprocess.run([str(javac), '--release', '21', '-proc:none', '-encoding', 'UTF-8', '-cp', classpath,
                '-d', str(classes), *map(str, sources)], check=True)
jar = build / 'cloud-test-review.jar'
with zipfile.ZipFile(jar, 'w', zipfile.ZIP_DEFLATED) as archive:
    for base in [classes, root / 'src/main/resources']:
        for file in sorted(base.rglob('*')):
            if file.is_file(): archive.write(file, file.relative_to(base))
output = build / 'cloud-test-review-1.0.0.zip'
with zipfile.ZipFile(output, 'w', zipfile.ZIP_DEFLATED) as archive:
    archive.write(jar, 'cloud-test-review/lib/cloud-test-review.jar')
print(output)
