# java-todo
Todo app using SQLite backend and Java SWT frontend. 
Added export functionality via PDF and the ability to email yourself a list of your tasks. 

## Setup Documentation

    sudo dnf install -y sqlite sqlite-devel sqlite-tcl sqlite-jdbc

This project also makes use of the following external jars:
* Java SWT from https://download.eclipse.org/eclipse/downloads/index.html#Latest_Release
* Javax Mail from https://search.maven.org/remotecontent?filepath=javax/mail/mail/1.5.0-b01/mail-1.5.0-b01.jar
* sqlite-jdbc libery from https://search.maven.org/artifact/org.xerial/sqlite-jdbc/3.39.2.0/jar
* Apache PDFbox library from https://www.apache.org/dyn/closer.lua/pdfbox/2.0.27/pdfbox-app-2.0.27.jar 

I may turn this into a Maven or Gradle project later. 

Useful for testing:

    sudo dnf install -y sqlitebrowser
