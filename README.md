# java-todo
Todo app using SQLite and Java SWT

## Setup Documentation

    sudo dnf install -y sqlite sqlite-devel sqlite-tcl sqlite-jdbc

This project also makes use of the sqlite-jdbc driver from https://search.maven.org/artifact/org.xerial/sqlite-jdbc/3.39.2.0/jar.

I grabbed the stand alone jar rather than use a build tool like Maven, but that may change later for consistency. 

Useful for testing:

    sudo dnf install -y sqlitebrowser