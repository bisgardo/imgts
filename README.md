imgts
=====

Compile jar
-----------

From project root:
```
mvn clean compile assembly:single
```

Run
---

From project root:
```
java -jar target/imgts-1.0-SNAPSHOT.jar <files> # Outputs input- and output name separated by TAB.
```

Rename files
------------
```
java -jar target/imgts-1.0-SNAPSHOT.jar <files> > renames.txt
less renames.txt # Check that it looks correct.
                 # Modify file appropriately if it doesn't.
cat renames.txt | while IFS=$'\t' read l r; do
  mv "$l" "$(dirname "$l")/$r" # Rename inside the file's old location.
                               # Prefix command with "echo" to see the effect nondestructively.
done
```
