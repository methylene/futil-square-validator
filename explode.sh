#/bin/sh
# Use this while running mvn clean jetty:run-exploded
rsync -v -r "src/main/webapp/" "target/futil-square-validator" --include=*.xhtml --include=*.css --include=*.jpg --include=*.png --include=*.gif --include=*.js --exclude=*~
