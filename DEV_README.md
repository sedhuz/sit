
## Move : Resources To Bin

Copy all files in resources to bin
``` bash
cp -r resources/* bin/
```
- `-r` : recursive flag

## Compile : Java To Class

Common command
``` bash
javac -d bin src/**/*.java
find src -name "*.java" -exec javac -source 11 -target 11 -d bin {} +
```
- `-d bin` : output dir = bin.
- `src/**/*.java` : `**` matches any subdir recursively. `*.java` matches all java files

If u need to specify a java version - use this
``` bash
javac -source 11 -target 11 -d bin src/**/*.java
```

## Compile : Class To Jar

```bash
jar cfm sit.jar MANIFEST.MF -C bin/ .
```
- `-c` : Create JAR.
- `-f` : Specify output file (sit.jar).
- `-m` : Use MANIFEST.MF.
- `-C bin/ .` : Include all compiled classes from bin/.

## Run : Jar

```bash
java -jar sit.jar <command>
```
- kindly note that the MANIFEST.MF need to have to a blank line at end (as defined in Jar Spec)

## Run : Bash Jar Wrapper (for mac & linux)
```bash
scripts/sit <command>
```
- give permission to execute for `chmod +x scripts/sit`

## Run : Jar Wrapper (for windows)
```bash
scripts/sit.bat <command>
```

---
# Todo
- add gradle
