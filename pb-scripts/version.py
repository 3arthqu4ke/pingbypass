from os import path, linesep
from sys import argv
import fileinput
import subprocess
import sys
import re


def update(file, regex, value):
    print(f"Checking file {file} for {regex}, replacing with {value}...")
    with fileinput.input(file, inplace=True) as f:
        for line in f:
            if re.match(regex, line):
                line = re.sub(regex, f"\\g<1>{value}\\g<2>", line)
            print(line, end='')
    return file


def updateMinecraftProject(paths, base_path):
    paths.append(update(
        path.join(base_path, 'src', 'main', 'resources', 'META-INF', 'mods.toml'),
        r"(.*version=\").*(\".*)", version))
    paths.append(update(
        path.join(base_path, 'src', 'main', 'resources', 'fabric.mod.json'),
        r"(.*\"version\": \").*(\",.*)", version))


if __name__ == '__main__':
    if len(argv) < 2:
        version = input(f"Please input the version to update to...{linesep}")
        used_input = True
    else:
        version = argv[1]
        used_input = False
    if used_input or (len(argv) > 2 and argv[2] == '-f') or input(f"Set version to {version} (y/n)?{linesep}") == 'y':
        base = path.abspath(path.join(path.dirname(__file__), '..'))
        paths = []
        updateMinecraftProject(paths, path.join(base, 'pb-server'))
        updateMinecraftProject(paths, path.join(base, 'pb-client'))
        updateMinecraftProject(paths, path.join(base, 'pb-security', 'plugin'))
        paths.append(update(path.join(base, 'gradle.properties'), r"(.*project_version=).*(.*)", version))

        if '-nogit' not in argv:
            subprocess.run(['git', 'reset'], stdout=sys.stdout, stderr=sys.stderr)
            for path in paths:
                subprocess.run(['git', 'add', path], stdout=sys.stdout, stderr=sys.stderr)
            subprocess.run(['git', 'commit', '-m', f"[{version}] Bump version"], stdout=sys.stdout, stderr=sys.stderr)
    else:
        print("Cancelled version update!")
